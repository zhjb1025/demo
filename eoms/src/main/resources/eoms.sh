#!/bin/sh
PROG_NAME=$0
ACTION=$1
usage() {
    echo "Usage: $PROG_NAME {start|stop}"
    exit 1;
}

if [ $# -lt 1 ]; then
    usage
fi
##########################################################################################
FUNSERVER_GC_LOG="/root/logs/eoms/gc.log"
FUNSERVER_HOME="/root/app"
FUNSERVER_LOG="/root/logs/eoms/nohup.out"
FUNSERVER_JAR="eoms.jar"
##########################################################################################


start()
{

	##########################################################################################
	#JAVA_START_OPT="$JAVA_START_OPT -server -d64 -Xms4096m -Xmx5120m -XX:PermSize=64M -XX:MaxPermSize=512M -Xmn2730m -Xss2048k"
	JAVA_START_OPT="$JAVA_START_OPT -XX:+PrintHeapAtGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+HeapDumpOnOutOfMemoryError -Xloggc:$FUNSERVER_GC_LOG"							
	
	##########################################################################################

	echo "Starting  ..."
	if [ -f "$FUNSERVER_GC_LOG" ]; then  
		mv -f $FUNSERVER_GC_LOG "$FUNSERVER_GC_LOG.`date '+%Y%m%d%H%M%S'`"
	fi 
	cd $FUNSERVER_HOME
	nohup java  $JAVA_START_OPT  -jar $FUNSERVER_JAR --spring.profiles.active=prd  >$FUNSERVER_LOG 2>&1 &
	tail -f $FUNSERVER_LOG
}

stop()
{
	echo "Stopping ..."
	curl  -X POST http://127.0.0.1:9091/manage/shutdown
	tail -f $FUNSERVER_LOG
}
case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    *)
        usage
    ;;
esac

