LOCAL_PATH := $(call my-dir)
 
include $(CLEAR_VARS)
 
# Here we give our module name and source file(s)
LOCAL_MODULE    := ndklib
LOCAL_SRC_FILES := \
                   main.c \
                   cjson.c \
                   iperf_api.c \
                   iperf_error.c \
                   iperf_client_api.c \
                   iperf_server_api.c \
                   iperf_tcp.c \
                   iperf_udp.c \
                   iperf_util.c \
                   iperf_locale.c \
                   net.c \
                   tcp_info.c \
                   tcp_window_size.c \
                   timer.c \
                   units.c \
                   iperf_sctp.c
 
include $(BUILD_SHARED_LIBRARY)