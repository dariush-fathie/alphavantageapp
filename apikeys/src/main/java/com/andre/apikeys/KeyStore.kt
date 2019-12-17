package com.andre.apikeys

object KeyStore {

    external fun getApiKey(): String

    init {
        System.loadLibrary("native-lib")
    }
}