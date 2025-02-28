java -server -Xms1G -Xmx1G -XX:+UseG1GC -XX:+AlwaysPreTouch -Djava.awt.headless=true -Djna.nosys=true -Dfile.encoding=UTF-8 -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dopenssl -Dio.netty.leakDetectionLevel=advanced -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dio.netty.native.workdir=temp -jar hellosummer-2.0.jar -domain dev -use hawaii_1 RoleBased
pause

rem java -jar hellosummer-2.0.jar -cfgdir standalone_dev/configuration_two-way_8424