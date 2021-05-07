package com.example.HW_reposts_and_recent

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.HW_reposts_and_recent.utils.easyToastRes
import com.example.HW_reposts_and_recent.utils.isValid
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btn_confirm_reg.setOnClickListener {

            if (edit_password_reg.text.toString() == edit_password_reg_repeat.text.toString() && isValid(
                    edit_password_reg.text.toString()
                )
            ) {
                case_password_reg_repeat.error = null

                progressDialog = ProgressDialog(this@RegistrationActivity)
                progressDialog!!.setTitle(getString(R.string.titlePD))
                progressDialog!!.setMessage(getString(R.string.massagePD))
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()

                launch {
                    val response = Repository.registration(
                        edit_login_reg.text.toString(),
                        edit_password_reg.text.toString()
                    )
                    if (response.isSuccessful) {
                        if (progressDialog != null) {
                            progressDialog!!.cancel()
                        }
                        easyToastRes(this@RegistrationActivity, R.string.good_reg)
                        finish()
                    } else {
                        if (progressDialog != null) {
                            progressDialog!!.cancel()
                        }
                        case_password_reg_repeat.error = getString(R.string.registration_failed)
                    }
                }
            } else {
                if (edit_password_reg.text.toString() == edit_password_reg_repeat.text.toString()) {
                    case_password_reg_repeat.error = getString(R.string.error_password_not_same)
                } else {
                    case_password_reg_repeat.error = getString(R.string.error_password_not_same_2)
                }
            }
        }

        btn_cancel.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        if (progressDialog != null) {
            progressDialog!!.cancel()
        }
        cancel()
    }
}