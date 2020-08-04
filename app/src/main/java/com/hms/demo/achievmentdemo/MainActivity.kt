package com.hms.demo.achievmentdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val handler= Handler()
        handler.postDelayed({
            val scopes= listOf(Scope("email"))
            val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
                .setScopeList(scopes)
                .createParams()
            val service = HuaweiIdAuthManager.getService(this, authParams)
            val task = service.silentSignIn()
            task.addOnSuccessListener {
                val intent= Intent(this,GameActivity::class.java)
                intent.putExtra("account",it)
                startActivity(intent)
                finish()
            }
            task.addOnFailureListener{
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
        },1000)
    }
}