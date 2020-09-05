# Steps to build a Standalone TCK


## 1. Set the following environment variables
         export WORKSPACE=<local directory where Jakarta EE TCK repo is checked out>
         export ANT_HOME=<Ant Home Path>
         export JAVA_HOME=<JDK 8>
         export M2_HOME=<apache-maven home path>
         export PATH=$M2_HOME/bin:$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH
         export GF_BUNDLE_URL=/path/to/glassfish-bundle-5.1.0/ (to download the glassfish bundle to WORKSPACE(=GF_HOME by default) which is used to obtain API jars)
 
## 2. Execute $WORKSPACE/docker/build_standalonetck.sh script to build the standalone TCKs. 
    
      cd $WORKSPACE
      sh docker/build_jakartaeetck.sh <technology>

where technology can be any of the below list 

        jaxr, jsonp, jsonb, jaxrs, websocket, el, concurrency, connector, jacc, jaspic, caj, jms, jsp, jstl, jaxws, saaj, servlet, jsf, securityapi, jaxrpc, jpa, jta 

Execute "sh docker/build_jakartaeetck.sh all" to build all the standalone TCKs in Jakarta EE TCK repo




# Steps to Run a Standalone TCK

## 1. Set the following environment variables
         export WORKSPACE=<local directory where Jakarta EE TCK bundle is checked out>
         export ANT_HOME=<Ant Home Path>
         export JAVA_HOME=<JDK 8>
         export M2_HOME=<apache-maven 3.x.x>
         export PATH=$M2_HOME/bin:$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH
         export GF_BUNDLE_URL=<url to download glassfish bundle to WORKSPACE>


## 2. Execute TCK run script

This expects the directory ${WORKSPACE}/standalone-bundles/ to have standalone TCK bundle (will be available after the successful TCK build)

         cd $WORKSPACE
         sh docker/{tck}tck.sh 

where  tck can be any of the below list 

        jaxr, jsonp, jsonb, jaxrs, websocket, el, concurrency, connector, jacc, jaspic, caj, jms, jsp, jstl, jaxws, saaj, servlet, jsf, securityapi, jaxrpc, jpa, jta 


If JAXR tests need to be run, then the UDDI Registry (part of JWSDP 1.3) should be started using the cts-base docker image. The Registry server initialization in jaxrtck.sh can be commented before running this.

  #cd /opt/jwsdp-1.3/bin
  #./startup.sh

docker run --rm -dit -p <host_port>:8080 jakartaee/cts-base:0.1 bash -c "/opt/jwsdp-1.3/bin/startup.sh; cat"

eg:

docker run --rm -dit -p 8080:8080 jakartaee/cts-base:0.1 bash -c "/opt/jwsdp-1.3/bin/startup.sh; cat"

This would start JWSDP server inside a docker container and expose it on the <host_port> making the JWSDP server available at http://localhost:<host_port>(for eg: http://localhost:8080).
