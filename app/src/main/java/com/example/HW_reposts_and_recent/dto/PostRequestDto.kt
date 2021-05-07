package com.example.HW_reposts_and_recent.dto

import com.example.HW_reposts_and_recent.model.Advertising
import com.example.HW_reposts_and_recent.model.PostModel
import com.example.HW_reposts_and_recent.model.Video
import com.example.HW_reposts_and_recent.model.Location
import com.example.HW_reposts_and_recent.model.PostType

data class PostRequestDto(
    val id: Int,
    val author: String,
    val content: String,
    val created: Long,
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
) {
    companion object{
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            likesCount = model.likesCount,
            commentsCount = model.commentsCount,
            shareCount = model.shareCount,
            likedByMe = model.likedByMe,
            commentedByMe = model.commentedByMe,
            sharedByMe = model.sharedByMe,
            address = model.address,
            location = model.location,
            video = model.video,
            advertising = model.advertising
        )

        fun toModel(dto: PostRequestDto) = PostModel(
            id = dto.id,
            author = dto.author,
            content = dto.content,
            created = dto.created,
            likesCount = dto.likesCount,
            commentsCount = dto.commentsCount,
            shareCount = dto.shareCount,
            likedByMe = dto.likedByMe,
            commentedByMe = dto.commentedByMe,
            sharedByMe = dto.sharedByMe,
            address = dto.address,
            location = dto.location,
            video = dto.video,
            advertising = dto.advertising,
            source = dto.source,
            postType = dto.postType,
            timesShown = dto.timesShown
        )
    }
}
