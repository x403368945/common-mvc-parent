#!/usr/bin/env bash
# 启动|停止 app-demo.jar 文件；  chmod +x app-demo.sh
# 注意：在sh文件中=赋值，左右两侧不能有空格

# 应用运行目录
APP_HOME=~/git-repository/common-mvc-parent/apps-prod
# 应用名称(jar包名称)：{APP_NAME}.jar
APP_NAME=app-demo
# 启动端口
SERVER_PORT=3366
# LOG_PATH=$APP_HOME/logs/$APP_NAME.log
#JVM参数
# JVM_OPTS="-Dname=$SpringBoot  -Duser.timezone=Asia/Shanghai -Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
JVM_OPTS="-Dapp.name=$APP_NAME"
#启动参数
# SPRING_OPTS="--spring.profiles.active=prod"
SPRING_OPTS="--server.port=$SERVER_PORT"
echo $APP_HOME
echo $APP_NAME

if [ "$1" = "" ];
then
    echo -e "未输入操作名 {start|stop|restart|status} "
    exit 1
fi

function start()
{
    count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
    if [ $count != 0 ];then
        echo "$APP_NAME is running..."
    else
        echo "Start $APP_NAME success..."
        nohup java -jar $JVM_OPTS $APP_NAME.jar $SPRING_OPTS > /dev/null 2>&1 &
    fi
    echo "tail -f $APP_HOME/logs/$APP_NAME."
}

function stop()
{
    echo "Stop $APP_NAME"
    echo `ps -ef |grep java|grep $APP_NAME|grep -v grep`
    echo `ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`
    boot_id=`ps -ef |grep java|grep $APP_NAME|grep -v grep`
    count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`

    if [ $count != 0 ];then
        kill $boot_id
        count=`ps -ef |grep java|grep $APP_NAME|grep -v grep|wc -l`

        boot_id=`ps -ef |grep java|grep $APP_NAME|grep -v grep`
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

    echo -e "Usage: sh $APP_NAME.sh {start|stop|restart|status} \nExample: \nsh $APP_NAME.sh start"

esac
