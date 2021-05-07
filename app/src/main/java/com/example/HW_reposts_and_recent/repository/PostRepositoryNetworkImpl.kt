package com.example.HW_reposts_and_recent.repository


import com.example.HW_reposts_and_recent.R
import com.example.HW_reposts_and_recent.api.*
import com.example.HW_reposts_and_recent.dto.PostRequestDto
import com.example.HW_reposts_and_recent.dto.PostResponseDto
import com.example.HW_reposts_and_recent.dto.PostsCreatedBeforeRequestDto
import com.example.HW_reposts_and_recent.dto.RepostRequestDto
import com.example.HW_reposts_and_recent.errors.AuthException
import com.example.HW_reposts_and_recent.errors.PostNotFoundException
import com.example.HW_reposts_and_recent.model.PostModel
import com.example.HW_reposts_and_recent.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val serverUrl = "https://netology-back-end-post-hw-8.herokuapp.com"

class PostRepositoryNetworkImpl : PostRepository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private var api: API = retrofit.create(API::class.java)

    private lateinit var userAuth: User

    override suspend fun getUserAuth(): User = userAuth

    override suspend fun authenticate(login: String, password: String): Token {
        val response = api.authenticate(
            AuthRequestParams(login, password)
        )

        val token = requireNotNull(response.body())
        userAuth = User(login)

        if (response.isSuccessful) {
            createRetrofitWithAuth(
                token.token,
                userAuth
            )
        } else {
            if (response.code() == 401) {
                throw AuthException()
            }
        }

        return token
    }

    override suspend fun register(login: String, password: String) =
        api.register(
            RegistrationRequestParams(login, password)
        )

    override fun createRetrofitWithAuth(token: String, userAuth: User) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken = token))
            .addInterceptor(httpLoggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(API::class.java)

        this.userAuth = userAuth
    }

    override suspend fun getAll(): List<PostModel> {
        val response = api.getAllPosts()

        if (response.isSuccessful) {
            val postResponseDtoList: List<PostResponseDto> = requireNotNull(response.body())
            return postResponseDtoList.map(PostResponseDto.Companion::toModel)
        } else throw Exception("Penises")
    }

    override suspend fun getById(id: Int): PostModel {
        val response = api.getPostById(id)

        if (response.isSuccessful) {
            val postResponseDto = response.body() ?: throw PostNotFoundException()
            return PostResponseDto.toModel(postResponseDto)
        } else {
            throw Exception(R.string.error_take_post.toString())
        }
    }

    override suspend fun deleteById(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun save(model: PostModel): PostModel {
        val response = api.createPost(
            PostRequestDto.fromModel(model)
        )

        if (response.isSuccessful) {
            return PostResponseDto.toModel(requireNotNull(response.body()))
        } else {
            throw Exception(R.string.error_create_post.toString())
        }
    }

    override suspend fun repost(repostedId: Int, content: String): PostModel {
        val repostRequestDto = RepostRequestDto(content)
        val response = api.repost(repostedId, repostRequestDto)
        val postResponseDto = requireNotNull(response.body())
        return PostResponseDto.toModel(postResponseDto)
    }

    override suspend fun getPostsCreatedBefore(idCurPost: Int, countUploadedPosts: Int): List<PostModel> {
        val requestData = PostsCreatedBeforeRequestDto(idCurPost, countUploadedPosts)
        val response = api.getPostsCreatedBefore(requestData)
        val postResponseDtoList = requireNotNull(response.body())
        return postResponseDtoList.map(PostResponseDto.Companion::toModel)
    }

    override suspend fun getPostsAfter(idFirstPost: Int): List<PostModel> {
        val response = api.getPostsAfter(idFirstPost)
        val postResponseDtoList = requireNotNull(response.body())
        return postResponseDtoList.map(PostResponseDto.Companion::toModel)
    }

    override suspend fun getRecentPosts(countPosts: Int): List<PostModel> {
        val response = api.getRecentPosts(countPosts)
        val postResponseDtoList = requireNotNull(response.body())
        return postResponseDtoList.map(PostResponseDto.Companion::toModel)
    }
}