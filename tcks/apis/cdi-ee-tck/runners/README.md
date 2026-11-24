# CDI TCK Runners
This module contains sample maven poms for running different servers against the
CDI EE TCK.

## WildFly Runner
install WildFly using the install-wildfly profile:
`mvn -f wildfly-runner.pom -Pinstall-wildfly -Pstaging initialize`
`mvn -f wildfly-runner.pom -Pupdate-wildfly -Pstaging initialize`

The WildFly sample uses Surefire 3.6.1. It copies the TCK `txt:excludes` artifact to `target/suites/tck-excludes.txt`, scans all TCK class names, and fails if discovery selects no tests. The TCK JAR registers the CDI core method interceptor through TestNG services. The sample also enables CDI core’s optional configuration and progress logging listeners through Surefire’s `listener` property.

run the tests using the staging profile:
`mvn -f wildfly-runner.pom -Pstaging -Dincontainer test`
