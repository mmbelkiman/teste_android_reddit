package com.fastnews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastnews.mechanism.Coroutines
import com.fastnews.repository.CommentRepository
import com.fastnews.service.model.CommentData

/**
 * Comment ViewModel
 */
class CommentViewModel : ViewModel() {
    //region Fields
    private val comments: MutableLiveData<List<CommentData>> by lazy {
        MutableLiveData<List<CommentData>>()
    }
//endregion

    //region GET
    fun getComments(postId: String): LiveData<List<CommentData>> {
        Coroutines.ioThenMain({
            CommentRepository.getComments(postId)
        }) {
            this.comments.postValue(it)
        }
        return this.comments
    }
//endregion
}