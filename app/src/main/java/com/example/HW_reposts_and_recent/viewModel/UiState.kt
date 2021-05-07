package com.example.HW_reposts_and_recent.viewModel

import com.example.HW_reposts_and_recent.model.PostModel


sealed class UiState {

    data class Update(val posts: List<PostModel>) : UiState()

    object EmptyProgress : UiState()

    object InternetAccessError : UiState()

    object AuthError : UiState()

    data class Success(val posts: List<PostModel>) : UiState()
}