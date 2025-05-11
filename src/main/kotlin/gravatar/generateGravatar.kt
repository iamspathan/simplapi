package com.iamspathan.gravatar

import java.math.BigInteger
import java.security.MessageDigest

object GravatarUtil {
    fun generateGravatarUrl(email: String, size: Int = 200, default: String = "identicon"): String {
        val emailHash = md5Hash(email.trim().lowercase())
        return "https://www.gravatar.com/avatar/$emailHash?s=$size&d=$default"
    }

    private fun md5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}