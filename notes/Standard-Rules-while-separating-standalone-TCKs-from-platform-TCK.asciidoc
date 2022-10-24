The wiki attempts to create some general rules and common steps that can be referred while separating standalone TCKs out from the platform TCK repository and to convert them into JUnit/maven based tests. This is not a comprehensive document and might require further evaluation of each items before refactoring standalone TCKs.

1. It is required to understand whether server like environment or container based testing is needed for the standalone TCK and whether archives need to be deployed to the container based on the current java tests. Arquillian is currently the accepted way for creating and deploying archives that uses shrinkwrap api. 
* Jakarta REST TCK uses arquillian(creates serlvet container) for testing: The initial POC PR that was used for Jakarta REST TCK migration https://github.com/jakartaee/rest/pull/1002 , wiki https://github.com/alwin-joseph/rest/wiki/REST-TCK-separation-from-JakartaEE-TCK-repo describes the migration effort. 
* If no containers are required for testing, the JSONB TCK example can be followed. PR https://github.com/eclipse-ee4j/jsonb-api/pull/276/.

2. The tests in platform repository runs in different vehicles based on the property file `install/<tck>/other/vehicle.properties` for <tck>.
The standalone tests should adhere to the specification requirements on running in these vehicles while migrating the tests.
For instance the JAXRS tck was run run in servlet vehicle ; the arquillian uses servlet container hence the migration of tests did not require additional changes to run them in servlet vehicles.

3. `install/<tck>/bin/build.xml` is the main ant file specific to each technology for building the <tck> from jakartaee-tck repository. The java source files in list all.test.dir can be considered as the list of files with the tests. Each test method would have `@testName` tag in the comments, for JUnit these methods will need `@org.junit.jupiter.api.Test` tag correspondingly.

4. Need to implement the list of tests run for javaee full platform , webprofile, standalone and other profile runs as specified in
`src/com/sun/ts/lib/harness/keyword.properties` . Currently "KEYWORDS" parameter is used to specify the right list of tests to be run using ant. In JUnit `org.junit.jupiter.api.Tag` can be used to implement the special keywords to create test groups. The `install/<tck>/bin/ts.jtx` has the list of tests disabled, the same can be implemented using `@org.junit.jupiter.api.Disabled` Tag.

5. `release/tools/<tck>.xml` will have the list of files that is included in the legacy final tck bundle.
Choose those java/class files and the other helper files from here for the migrated tests so those will be included in the bundle post migration.

6. jakartaee-tck/lib/ has the jars used for build/run of the tcks. The same can be mentioned as dependency in maven project after migration instead of using jars from repository. The current legacy standalone TCK bundle will have the jars that is required for the tck to be run.
For eg : in Jakarta REST 
common-httpclient was used to build and run the jaxrs tck hence used it as maven dependency in Jakarta REST TCK build pom file https://github.com/jakartaee/rest/blob/6f453f85ed8aa5b1ae4b8261b9039d28cd974b04/jaxrs-tck/pom.xml#L190  and sample tck run pom file https://github.com/jakartaee/rest/blob/master/jersey-tck/pom.xml

7. userguides are present at jakartaee-tck/user_guides/<tck> : The current users guides in jakartaee-tck project are maven projects. They can be moved directly to a new repo/maven-project. It would be required to edit the userguide contents for change in details related to migration of the tck.

8. Create tck runners for implementations: Once the standalone TCK is created as an archive bundle, it is recommended to execute it against at least one implementation. The https://github.com/eclipse-ee4j/jakartaee-tck/tree/master/glassfish-runner has some of the tck runners against glassfish server.

