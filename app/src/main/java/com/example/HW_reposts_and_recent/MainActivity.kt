package com.example.HW_reposts_and_recent

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.HW_reposts_and_recent.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isAuthenticated()) {
            startFeedActivity()
        } else {

            btn_login.setOnClickListener {
                case_password.error = null

                val progressDialog = ProgressDialog(this@MainActivity).apply {
                    setTitle(getString(R.string.titlePD))
                    setMessage(getString(R.string.massagePD))
                    setCancelable(false)
                    show()
                }

                lifecycleScope.launch {
                    val response = runCatching {
                        Repository.authenticate(
                            edit_login.text.toString(),
                            edit_password.text.toString()
                        )
                    }
                    progressDialog.dismiss()
                    response.getOrNull()?.body()?.let {
                        easyToastRes(this@MainActivity, R.string.success)
                        setUserAuth(it.token)

                        startFeedActivity()
                    } ?: run {
                        case_password.error = getString(R.string.authentication_failed)
                    }
                }
            }

            btn_registration.setOnClickListener {
                val regIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
                startActivity(regIntent)
            }
        }
    }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .apply()

    private fun startFeedActivity() {
        val feedIntent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(feedIntent)
        finish()
    }
}

