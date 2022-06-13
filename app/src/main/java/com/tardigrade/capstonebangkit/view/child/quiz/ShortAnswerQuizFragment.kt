package com.tardigrade.capstonebangkit.view.child.quiz

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.databinding.FragmentMultipleChoiceQuizBinding
import com.tardigrade.capstonebangkit.databinding.FragmentShortAnswerQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.Manifest

class ShortAnswerQuizFragment : Fragment(), MediaPlayer.OnPreparedListener {
    private val viewModel by viewModels<ShortAnswerQuizViewModel>() {
        ShortAnswerQuizViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private val quizViewModel : QuizViewModel by activityViewModels()
    var answer: String? = null
    var outputFilePath: String? = null
    var shortAnswerId: Int? = null
    val mediaPlayer: MediaPlayer = MediaPlayer()
    val mediaRecorder = MediaRecorder()
    private var binding: FragmentShortAnswerQuizBinding? = null
    private var quizFragment: QuizFragment = this.parentFragment as QuizFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShortAnswerQuizBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer.apply {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            setAudioAttributes(attributes)
            setOnPreparedListener{onPrepared(this)}
        }

        viewModel.shortAnswerQuestion.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    setQuizContent(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getShortAnswerQuestion(quizViewModel.quizContent?.shortAnswerId!!)
                            }
                        }
                    }
                }
                is Result.Loading -> {

                }
            }
        }

        shortAnswerId = quizViewModel.quizContent?.shortAnswerId!!
        viewModel.getShortAnswerQuestion(shortAnswerId!!)

        binding?.apply {
            btnNext.setOnClickListener {
                answer?.let { it1 -> quizViewModel.setAnswer(shortAnswerId!!, it1) }
                quizFragment.nextQuestion()
            }
            btnPrevious.setOnClickListener {
                quizFragment.previousQuestion()
                answer?.let { it1 -> quizViewModel.setAnswer(shortAnswerId!!, it1) }
            }
            btnBack.setOnClickListener { quizFragment.backToHome() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setQuizContent(content: ShortAnswerQuestion) {

        content.qText?.let { binding?.tvQuestion?.text = it }
        content.qImage?.let {
            Glide.with(this@ShortAnswerQuizFragment)
                .asBitmap()
                .load(content.qImage)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding?.imgQuestion?.setImageBitmap(resource)
                    }
                })
        }

        if (content.type > 0) {
            if (!allPermisionGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSION
                )
            }

            binding?.apply {
                cardWarning.visibility = View.VISIBLE
                btnAction.visibility = View.VISIBLE
                edtAnswer.visibility = View.GONE
                btnNext.visibility = View.GONE

                when(content.type) {
                    1 -> {
                        tvWarning.text = getString(R.string.tv_warning_text_photo)
                        imgWarning.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.warning_photo))
                        btnAction.text = getString(R.string.btn_action_text_photo)
                        btnAction.setOnClickListener { recordAudio() }
                    }
                    2 -> {
                        tvWarning.text = getString(R.string.tv_warning_text_audio)
                        imgWarning.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.warning_audio))
                        btnAction.text = getString(R.string.btn_action_text_audio)
                        btnAction.setOnClickListener { startTakePhoto() }
                    }
                }
            }
        }

        binding?.apply{
            with(quizFragment.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(currentLesson?.title).append(" - ")
                    .append(currentLessonContent?.title).append(" - ")
                    .append(content.order)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun recordAudio() {
        outputFilePath = Environment.getExternalStorageDirectory().absolutePath + "/record.3gp"
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(Environment.getExternalStorageDirectory().absolutePath + "/record.3gp")
            prepare()
            start()
        }
        binding?.apply {
            btnAction.text = "Stop Merekam"
            btnAction.setOnClickListener {
                mediaRecorder.stop()
                val b = Files.readAllBytes(Paths.get(outputFilePath))
                answer = Base64.encodeToString(b, Base64.DEFAULT)
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            val baos: ByteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
            val b = baos.toByteArray()
            answer = Base64.encodeToString(b, Base64.DEFAULT)
        }
    }

    private fun allPermisionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun prepareAudio(audioUrl: String) {
        mediaPlayer.apply {
            setDataSource(audioUrl)
            setOnPreparedListener{onPrepared(this)}
            prepareAsync()
        }
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.d("onPrepared", "prepared")
        mediaPlayer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermisionGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
        private const val REQUEST_CODE_PERMISSION = 10
    }
}