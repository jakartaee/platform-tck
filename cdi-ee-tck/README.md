# CDI EE TCK Development


Check out the [TCK Reference Guide](https://jakartaee.github.io/cdi-tck/) to get acquainted with the CDI TCK and learn how to execute and debug it.

## Building CDI TCK artifacts
To build the CDI TCK artifacts, use:

`mvn install`

or when compiling against staged Jakarta artifacts:

`mvn -Pstaging install`

## Building the CDI TCK distribution
The CDI TCK distribution artifact is built by specifing an additional `-Drelease` property to build the TCK reference
documentation and distribution bundle, e.g.:

`mvn -Drelease install`

## Eclipse Continuous Integration Environment
The Eclipse continuous integration environment interface for the CDI project is located at https://ci.eclipse.org/cdi/
The https://github.com/jakartaee/cdi/wiki/Eclipse-CI-Release-Jobs page describes the jobs found there.

## Sources in GIT

Master branch contains the CDI TCK 4.0

### Source Layout

* dist-build - assembly project to create the distribution zip - TBD
* doc - the TCK user guide source - TBD
* src - The set of tests that depend on web and full platform containers
* README.md - this doc
