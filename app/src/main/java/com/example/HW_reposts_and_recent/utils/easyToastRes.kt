package com.example.HW_reposts_and_recent.utils

import android.app.Activity
import android.widget.Toast

fun easyToastRes(activity: Activity, massage: Int){
    Toast.makeText(activity.applicationContext, massage, Toast.LENGTH_SHORT).show()
}