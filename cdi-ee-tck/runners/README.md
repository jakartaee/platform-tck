# CDI TCK Runners
This module contains sample maven poms for running different servers against the
CDI EE TCK.

## WildFly Runner
install WildFly using the install-wildfly profile:
`mvn -f wildfly-runner.pom -Pinstall-wildfly -Pstaging initialize`
`mvn -f wildfly-runner.pom -Pupdate-wildfly -Pstaging initialize`

run the tests using the staging profile:
`mvn -f wildfly-runner.pom -Pstaging -Dincontainer test`

