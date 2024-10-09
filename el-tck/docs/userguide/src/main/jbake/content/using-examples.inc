///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
These CLI examples can be customized as necessary.
///////////////////////////////////////////////////////////////////////

Start the Junit tests using the following command:

--
[source,oac_no_warn]
----
mvn verify
----
--


[[GCMCU]]

Example 5-1 {TechnologyShortName} TCK Signature Tests

To run the {TechnologyShortName} TCK signature tests, use the 
sigtest-maven-plugin in the TCK runner as below.

=======================================================================
<plugin>
    <groupId>jakarta.tck</groupId>
    <artifactId>sigtest-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <sigfile>path-to-sig-file-provided-with-TCK</sigfile>
        <packages>jakarta.el</packages>
        <classes>path-to-api-jar</classes>
        <report>target/sig-report.txt</report>
    </configuration>
    <executions>
        <execution>
        <id>sigtest</id>
        <goals>
            <goal>check</goal>
        </goals>
        <phase>verify</phase>
        </execution>
    </executions>
</plugin>
=======================================================================

[[GCMBV]]


Example 5-2 Single Test Directory

To run a single test directory, enter the following commands:

[source,subs="attributes"]
----
mvn verify -Dit.test={singleTestDirectoryExample}.**
----

[[GCMCA]]


Example 5-3 Subset of Test Directories

To run a subset of test directories, enter the following commands:

[source,subs="attributes"]
----
mvn verify -Dit.test={subsetTestDirectoryExample}.**
----
