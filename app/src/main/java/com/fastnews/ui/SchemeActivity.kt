package com.fastnews.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

/**
 * Scheme Activity
 */
class SchemeActivity : AppCompatActivity() {
    //region superclass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ParentActivity::class.java))
        finish()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }
//endregion
}
