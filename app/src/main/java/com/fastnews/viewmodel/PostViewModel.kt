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
    private val limit = 25
    private lateinit var posts: MutableLiveData<MutableList<PostData>>
//endregion

    //region GET
    fun getPosts(after: String): LiveData<MutableList<PostData>> {
        posts = MutableLiveData()

        Coroutines.ioThenMain({
            PostRepository.getPosts(after, limit)
        }) {
            posts.postValue(it.orEmpty() as MutableList<PostData>?)
        }
        return posts
    }
//endregion
}