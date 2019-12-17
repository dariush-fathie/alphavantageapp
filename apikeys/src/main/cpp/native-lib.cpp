//
// Created by me on 16/12/19.
//

#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_andre_apikeys_KeyStore_getApiKey(JNIEnv *pEnv, jobject instance) {
    return pEnv->NewStringUTF("FS0FJ420A520G88R");
}