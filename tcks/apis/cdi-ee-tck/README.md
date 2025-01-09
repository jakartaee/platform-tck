# CDI EE TCK Development


The distribution contains a user guide to get acquainted with the CDI EE TCK and learn how to execute and debug it.

## Building CDI TCK artifacts
To build the CDI TCK artifacts, use:

`mvn install`

or when compiling against staged Jakarta artifacts:

`mvn -Pstaging install`

## Building the CDI EE TCK distribution
The CDI EE TCK distribution artifact is built by specifing an additional `-Drelease` property to build the TCK reference documentation and distribution bundle, e.g.:

`mvn -Drelease install`

## Eclipse Continuous Integration Environment
The Eclipse continuous integration environment interface for the CDI EE TCK project is located at https://ci.eclipse.org/plaform-tck. The https://github.com/jakartaee/platform-tck/wiki/CI_Job_Notes page describes the jobs found there.

## Sources in GIT

The main branch contains the CDI EE TCK for the Jakarta EE 11 release.

### Source Layout

* doc - the TCK user guide source
* runners - example runners for the TCK
* tck - The set of tests that depend on web and full platform containers
* tck-dist - assembly project to create the distribution zip
* README.md - this doc
