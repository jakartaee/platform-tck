* The bom directory contains the Maven BOM for the TCK runners in this directory. It is
referened in the runner pom using a dependencyManagement section like this:
```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>jakarta.tck</groupId>
                <artifactId>glassfish-runner-bom</artifactId>
                <version>${glassfish.runner.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
You can update the bom locally and install it, or run the https://ci.eclipse.org/jakartaee-tck/job/11/job/stage-artifacts/job/glassfish-runner-bom in the CI environment to stage the bom. Pass in -Dglassfish.runner.version=X to the job to get the version of dependenies you wish to run with.
* Directories containing the keyword `extra` are runners for tests
  that must be run in addition to that component tck.
  
   As of 2025-03-12, this is:
   
   - cdi-platform-extra-tck
   - jsonb-platform-extra-tck
   - jsonp-platform-extra-tck
   - pages-platform-extra-tck
   - rest-platform-extra-tck
   - websocket-platform-extra-tck

* Directories containing the keyword `subst` are runners for tests
  where the component TCK completely replaces the component tck. In
  other words, the component TCK can be completely ignored.

   As of 2025-03-12, this is:
   
   - expression-language-platform-subst-tck

* Directories that do not contain either of the preceding keywords
  contain runners for tests where component TCK is the necessary and
  sufficient set of tests. These tests are produced and published by
  the respective component specification project.

   As of 2025-03-12, this is:
   
   - annotations-tck
   - batch-tck
   - cdi-model-tck
   - cdi-tck
   - concurrency-tck
   - connector-platform-tck
   - data-tck
   - enterprise-beans-tck
   - expression-language-tck
   - jsonb-tck
   - jsonp-tck
   - mail-platform-tck
   - messaging-platform-tck
   - messaging-tck
   - pages-tck
   - persistence-platform-tck
   - persistence-tck
   - platform
   - rest-tck
   - servlet-tck
   - signature
   - tags-tck
   - transactions-tck
   - validation-tck
   - websocket-tck
