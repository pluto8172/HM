package com.plout.module_login.login.bean

import java.io.Serializable

class ThirdLoginInfo(val thirdType: String, val openId: String, val nickName: String, val avatarUrl: String, val unionId: String, val gender: String) : Serializable
