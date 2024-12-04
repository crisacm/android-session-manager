package com.github.crisacm.sessionmanager.util.crypto

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

class CryptoHelperImp(
  context: Context
) : CryptHelper {
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

  override fun encrypt(plaintext: String, associatedData: ByteArray?): ByteArray {
    return aead.encrypt(plaintext.toByteArray(Charsets.UTF_8), associatedData)
  }

  override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray?): String {
    val decryptedBytes = aead.decrypt(ciphertext, associatedData)
    return String(decryptedBytes, Charsets.UTF_8)
  }
}