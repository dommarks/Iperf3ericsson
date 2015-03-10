#include "iperf_config.h"

#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
//#include <sysexits.h>
#ifdef HAVE_STDINT_H
#include <stdint.h>
#endif

#include "iperf_api.h"
//#define NULL ((void *) 0)

JNIEXPORT void JNICALL Java_vigroid_iperf3ericsson_MainActivity_runIperf(JNIEnv* env, jobject javaThis)
{

	//char *result=(char *)malloc(100);
	FILE *file = fopen("/sdcard/iPerfResult.json","w+");
    char **argv;
    int argc=3;
    char* argv0;
    char* host;
    int port;
    struct iperf_test *test;
    argv[0]="mic";
    argv[1]="iperf.scottlinux.com";
    argv[2]="5201";

    argv0 = strrchr( argv[0], '/' );
    if ( argv0 != (char*) 0 )
	++argv0;
    else
	argv0 = argv[0];

    if ( argc != 3 ) {
	fprintf( stderr, "usage: %s [host] [port]\n", argv0 );
	exit( EXIT_FAILURE );
    }
    host = argv[1];
    port = atoi( argv[2] );

    test = iperf_new_test();
    if ( test == NULL ) {
	fprintf( stderr, "%s: failed to create test\n", argv0 );
	exit( EXIT_FAILURE );
    }
    iperf_defaults( test );
    iperf_set_verbose( test, 1 );

    iperf_set_test_role( test, 'c' );
    iperf_set_test_server_hostname( test, host );
    iperf_set_test_server_port( test, port );
    //upload or download
    iperf_set_test_reverse( test, 1 );

    iperf_set_test_omit( test, 3 );
    iperf_set_test_duration( test, 5 );
    iperf_set_test_reporter_interval( test, 1 );
    iperf_set_test_stats_interval( test, 1 );
    iperf_set_test_json_output( test, 1 );

    if ( iperf_run_client( test ) < 0 ) {
	fprintf( stderr, "%s: error - %s\n", argv0, iperf_strerror( i_errno ) );
	exit( EXIT_FAILURE );
    }

    if (file != NULL)
        {
            fputs(iperf_get_test_json_output_string(test), file);
            fflush(file);
            fclose(file);
        }

    iperf_free_test( test );

   //works on 4.4.4 not 5.0
   exit( EXIT_SUCCESS );
}
