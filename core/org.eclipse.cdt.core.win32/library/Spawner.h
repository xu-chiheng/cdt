/*******************************************************************************
 * Copyright (c) 2002 - 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - initial API and implementation
 *
 *  Spawner.h
 *
 *  This is a part of JNI implementation of spawner 
***********************************************************************/
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_eclipse_cdt_utils_spawner_Spawner */

#ifndef _Included_org_eclipse_cdt_utils_spawner_Spawner
#define _Included_org_eclipse_cdt_utils_spawner_Spawner
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_eclipse_cdt_utils_spawner_Spawner
 * Method:    exec0
 * Signature: ([Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_cdt_utils_spawner_Spawner_exec0
  (JNIEnv *, jobject, jobjectArray, jobjectArray, jstring, jintArray);

/*
 * Class:     org_eclipse_cdt_utils_spawner_Spawner
 * Method:    exec1
 * Signature: ([Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_cdt_utils_spawner_Spawner_exec1
  (JNIEnv *, jobject, jobjectArray, jobjectArray, jstring);

JNIEXPORT jint JNICALL Java_org_eclipse_cdt_utils_spawner_Spawner_exec2
  (JNIEnv * env, jobject process, jobjectArray cmdarray, jobjectArray envp, jstring dir, jintArray channels, jstring slaveName, jint fdm);

/*
 * Class:     org_eclipse_cdt_utils_spawner_Spawner
 * Method:    raise
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_cdt_utils_spawner_Spawner_raise
  (JNIEnv *, jobject, jint, jint);


/*
 * Class:     org_eclipse_cdt_utils_spawner_Spawner
 * Method:    waitFor
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_cdt_utils_spawner_Spawner_waitFor
  (JNIEnv *, jobject, jint);

// #define DEBUG_MONITOR

int interruptProcess(int pid);


#ifdef __cplusplus
}
#endif

// #define DEBUG_MONITOR

#endif
