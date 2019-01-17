# Jakarta EE TCK Jenkins jobs
Jakarta EE TCK jobs uses the Cloud bees Jenkins Enterprise Server (CJE) hosted at [https://jenkins.eclipse.org/jakartaee-tck/](https://jenkins.eclipse.org/jakartaee-tck/) for the various build, release and run jobs. It uses the Jenkins pipeline setup based on the [Jenkinsfile](https://github.com/eclipse-ee4j/jakartaee-tck/blob/master/Jenkinsfile) checked into the Jakarta EE TCK Github repository for running all kinds of jobs. The pipeline job is parameterized, which can take in multiple inputs from the user or upstream jobs and execute the required build/run stages in the pipeline. Various upstream jobs are created in the CJE instance which in turn use this Jenkins pipeline in master branch to trigger various kinds of jobs by passing appropriate input parameters.

There are 2 kinds of TCK bundles that gets created from the Jakarta EE TCK Github repository. They are 
* Jakarta EE TCK bundle 

    This is a single bundle (javaeetck.zip) containing the tests, scripts and configurations required to validate and confirm all technologies that are part of the Jakarat EE specification. This bundle is meant to be run with various Java EE compatible servers (like GlassFish, Payara, WebLogic, Wildfly etc). The default configuration stored in the bundle is used for running tests against GlassFish RI bundle. The tests typically run in various containers like EJB, Servlet, AppClient etc.
* Standalone TCK bundles

    These are small and independent subset of tests along with the scripts and configurations required for validating a specific technology. The specific technology may or may not be part of the Jakarta EE specification. The default configurations stored in the bundle is used for running the tests against standalone RI implementation. For instance, JSON-B tck bundle can be used for running against Yasson standalone RI implementation. The various technologies for which the standalone TCK bundles gets generated are as follows:
    * CAJ
    * Concurrency 
    * Connector 
    * EL 
    * JACC
    * JASPIC 
    * JAXR 
    * JAX-RPC 
    * JAX-RS 
    * JAX-WS 
    * JMS
    * JPA
    * JSF
    * JSP
    * JSON-B
    * JSON-P
    * JSTL
    * JTA
    * SAAJ
    * SecurityAPI
    * Servlet
    * WebSocket

## Jakarata EE TCK bundle related Jobs
The following jobs are used for building and running the Jakarta EE TCKs from [Jakarta EE TCK Github Repository](https://github.com/eclipse-ee4j/jakartaee-tck) against Eclipse GlassFish RI.

* [jakartaeetck-nightly-build](https://jenkins.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/)
This job is meant for building the Jakarta EE TCK bundle (bundle used for certifying all JavaEE/JakaratEE Technologies). It builds the Jakarta EE TCK bundle (javaeetck.zip) and publishes it to the [nightly staging area](https://download.eclipse.org/ee4j/jakartaee-tck/8.0.1/nightly/javaeetck.zip). This job is triggered on a daily basis and does not require any user intervention. It runs the samples suite to validate the built bundle.

* [jakartaeetck-nightly-run](https://jenkins.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/)
This job is meant for running the entire Jakarta EE TCK tests using the latest nightly TCK bundle available in the staging area against the latest GlassFish bundle built from EE4J_8 branch. This job is triggered on a daily basis and does not require any user intervention. The latest results would be published [here](https://jenkins.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking.

* [jakartaeetck-certification](https://jenkins.eclipse.org/jakartaee-tck/job/jakartaeetck-certification/)
This job is meant for certifying milestone builds like (RC1, RC2 etc.) or for validating a custom development build to run a subset of tests for validation and debugging failures. This is not triggered periodically and used only on a need basis. This job is parameterized and if its required to run confirm only a subsets of technologies, then those alone can be selected and run.

## Standalone TCK bundle related Jobs
The following jobs are used for building and running the various standalone technology TCKS from [Jakarta EE TCK Github Repository](https://github.com/eclipse-ee4j/jakartaee-tck) against Eclipse GlassFish RI.

* [standalonetck-nightly-build-run](https://jenkins.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/)
This job runs on a nightly basis and builds all the available standalone technology TCKs from scratch and runs it against the latest nightly [GlassFish bundle](https://jenkins.eclipse.org/glassfish/job/glassfish/job/EE4J_8/lastSuccessfulBuild/artifact/bundles/glassfish.zip) built in EE4J_8 branch. The job publishes the standalone TCK bundles to the nightly staging area available [here](https://download.eclipse.org/ee4j/jakartaee-tck/8.0.1/nightly/). The latest nightly run results are available [here](https://jenkins.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking.

* [standalonetck-certification](https://jenkins.eclipse.org/jakartaee-tck/job/standalonetck-certification/)
This job is meant for certifying milestone builds like (RC1, RC2 etc.) or for validating a custom development build to run a subset of tests for validation and debugging failures. This is not triggered periodically and used only on a need basis. This job is parameterized and if its required to run confirm only a subsets of technologies, then those alone can be selected and run.


