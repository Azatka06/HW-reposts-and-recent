package com.example.HW_reposts_and_recent.model

data class PostModel(
    val id: Int = -1,
    val author: String,
    val content: String = "",
    val created: Long = System.currentTimeMillis(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val shareCount: Int = 0,
    val likedByMe: Boolean = false,
    val commentedByMe: Boolean = false,
    val sharedByMe: Boolean = false,
    val address: String? = null,
    val location: Location? = null,
    val video: Video? = null,
    val advertising: Advertising? = null,
    val source: PostModel? = null,
    val postType: PostType = PostType.SIMPLE_POST,
    val timesShown: Long = 0
)