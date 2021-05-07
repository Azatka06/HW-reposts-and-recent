package com.example.HW_reposts_and_recent

import android.app.Application
import com.example.HW_reposts_and_recent.repository.PostRepository
import com.example.HW_reposts_and_recent.repository.PostRepositoryNetworkImpl

class App : Application() {


    companion object {
        lateinit var postRepository: PostRepository
    }

    override fun onCreate() {
        super.onCreate()
        postRepository = PostRepositoryNetworkImpl()

    }

}
