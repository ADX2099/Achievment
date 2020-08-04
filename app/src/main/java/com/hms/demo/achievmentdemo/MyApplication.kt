package com.hms.demo.achievmentdemo

import android.app.Application
import android.util.Log
import com.huawei.hms.api.HuaweiMobileServicesUtil
import com.huawei.hms.jos.JosApps


class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HuaweiMobileServicesUtil.setApplication(this);
    }


}

