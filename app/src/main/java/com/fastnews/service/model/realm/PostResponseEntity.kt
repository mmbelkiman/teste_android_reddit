package com.fastnews.service.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class PostResponseEntity(
    @PrimaryKey var id: String = "",
    var author: String = "",
    var thumbnail: String = "",
    var name: String = "",
    var num_comments: Int = 0,
    var score: Int = 0,
    var title: String = "",
    var created_utc: Long = 0,
    var url: String = "",
    var date: Date = Date()
) : RealmObject()