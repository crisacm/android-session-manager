package com.github.crisacm.module.sessionmanager.util

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

internal class CryptHelper(context: Context) {

    private var aead: Aead

    init {
        AeadConfig.register()

        val keySetHandle: KeysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "master_keyset", "master_key_preference")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://master_key")
            .build()
            .keysetHandle

        aead = keySetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(plaintext: String, associatedData: ByteArray? = null): ByteArray {
        return aead.encrypt(plaintext.toByteArray(Charsets.UTF_8), associatedData)
    }

    fun decrypt(ciphertext: ByteArray, associatedData: ByteArray? = null): String {
        val decryptedBytes = aead.decrypt(ciphertext, associatedData)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}