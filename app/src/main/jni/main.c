
#include "com_iperf3ericsson_MainActivity.h"
#include "iperf_main.h"

JNIEXPORT jstring JNICALL Java_com_iperf3ericsson_MainActivity_callNative
  (JNIEnv *env, jobject obj){

    char** argv;
    argv[0]="iperf";
    argv[1]="-s";
    //main(2,argv);
    return (*env) ->NewStringUTF(env, argv[0]);

  }

