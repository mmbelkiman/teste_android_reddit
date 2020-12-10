package com.fastnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

/**
 * Parent Activity
 */
class ParentActivity : AppCompatActivity() {
    //region superclass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.fastnews.R.layout.activity_parent)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }
//endregion
}
