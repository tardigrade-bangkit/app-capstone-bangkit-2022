package com.tardigrade.capstonebangkit.view.child.material

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.MaterialContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.databinding.FragmentMaterialBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel

class MaterialFragment : Fragment(), MediaPlayer.OnPreparedListener {
    private val viewModel by viewModels<MaterialViewModel>() {
        MaterialViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private val lessonContentViewModel: LessonContentViewModel by activityViewModels()

    private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
    private val lessonRepository = LessonRepository(ApiConfig.getApiService())
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var audioUrl: String? = null
    private var binding: FragmentMaterialBinding? = null
    private var listMaterialContent: List<MaterialContent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMaterialBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer.apply {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            setAudioAttributes(attributes)
        }

        viewModel.currentMaterialContent.observe(viewLifecycleOwner) {
            Log.d("MaterialFragment", it.toString())
            if (it != null) {
                setMaterialContent(it)
            }
        }

        viewModel.listMaterialContent.observe(viewLifecycleOwner) { contents ->
            when(contents) {
                is Result.Success -> {
                    Log.d("materialcontent success", contents.data.toString())
                    viewModel.setCurrentMaterialContent(contents.data[0])
                    listMaterialContent = contents.data
                }
                is Result.Error -> {
                    val error = contents.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                lessonContentViewModel.currentLessonContent?.materialId?.let { viewModel.getMaterial(it) }
                            }
                        }
                    }
                }
                is Result.Loading -> {

                }
            }
        }

        Log.d("MaterialFragment", "currentLessonContent: ${lessonContentViewModel.currentLessonContent.toString()}")
        lessonContentViewModel.currentLessonContent?.materialId?.let { viewModel.getMaterial(it) }

        binding?.apply {
            btnBack.setOnClickListener {findNavController().navigate(R.id.action_materialFragment_to_homeFragment)}
            btnNextSlide.setOnClickListener {nextSlide()}
            btnPreviousSlide.setOnClickListener {previousSlide()}
            btnReplaySlide.setOnClickListener {
                mediaPlayer.apply {
                    stop()
                    prepareAudio()
                }
            }
        }
    }

    private fun setMaterialContent(content: MaterialContent) {
        binding?.apply {
            Glide.with(this@MaterialFragment)
                .asBitmap()
                .load(content.imageUrl)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        root.background = BitmapDrawable(context?.resources, resource)
                    }
                })

            with(lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(currentLesson?.title).append(" - ")
                    .append(currentLessonContent?.title).append(" - ")
                    .append(content.order)
            }
        }

        audioUrl = content.audioUrl
        prepareAudio()
    }

    private fun prepareAudio() {
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

    private fun nextSlide() {
        mediaPlayer.stop()
        mediaPlayer.reset()

        val currentIndex = listMaterialContent?.indexOf(viewModel.currentMaterialContent.value)
        if (currentIndex != null && currentIndex < (listMaterialContent?.size?.minus(1) ?: 0)) {
            viewModel.setCurrentMaterialContent(listMaterialContent?.get(currentIndex + 1))
        } else {
            lessonContentViewModel.getNextLessonContent()
            val nextLessonContent = lessonContentViewModel.currentLessonContent
            if (nextLessonContent == null) findNavController().navigate(R.id.action_materialFragment_to_homeFragment)
            with(nextLessonContent) {
                if (this?.materialId != null) findNavController().navigate(R.id.action_materialFragment_self)
                else if (this?.quizzesId != null) findNavController().navigate(R.id.action_materialFragment_to_quizFragment)
            }
        }
    }

    private fun previousSlide() {
        mediaPlayer.stop()
        mediaPlayer.reset()

        val currentIndex = listMaterialContent?.indexOf(viewModel.currentMaterialContent.value)
        if (currentIndex != null && currentIndex > 0) {
            viewModel.setCurrentMaterialContent(listMaterialContent?.get(currentIndex - 1))
        } else {
            lessonContentViewModel.getPreviousContent()
            val prevLessonContent = lessonContentViewModel.currentLessonContent
            if (prevLessonContent == null) findNavController().navigate(R.id.action_materialFragment_to_homeFragment)
            with(prevLessonContent) {
                if (this?.materialId != null) findNavController().navigate(R.id.action_materialFragment_self)
                else if (this?.quizzesId != null) findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}