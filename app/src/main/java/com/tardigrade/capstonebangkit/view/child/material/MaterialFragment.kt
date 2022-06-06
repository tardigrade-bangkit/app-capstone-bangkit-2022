package com.tardigrade.capstonebangkit.view.child.material

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.model.MaterialContent
import com.tardigrade.capstonebangkit.databinding.FragmentMaterialBinding
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel

class MaterialFragment : Fragment() {
    private val viewModel by viewModels<MaterialViewModel>()
    private val lessonContentViewModel by viewModels<LessonContentViewModel>()
    private var binding: FragmentMaterialBinding? = null

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

        viewModel.currentMaterialContent.observe(viewLifecycleOwner) {
            Log.i("MaterialFragment", "MaterialContent: $it.order")
            setMaterialContent(it)
        }

        viewModel.listMaterialContent.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.currentMaterialContent.value = it[0]
            }
        }

        val materialContents = arrayListOf(
            MaterialContent(
                order = 1,
                imageUrl = "https://picsum.photos/800/360"
            ),
            MaterialContent(
                order = 2,
                imageUrl = "https://picsum.photos/801/360"
            ),
            MaterialContent(
                order = 3,
                imageUrl = "https://picsum.photos/802/360"
            ),
            MaterialContent(
                order = 4,
                imageUrl = "https://picsum.photos/803/360"
            ),
        )
        viewModel.listMaterialContent.value = materialContents

        binding?.apply {
            btnBack.setOnClickListener {findNavController().navigate(R.id.action_materialFragment_to_homeFragment)}
            btnNextSlide.setOnClickListener {nextSlide()}
            btnPreviousSlide.setOnClickListener {previousSlide()}
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
        }
    }

    private fun nextSlide() {
        val currentIndex = viewModel.listMaterialContent.value?.indexOf(viewModel.currentMaterialContent.value)
        if (currentIndex != null && currentIndex < viewModel.listMaterialContent.value?.size!! - 1) {
            viewModel.currentMaterialContent.value = viewModel.listMaterialContent.value?.get(currentIndex + 1)
        } else {
            findNavController().navigate(R.id.action_materialFragment_to_homeFragment)
        }
    }

    private fun previousSlide() {
        val currentIndex = viewModel.listMaterialContent.value?.indexOf(viewModel.currentMaterialContent.value)
        if (currentIndex != null && currentIndex > 0) {
            viewModel.currentMaterialContent.value = viewModel.listMaterialContent.value?.get(currentIndex - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}