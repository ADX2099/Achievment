package com.hms.demo.achievmentdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val AUTH_CODE=100
    private val AUTH_TOKEN=200
    private val TAG="LoginActivity"
    val scopes= listOf(Scope("email"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        code.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.code ->{
                val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
                    .setAuthorizationCode()
                    .setScopeList(scopes)
                    .createParams()
                val service = HuaweiIdAuthManager.getService(this, authParams)
                startActivityForResult(service.getSignInIntent(), AUTH_CODE)
            }


            else -> {}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            AUTH_CODE ->{
                val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
                if (authHuaweiIdTask.isSuccessful) {
                    //The sign-in is successful, and the user's HUAWEI ID information and authorization code are obtained.
                    val huaweiAccount = authHuaweiIdTask.result
                    Log.e(TAG,"Huawei Id  ${huaweiAccount.displayName} ${huaweiAccount.avatarUriString} ${huaweiAccount.email}")
                    Log.e(TAG, "Authorization code:" + huaweiAccount.authorizationCode)
                    //sendCredentialToServer(huaweiAccount)
                    jump(huaweiAccount)
                } else {
                    // The sign-in failed.
                    Log.e(TAG, "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode)
                }
            }

            AUTH_TOKEN ->{
                val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
                if (authHuaweiIdTask.isSuccessful) {
                    // The sign-in is successful, and the user's HUAWEI ID information and ID token are obtained.
                    val huaweiAccount = authHuaweiIdTask.result
                    Log.e(TAG,"Huawei Id  ${huaweiAccount.displayName} ${huaweiAccount.avatarUriString} ${huaweiAccount.email}")
                    Log.i(TAG, "idToken:" + huaweiAccount.idToken)
                    Log.i(TAG, "image" + huaweiAccount.avatarUri)
                    jump(huaweiAccount)
                } else {
                    // The sign-in failed. No processing is required. Logs are recorded to facilitate fault locating.
                    Log.e(TAG, "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode)
                }
            }
        }
    }

    private fun jump(huaweiAccount: AuthHuaweiId) {
        val intent= Intent(this,GameActivity::class.java)
        intent.putExtra("account",huaweiAccount)
        startActivity(intent)
        finish()
    }
}