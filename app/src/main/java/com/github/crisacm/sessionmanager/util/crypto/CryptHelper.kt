package com.github.crisacm.sessionmanager.util.crypto

interface CryptHelper {

  fun encrypt(plaintext: String, associatedData: ByteArray? = null): ByteArray

  fun decrypt(ciphertext: ByteArray, associatedData: ByteArray? = null): String
}