package com.fastnews.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fastnews.mechanism.Coroutines
import com.fastnews.repository.PostRepository
import com.fastnews.service.model.PostData

/**
 * Post ViewModel
 */
class PostViewModel(application: Application) : AndroidViewModel(application) {
    //region Fields
    private val posts: MutableLiveData<MutableList<PostData>> by lazy {
        MutableLiveData<MutableList<PostData>>()
    }
    private val limit = 25
//endregion

    //region GET
    fun getPosts(after: String): LiveData<MutableList<PostData>> {
        Coroutines.ioThenMain({
            PostRepository.getPosts(after, limit)
        }) {
            this.posts.postValue(it.orEmpty() as MutableList<PostData>?)
        }
        return this.posts
    }
//endregion
}