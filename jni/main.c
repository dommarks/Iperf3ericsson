#include "iperf_config.h"

#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#ifdef HAVE_STDINT_H
#include <stdint.h>
#endif

#include "iperf_api.h"

jstring  Java_vigroid_iperf3ericsson_MainActivity_runIperf(JNIEnv* env, jobject x, jstring hostName, jint portNum)
{

	char *serverHostname = (*env)->GetStringUTFChars(env, hostName, 0);
	//TODO change the file name depends on timestamp
	FILE *file = fopen("/sdcard/iPerfResult.json","w+");
	struct iperf_test *test;
	test = iperf_new_test();
	if ( test == NULL ) {
		//TODO !Localization
		return (*env)->NewStringUTF(env, "Fail to create a test! Please check your memory usage!");
	    }
	iperf_defaults( test );
	iperf_set_verbose( test, 0 );

	iperf_set_test_role( test, 'c' );
	//TODO Can be changed by setting
	iperf_set_test_server_hostname( test, serverHostname);
	//iperf_set_test_server_hostname( test, "coneye.myqnapcloud.com" );

	iperf_set_test_server_port( test, portNum );

	//iperf_set_test_server_port( test, 5201 );
	//upload or download
    iperf_set_test_reverse( test, 1 );

    iperf_set_test_omit( test, 0 );
    iperf_set_test_duration( test, 7 );
    iperf_set_test_reporter_interval( test, 1 );
    iperf_set_test_stats_interval( test, 1 );
    iperf_set_test_json_output( test, 1 );

    if ( iperf_run_client( test ) < 0 ) {
    	//TODO !Localization
    	return (*env)->NewStringUTF(env, "Please check your network and try again. Thanks!");
    }

    if (file != NULL)
    {
         fputs(iperf_get_test_json_output_string(test), file);
         fflush(file);
         fclose(file);
    }

    else
    	//TODO !Localization
    	return (*env)->NewStringUTF(env, "Fail to write logs into SDCard. Please check you SDcard or change SDcard settings");

    iperf_free_test( test );


	return (*env)->NewStringUTF(env, "Test Finished Successfully!");
}
