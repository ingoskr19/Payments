package co.com.test.payments.util

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter.formatIpAddress
import co.com.test.payments.BuildConfig
import com.google.gson.JsonObject
import java.net.Inet4Address
import java.net.NetworkInterface
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By oscar.vergara on 4/03/2021
 */

fun ByteArray.base64(): String {
    return Base64.getEncoder().encodeToString(this)
}

fun String.base64(): String {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

fun String.sha256(): ByteArray {
    return hashString(this, "SHA-256")
}

fun String.mask(): String {
    return "****"+ this.substring(this.length-4)
}

private fun hashString(input: String, algorithm: String): ByteArray {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
}

fun random(): String {
    var random = ""
    for (i in 1..8) {
        random += (Math.random() * 10).toInt()
    }
    return random
}

fun getIPHostAddress(): String {
    NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
        networkInterface.inetAddresses?.toList()?.find {
            !it.isLoopbackAddress && it is Inet4Address
        }?.let { return it.hostAddress }
    }
    return ""
}

fun createAuth(): JsonObject {
    var tranKey = BuildConfig.TRAN_KEY
    val login = BuildConfig.LOGIN
    val nonce = random()
    val seed = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(Calendar.getInstance().time)

    val json = JsonObject()
    tranKey = nonce+seed+tranKey

    json.addProperty("login",login)
    json.addProperty("tranKey",tranKey.sha256().base64())
    json.addProperty("nonce",nonce.base64())
    json.addProperty("seed",seed)

    return json
}