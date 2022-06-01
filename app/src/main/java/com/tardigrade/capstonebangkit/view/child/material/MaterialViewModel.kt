package com.tardigrade.capstonebangkit.view.child.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.MaterialContent

class MaterialViewModel : ViewModel() {
    private val _listMaterialContent = MutableLiveData<List<MaterialContent>>()
    val listMaterialContent: MutableLiveData<List<MaterialContent>> = _listMaterialContent

    private val _currentMaterialContent = MutableLiveData<MaterialContent>()
    val currentMaterialContent: MutableLiveData<MaterialContent> = _currentMaterialContent
}