start cmd /k "cd E:\work\demo\zookeeper-starter && mvn clean  -Dmaven.test.skip=true  package install && cd E:\work\demo\ehcache-redis-starter && mvn clean  -Dmaven.test.skip=true  package install && cd E:\work\demo\demo-framework && mvn clean  -Dmaven.test.skip=true  package install && cd E:\work\demo\config-center-client && mvn clean  -Dmaven.test.skip=true  package install && cd E:\work\demo\config-center && mvn clean  -Dmaven.test.skip=true  package spring-boot:repackage && cd E:\work\demo\gateway && mvn clean  -Dmaven.test.skip=true  package spring-boot:repackage && cd E:\work\demo\spring-boot-demo && mvn clean  -Dmaven.test.skip=true  package spring-boot:repackage"
