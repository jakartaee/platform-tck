# Build instructions - JakartaEE TCK Bundle
## System Requirements
- JDK 8u191+
- Apache Ant 1.10.5+
- JTHarness 5.0 (Included in the repository)

## Steps to build the bundle
1. Clone the [https://github.com/eclipse-ee4j/jakartaee-tck](jakartaee-tck) repository to your local directory.
2. Edit the properties in jakartaee-tck/install/jakartaee/bin/ts.jte. The default values in ts.jte file would work with the latest released version of the GlassFish build. If you are compiling it against a different JavaEE 8 compliant server, you need to adjust the below properties accordingly.
```
javaee.home=
javaee.home.ri=
endorsed.dirs=
endorsed.dirs.ri=
implementation.classes.ri=
implementation.classes=
sigTestClasspath=
```

3. Set the following environment variables
```
     export WORKSPACE=/cloned/directory/for/jakartaee-tck
     export GF_BUNDLE_URL=https://ci.eclipse.org/jakartaee-tck/job/build-glassfish/lastSuccessfulBuild/artifact/appserver/distributions/glassfish/target/glassfish.zip
     export ANT_HOME=/path/to/apache-ant
     export JAVA_HOME=/path/to/jdk8
     export PATH=$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH
```

Optional:

```
     export GF_HOME=/path/to/install/glassfish
```

_Note that GF_HOME is a to be set to the *parent* folder of GlassFish bundle; a folder containing the "glassfish6" folder, which is the hardcoded name_

_If GF_HOME is not set it defaults to BASEDIR, where the GlassFish downloaded from GF_BUNDLE_URL is installed. ANT_HOME defaults to /usr/share/ant/ and JAVA_HOME to /opt/jdk1.8.0_171_

4. Run the wrapper build script
```
$WORKSPACE/docker/build_jakartaeetck.sh
```

_Note the script sets paths for javaee.home and javaee.home.ri in jakartaee-tck/install/jakartaee/bin/ts.jte based on GF_HOME, while report.dir and work.dir are set to paths based on BASEDIR_

5. If the build is successful, the Jakarta EE TCK bundle would be available at the below location.
`$WORKSPACE/release/JAKARTAEE_BUILD/latest/jakartaeetck-*<date>.zip`

# Run instructions - JakartaEE TCK Bundle
## System Requirements
- JDK 8u191+
- Apache Ant 1.10.5+
- JTHarness 5.0 (Included in the repository)
- Java Mail Server
- JWSDP 1.3 (Required by JAXR test suite, which uses UDDI registry)
- LDAP Server (Required by Security API test suite, unbound-ldapsdk 4.0 is used by default)

1. Extract the JakartaEE TCK bundle to a local directory.
2. Set the following environment variables
```
     export CTS_HOME=/directory/where/jakartaeetck/bundle/was/extracted
     export GF_BUNDLE_URL=https://ci.eclipse.org/jakartaee-tck/job/build-glassfish/lastSuccessfulBuild/artifact/appserver/distributions/glassfish/target/glassfish.zip
     export PROFILE=full
     export ANT_HOME=/path/to/apache-ant
     export JAVA_HOME=/path/to/jdk8
     export PATH=$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH
     export LANG="en_US.UTF-8"
```
3. If JAXR tests need to be run, then the UDDI Registry (part of JWSDP 1.3) should be started using the cts-base docker image. 
```
docker run --rm -dit -p <host_port>:8080 jakartaee/cts-base:0.1 bash -c "/opt/jwsdp-1.3/bin/startup.sh; cat"
```
eg:
```
docker run --rm -dit -p 8080:8080 jakartaee/cts-base:0.1 bash -c "/opt/jwsdp-1.3/bin/startup.sh; cat"
```
This would start JWSDP server inside a docker container and expose it on the <host_port> making the JWSDP server available at `http://localhost:<host_port>`(for eg: `http://localhost:8080`)
Set the environment variable for UDDI Registry URL as follows:
```
export UDDI_REGISTRY_URL="http://localhost:<host_port>/RegistryServer/
```

4. If Javamail server related tests(these tests are under suites - javamail, samples, servlet) need to be run, and no mail server is available for testing, then the mail server can be started using the cts-mailserver docker image (it internally uses Apache James Mailserver) as follows:
```
docker run --rm -it -p 1025:25 -p 1143:143 jakartaee/cts-mailserver:0.1 bash -c "/root/startup.sh"
```
This would start the mail server and expose it through ports 1025 and 1143. The mail user name is "user01@james.local". 
set the Environment variable `MAIL_USER` to `user01@james.local`

The following environment variables are available can be used to override Mail server related default configurations used by the run script. If you are using a custom mail server, these default values should be overridden.
```
export MAIL_HOST=<hostname> # localhost by default
export MAIL_USER=<mail_user> # user01@james.local by default
export MAIL_FROM=<mail_user>@domain.com # user01@james.local by default
export MAIL_PASSWORD=<mail_password> # 1234 by default
export SMTP_PORT=<listen_port_of_smtp_server> # 1025 by default
export IMAP_PORT=<listen_port_of_imap_server> # 1143 by default
```

5. Run the wrapper script for running a specific test suite in the JakartaEE TCK bundle.
```
$WORKSPACE/docker/run_jakartaeetck.sh suite_name1
$WORKSPACE/docker/run_jakartaeetck.sh suite_name1_<vehicle-name>
$WORKSPACE/docker/run_jakartaeetck.sh suite_name1/sub-suite
eg:
$WORKSPACE/docker/run_jakartaeetck.sh jms
$WORKSPACE/docker/run_jakartaeetck.sh jms_servlet
$WORKSPACE/docker/run_jakartaeetck.sh jms/ee 
```
6. After a successful run, the test reports would be available at `${CTS_HOME}/jakartaeetck-report` and the detailed test logs would be available at `${CTS_HOME}/jakartaeetck-work`

7. The test logs, JUnit reports, GlassFish domain configuration and logs generated during the test run gets stored in ${WORKSPACE}/<suite_name>-results.tar.gz. These files can be used for debugging if there are any test failures.