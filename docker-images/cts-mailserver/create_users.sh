#!/bin/bash -x

DOMAIN_COUNT=0
USER_COUNT=0

DOMAIN_COUNT=`java -jar /root/james-cli.jar -h localhost -p 9999 listdomains  | grep james.local | wc -l`
USER_COUNT=`java -jar /root/james-cli.jar -h localhost -p 9999 listusers  | grep user01 | wc -l`

if [ $DOMAIN_COUNT -lt 1 ]; then
  echo "Domain james.local does not exist. Creating now..."
  java -jar /root/james-cli.jar -h localhost -p 9999 adddomain james.local
  echo "Domain james.local created successfully"
fi

if [ $USER_COUNT -lt 1 ]; then
  echo "User user01 does not exist. Creating now..."
  java -jar /root/james-cli.jar -h localhost -p 9999 adduser user01@james.local 1234
  echo "User user01 created successfully."
fi


