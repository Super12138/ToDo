package cn.super12138.todo.views.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.super12138.todo.logic.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProgressViewModel : ViewModel() {
    val totalCount: MutableLiveData<Int> = MutableLiveData()
    val completeCount: MutableLiveData<Int> = MutableLiveData()
    val remainCount: MutableLiveData<Int> = MutableLiveData()
    val progress: MutableLiveData<Int> = MutableLiveData()

    fun updateProgress() {
        viewModelScope.launch {
            delay(30)
            val total = Repository.getAll().size
            val complete = Repository.getAllComplete().size

            val calcProgress = (complete.toDouble() / total.toDouble()) * 100
            progress.postValue(calcProgress.toInt())
            completeCount.postValue(complete)
            totalCount.postValue(total)
            remainCount.postValue(total - complete)
        }
    }
}