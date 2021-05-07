package com.example.HW_reposts_and_recent.repository


import com.example.HW_reposts_and_recent.api.Token
import com.example.HW_reposts_and_recent.model.PostModel
import com.example.HW_reposts_and_recent.model.User
import retrofit2.Response

interface PostRepository {
    suspend fun getAll(): List<PostModel>
    suspend fun getById(id: Int): PostModel?
    suspend fun save(item: PostModel): PostModel
    suspend fun deleteById(id: Int)
    fun createRetrofitWithAuth(token: String, userAuth: User)
    suspend fun authenticate(login: String, password: String): Token
    suspend fun register(login: String, password: String): Response<Token>
    suspend fun getUserAuth(): User
    suspend fun repost(repostedId: Int, content: String): PostModel
    suspend fun getPostsCreatedBefore(idCurPost: Int, countUploadedPosts: Int): List<PostModel>
    suspend fun getPostsAfter(idFirstPost: Int): List<PostModel>
    suspend fun getRecentPosts(countPosts: Int): List<PostModel>
}