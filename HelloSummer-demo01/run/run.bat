md env_dev env_dev\config

java -Dopenssl -Dio.netty.leakDetectionLevel=advanced -Dfile.encoding=UTF-8 -server -Xms1G -Xmx1G -XX:+UseG1GC -Djava.awt.headless=true -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -XX:+AlwaysPreTouch -Djna.nosys=true  -jar hellosummer-1.0.jar -cfgdir env_dev/config
pause