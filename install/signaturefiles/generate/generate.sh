#!/bin/bash -x

export SIG_OUTPUT=../../.././tools/signaturetest/src/main/java/com/sun/ts/tests/signaturetest/signature-repository

mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.annotation
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.batch 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.cdi 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.concurrency
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.data
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.ejb 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.el 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.faces 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.interceptor 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.jms 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.json 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.json.bind 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.mail 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.persistence 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.resource 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.security.jacc 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.security.enterprise 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.security.auth.message 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.servlet 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.servlet.jsp 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.servlet.jsp.jstl
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.transaction
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.validation
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.websocket 
mvn clean install -DSIG_OUTPUT=$SIG_OUTPUT -P jakarta.ws.rs 

