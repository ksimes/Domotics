#!/bin/sh

PROCESS_ID=`ps -ef | grep "java -jar domotics.jar"  | grep -v "grep" | awk '{gsub(/^[ \t\r\n]+|[ \t\r\n]+$/, "", $2); printf $2}'`

if [ ! -z $PROCESS_ID ]
then
	kill $PROCESS_ID
	return 0
else
	echo "Domotics is NOT running!"
	return 1
fi
# nohup sudo java -jar catwatch.jar &
