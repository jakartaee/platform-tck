## System Requirements
- JDK 8u191+
- Apache Ant 1.10.5+

## Steps to generate the coverage report


1. Set the following environment variables
```
      export TS_TOOLS_HOME=/cloned/directory/for/jakartaee-tck-tools
      export TS_HOME=/cloned/directory/for/jakartaee-tck
      export ANT_HOME=/path/to/apache-ant
      export JAVA_HOME=/path/to/jdk8
      export PATH=$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH
```
4. Run the coverage script
```
cd $TS_HOME;
ant -buildfile coverage-build.xml <target>
````

`<target>` can be 'all', or any tcks in 
[j2eetools-mgt, j2eetools-deploy,jta,jdbc,ejb,connector,jaspic,jsp,el,servlet,
jms,jacc,javaee,jstl,jpa,jaxrpc,jaxrs,webservices,jsf,jsonp,jsonb,concurrency,
websocket,rmiiiop,securityapi]

5. If the run is successful, the coverage report would be available at the below location.
`$TS_HOME/tmp/assertion-coverage`