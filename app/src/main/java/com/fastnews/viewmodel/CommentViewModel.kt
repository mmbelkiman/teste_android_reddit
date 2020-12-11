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
    private lateinit var comments: MutableLiveData<List<CommentData>>
//endregion

    //region GET
    fun getComments(postId: String): LiveData<List<CommentData>> {
        comments = MutableLiveData()
        Coroutines.ioThenMain({
            CommentRepository.getComments(postId)
        }) {
            comments.postValue(it.orEmpty() as MutableList<CommentData>?)
        }
        return comments
    }
//endregion
}