#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $DISCOVERY_SERVICE_PORT)"
echo "********************************************************"
while ! `nc -z discovery-service  $(getPort $DISCOVERY_SERVICE_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

#echo "********************************************************"
#echo "Waiting for the configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
#echo "********************************************************"
#while ! `nc -z configserver $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
#echo "*******  Configuration Server has started"

#echo "********************************************************"
#echo "Starting Zuul Service with $CONFIGSERVER_URI"
echo "******************************************************** @project.build.finalName@" 
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$DISCOVERY_SERVICE_URI   \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /usr/local/target/@project.build.finalName@.jar