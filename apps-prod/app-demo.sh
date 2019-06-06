#!/usr/bin/env bash
# 启动|停止 app-demo.jar 文件；  chmod +x app-demo.sh
# 注意：在sh文件中=赋值，左右两侧不能有空格

# 应用名称
APP_NAME=$2
echo $APP_NAME
#启动参数
START_OPTS=$3
echo $START_OPTS

#JVM参数
# JVM_OPTS="-Dname=$SpringBoot  -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
JVM_OPTS=""
APP_HOME=~/git-repository/common-mvc-parent/apps-prod
LOG_PATH=$APP_HOME/logs/$APP_NAME.log
echo $APP_NAME
echo $LOG_PATH

if [ "$1" = "" ];
then
    echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
    exit 1
fi

if [ "$APP_NAME" = "" ];
then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

function start()
{
    count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
    if [ $count != 0 ];then
        echo "$APP_NAME is running..."
    else
        echo "Start $APP_NAME success..."
        nohup java -jar  $JVM_OPTS $APP_NAME  $START_OPTS > /dev/null 2>&1 &
    fi
}

function stop()
{
    echo "Stop $APP_NAME"
    boot_id=`ps -ef |grep java|grep $APP_NAME|grep -v grep|awk '{print $2}'`
    count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`

    if [ $count != 0 ];then
        kill $boot_id
        count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`

        boot_id=`ps -ef |grep java|grep $APP_NAME|grep -v grep|awk '{print $2}'`
        kill -9 $boot_id
    fi
}

function restart()
{
    stop
    sleep 2
    start
}

function status()
{
    count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
    if [ $count != 0 ];then
        echo "$APP_NAME is running..."
    else
        echo "$APP_NAME is not running..."
    fi
}

case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    *)

    echo -e "\033[0;31m Usage: \033[0m  \033[0;34m sh  $0  {start|stop|restart|status}  {SpringBootJarName} \033[0m
\033[0;31m Example: \033[0m
      \033[0;33m sh  $0  start esmart-test.jar \033[0m"
esac
