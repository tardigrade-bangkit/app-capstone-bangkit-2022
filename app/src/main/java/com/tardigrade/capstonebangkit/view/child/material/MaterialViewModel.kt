package com.tardigrade.capstonebangkit.view.child.material

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.MaterialContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import com.tardigrade.capstonebangkit.view.child.home.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MaterialViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
    ) : ViewModel() {
    private val _listMaterialContent = MutableLiveData<Result<List<MaterialContent>>>()
    val listMaterialContent: MutableLiveData<Result<List<MaterialContent>>> = _listMaterialContent

    private val _currentMaterialContent = MutableLiveData<MaterialContent?>()
    val currentMaterialContent: MutableLiveData<MaterialContent?> = _currentMaterialContent

    fun setCurrentMaterialContent(materialContent: MaterialContent?) {
        _currentMaterialContent.value = materialContent
    }

    fun getMaterial(materialId: Int) {
//        _listMaterialContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val material = lessonRepository.getMaterial(token, materialId)

                _listMaterialContent.value = Result.Success(material)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _listMaterialContent.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _listMaterialContent.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MaterialViewModel(lessonRepository, token) as T
        }
    }
}