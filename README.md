# Platform TCK

## Building

From the root folder, try: 
```
mvn clean install -Dmaven.compiler.failOnError=false > /tmp/build.txt
```

During early phases of releases you may need to specify the staging profile to get the artifacts from the staging
repository that have not been released yet. This is done by adding the `-Pstaging` option to the maven command.
```
mvn clean install -Pstaging -Dmaven.compiler.failOnError=false > /tmp/build.txt
```

At the end of the build the tck test artifacts are installed into the local maven repository.


# JakartaEE TCK Jenkins Jobs

## Releasing tools/common artifacts

The tools/commonn and tools/signaturetest artifacts are released independently of the platform-tck distribution.

* [tools/common](https://ci.eclipse.org/jakartaee-tck/job/11/job/stage-artifacts/job/tck_tools_sigtest_build_and_stage/)
* [tools/signaturetest](https://ci.eclipse.org/jakartaee-tck/job/11/job/stage-artifacts/job/tck_tools_sigtest_build_and_stage/)

## Releasing jakartaee-tck-tools arquillian artifacs

The platform-tck depends on the Arquillian protocol artifacts found in the jakartaee-tck-tools repo. The release job for these artifacts is:

[arquillian-tck-tools](https://ci.eclipse.org/jakartaee-tck/job/TckToolsArquillianRelease/))

## Releasing the platform-tck distributions

The job to build and stage the platform-tck distribution and artifacts is:

[StageTCK](https://ci.eclipse.org/jakartaee-tck/job/11/job/stage-artifacts/job/ReleaseTest/)

# GlassFish TCK Jenkins Jobs
Jenkins jobs for the runners located in the platform-tck/glassfish-runners directory can be found under:

[EE 11 TCK Jobs](https://ci.eclipse.org/jakartaee-tck/job/11/job/tck/)
