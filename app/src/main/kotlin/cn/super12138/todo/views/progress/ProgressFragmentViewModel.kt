package cn.super12138.todo.views.progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.super12138.todo.logic.Repository

class ProgressFragmentViewModel : ViewModel() {
    val totalCount: MutableLiveData<Int> = MutableLiveData()
    val completeCount: MutableLiveData<Int> = MutableLiveData()
    val progress: MutableLiveData<Int> = MutableLiveData()

    fun updateProgress() {
        val progressData = Repository.getCompleteTotalCount()
        val total = progressData.total
        val complete = progressData.complete
        val calcProgress = (complete.toDouble() / total.toDouble()) * 100

        progress.value = calcProgress.toInt()
        totalCount.value = total
        completeCount.value = complete
    }
}