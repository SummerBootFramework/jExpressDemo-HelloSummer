#!/usr/bin/bash
export _JAVA_OPTIONS="-Xms4g -Xmx4g"
JMETER_HOME=/apps/apache-jmeter-5.5
JMETER_PRJ_FILE=$1
REPORT_NAME=$1_$2_$4users_in$5s_eachloop$6delay$7ms

if [ -d ./reports/$REPORT_NAME ];then 
	rm -fr ./reports/$REPORT_NAME
fi

if [ -f ./reports/$REPORT_NAME.log ];then 
	rm ./reports/$REPORT_NAME.log
fi

#"$JMETER_HOME/bin/jmeter.sh" -n -t $1 -l "reports/$REPORT_NAME.log" -e -o "reports/$REPORT_NAME" -Jserver=$2 -Jport=$3 -Jusers=$4 -JrampUp=$5 -Jloop=$6 -Jdelay=$7
"$JMETER_HOME/bin/jmeter.sh" -n -t $1 -Jserver=$2 -Jport=$3 -Jusers=$4 -JrampUp=$5 -Jloop=$6 -Jdelay=$7 -l "reports/$REPORT_NAME.log" -e -o "reports/$REPORT_NAME" 