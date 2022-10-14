package com.clow.android_case

import com.blankj.utilcode.util.EncryptUtils
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by clow
 * Des:
 * Date: 2022/10/13.
 */
class EncryptTest {

    private val dataAES = "账号&123456"
    private val keyAES = "11111111111111111111111111111111"
    private val encryptData = mutableMapOf(
        "test" to "550B005048CDAC1E53FE76B831507E78",
        "google" to "550B005048CDAC1E53FE76B831507E78",
    )

    private val decryptData = mutableMapOf(
        "test" to "账号&123456",
        "google" to "账号&123456",
    )

    @Test
    fun testAes(){
        val encrypt = testAesEncrypt(dataAES, keyAES)
        println("encrypt:$encrypt")
        val decrypt = testAesDecrypt(encrypt, keyAES)
        println("decrypt:$decrypt")
        assertEquals(dataAES,decrypt)
    }

    private fun testAesEncrypt(data: String,key: String): String{
     return EncryptUtils.encryptAES2HexString(
         data.toByteArray(),
         key.toByteArray(),
            "AES/ECB/PKCS5Padding",
            null
        )
    }

    private fun testAesDecrypt(res: String,key: String): String {
        return EncryptUtils.decryptHexStringAES(
            res,
            key.toByteArray(),
            "AES/ECB/PKCS5Padding",
            null
        ).decodeToString()
    }

    @Test
    fun testAllAesDecrypt() {
        encryptData.forEach { t, u ->
            val decryptHexStringAES = EncryptUtils.decryptHexStringAES(
                u,
                keyAES.toByteArray(),
                "AES/ECB/PKCS5Padding",
                null
            )
            encryptData[t] = decryptHexStringAES.decodeToString()
        }
        println("decryptHexStringAES:${encryptData}")
    }

    @Test
    fun testAllAesEncrypt() {
        decryptData.forEach { t, u ->
            val encryptHexStringAES = EncryptUtils.encryptAES2HexString(
                u.toByteArray(),
                keyAES.toByteArray(),
                "AES/ECB/PKCS5Padding",
                null
            )
            decryptData[t] = encryptHexStringAES
        }
        println("decryptHexStringAES:${encryptData}")
    }
}