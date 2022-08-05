#!/bin/bash

REPOSITORY=/home/app
echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fla java | awk '{print $1}')
echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
fi
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME
echo "> $JAR_NAME 실행"
nohup /opt/jdk-17/bin/java -javaagent:/home/app/dd-java-agent.jar -Dspring.profiles.active=devserver -Ddd.logs.injection=true -Ddd.service=my-app -Ddd.env=staging -jar $JAR_NAME 1>$REPOSITORY/lof.log 2>&1 &