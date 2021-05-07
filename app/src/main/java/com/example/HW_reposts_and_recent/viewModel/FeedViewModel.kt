package com.example.HW_reposts_and_recent.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HW_reposts_and_recent.errors.AuthException
import com.example.HW_reposts_and_recent.model.PostModel
import com.example.HW_reposts_and_recent.repository.PostRepository
import com.example.HW_reposts_and_recent.repository.PostRepositoryNetworkImpl
import com.example.HW_reposts_and_recent.service.PostService

import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException
//TODO SocketTimeoutException
class FeedViewModel : ViewModel() {

    private val repo: PostRepository = PostRepositoryNetworkImpl()
    private val postService = PostService()

    private val _posts: MutableLiveData<UiState> =
        MutableLiveData()
    val posts: LiveData<UiState>
        get() = _posts

    init {
       loadRecent()
    }

    fun loadData() {
        viewModelScope.launch {
            _posts.value = UiState.EmptyProgress
            tryGetAllPosts()
        }
    }

    fun updateData() {
        viewModelScope.launch {
            val curPosts = when (val posts = _posts.value) {
                is UiState.Success -> posts.posts
                else -> emptyList()
            }

            _posts.value = UiState.Update(curPosts)

            tryGetAllPosts()
        }
    }

    private suspend fun tryGetAllPosts() {
        _posts.value = try {
            UiState.Success(repo.getAll())
        } catch (e: UnresolvedAddressException) {
            UiState.InternetAccessError
        } catch (e: AuthException) {
            Log.d("MyTag", "AuthException in FeedViewModel")
            UiState.AuthError
        }
    }

//    fun likeByMe(post: PostModel) {
//        viewModelScope.launch {
//            try {
//                val likedPost = postService.likeById(post.id)
//                updatePost(likedPost)
//            } catch (e: IOException) {
//                updatePost(post)
//            } catch (e: AuthException) {
//                _posts.value = UiState.AuthError
//            }
//        }
//    }
//
//    fun dislikeByMe(post: PostModel) {
//        viewModelScope.launch {
//            try {
//                val dislikedPost = postService.dislikeById(post.id)
//                updatePost(dislikedPost)
//            } catch (e: IOException) {
//                updatePost(post)
//            } catch (e: AuthException) {
//                _posts.value = UiState.AuthError
//            }
//        }
//    }

    private fun updatePost(post: PostModel) {
        when (val state = _posts.value) {
            is UiState.Success -> {
                _posts.value = state.copy(updateList(state.posts, post))
            }
            is UiState.Update -> {
                _posts.value = state.copy(updateList(state.posts, post))
            }
            else -> return
        }
    }

    private fun updateList(oldList: List<PostModel>, newPost: PostModel): List<PostModel> =
        oldList.toMutableList().apply {
            set(indexOfFirst { it.id == newPost.id }, newPost)
        }

    fun loadMore() {
        viewModelScope.launch {
            val state = _posts.value
            if (state is UiState.Success) {
                val curPosts = state.posts.toMutableList()
                val idLastPost = curPosts.last().id
                val oldPosts = repo.getPostsCreatedBefore(idLastPost, 10)
                curPosts.addAll(oldPosts)
                _posts.value = state.copy(curPosts)
            }
        }
    }

    fun loadNew() {
        viewModelScope.launch {
            val state = _posts.value
            if (state is UiState.Success) {
                val curPosts = state.posts.toMutableList()
                val idLastPost = curPosts.first().id
                val oldPosts = repo.getPostsAfter(idLastPost)
                curPosts.addAll(0, oldPosts)
                _posts.value = state.copy(curPosts)
            }
        }
    }

    fun loadRecent() {
//        viewModelScope.launch {
//            _posts.value = UiState.EmptyProgress
//
//            _posts.value = try {
//                UiState.Success(repo.getRecentPosts(10))
//            } catch (e: UnresolvedAddressException) {
//                UiState.InternetAccessError
//            } catch (e: AuthException) {
//                Log.d("MyTag", "AuthException in FeedViewModel")
//                UiState.AuthError
//            }
//
//        }
    }
}