#PATH=/usr/lib/jvm/java21/bin/:$PATH
JAVA_PATH=$(find /usr/lib/jvm -name "java21*" -type d | head -1)
echo "${JAVA_PATH}"
${JAVA_PATH}/bin/java \
 -Djava.awt.headless=true \
 -Xms2G -Xmx2G \
 -XX:+UseZGC -XX:ZUncommitDelay=300 -XX:+ZGenerational -XX:+AlwaysPreTouch \
 -XX:+PerfDisableSharedMem \
 -XX:+ZUncommit \
 -XX:+DisableExplicitGC \
 -XX:MaxDirectMemorySize=1g \
 -XX:+HeapDumpOnOutOfMemoryError \
 -XX:HeapDumpPath=log/heapdump.hprof \
 -XX:+ExitOnOutOfMemoryError \
 -Xlog:gc*:file=log/gc.log:time,level,tags:filecount=5,filesize=10M \
 -Dfile.encoding=UTF-8 \
 -Duser.timezone=America/Toronto \
 -Djava.security.egd=file:/dev/./urandom \
 -Dio.netty.handler.ssl.openssl.engine.enable=true \
 -Dio.netty.leakDetectionLevel=SIMPLE \
 -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
 -jar MockService-1.1.0.jar