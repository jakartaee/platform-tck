This wiki needs to be updated as some of the jobs mentioned in this wiki have been renamed.

# Jakarta EE TCK Jenkins jobs
Jakarta EE TCK jobs uses the Jenkins OSS customized by the Eclipse Foundation team hosted at [https://ci.eclipse.org/jakartaee-tck/](https://ci.eclipse.org/jakartaee-tck/) for the various build, release and run jobs. It uses the Jenkins pipeline setup based on the [Jenkinsfile](https://github.com/eclipse-ee4j/jakartaee-tck/blob/master/Jenkinsfile) checked into the Jakarta EE TCK Github repository for running all kinds of jobs. The pipeline job is parameterized, which can take in multiple inputs from the user or upstream jobs and execute the required build/run stages in the pipeline. Various upstream jobs are created in the CJE instance which in turn use this Jenkins pipeline in master branch to trigger various kinds of jobs by passing appropriate input parameters.

There are 2 kinds of TCK bundles that gets created from the Jenkins Jobs. They are 
* Jakarta EE TCK bundle (Generated from Jakarta EE TCK Github repository)

    This is a single bundle (jakartaeetck.zip) containing the tests, scripts and configurations required to validate and confirm all technologies that are part of the Jakarta EE specification. This bundle is meant to be run with various Java EE compatible servers (like GlassFish, Payara, WebLogic, Wildfly etc). The default configuration stored in the bundle is used for running tests against GlassFish RI bundle. The tests typically run in various containers like EJB, Servlet, AppClient etc.
* Standalone TCK bundles

    These are small and independent subset of tests along with the scripts and configurations required for validating a specific technology. The specific technology may or may not be part of the Jakarta EE specification. The default configurations stored in the bundle is used for running the tests against standalone RI implementation. For instance, JSON-B tck bundle can be used for running against Yasson standalone RI implementation. The various technologies for which the standalone TCK bundles gets generated are as follows:
   
   Generated from Jakarta EE TCK Github repository
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

   Generated from repositories outside Jakarta EE TCK Github.
   * cditck-porting
   * ditck-porting
   * bvtck-porting
   * debugging-support-for-other-languages-tck
   * jaxb-tck

## Job Summary
| Job Name      | Trigger Type | Frequency | Report Link
| ------------- | ------------- | ------------- | ------------- |
| [publish-glassfish-bundle](https://ci.eclipse.org/jakartaee-tck/job/publish-glassfish-bundle/)  | Automated  | Nightly | NA |
| [oracle-javaeects8-certification-full](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-full/)  | Manual| Milestone builds| [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [oracle-javaeects8-certification-web](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-web/)  | Manual | Milestone builds| [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [jakartaeetck-nightly-build](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/)  | Automated  | Nightly | NA |
| [jakartaeetck-nightly-run](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run/)  | Automated  | Nightly | [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [jakartaeetck-nightly-run-web](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run-web/)  | Automated  | Nightly | [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [jakartaeetck-certification](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-certification/)  | Manual| Milestone builds/Debugging failures| [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-certification/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/)  | Automated  | Nightly | [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)  | Automated  | Nightly | [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [standalonetck-certification](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-certification/)  | Manual| Milestone builds/Debugging failures| [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-certification/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |
| [jaxb-tck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck-nightly-build-run/)  | Automated  | Nightly | [Latest Report](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html) |

## Staging Job
* [publish-glassfish-bundle](https://ci.eclipse.org/jakartaee-tck/job/publish-glassfish-bundle/)
This job runs on a nightly basis and publishes the latest Eclipse GlassFish bundle built from master branch to the staging area in Eclipse download server. The latest bundle would be accessible [here](https://download.eclipse.org/ee4j/jakartaee-tck/jakartaee8/nightly/glassfish.zip)

## Oracle JavaEE 8 CTS bundle related Jobs
These jobs are meant for certifying Eclipse GlassFish 5.1.0 against the Oracle Java EE 8 CTS certification bundle published by Oracle. These jobs are required to be run till the first release of Jakarta EE TCK bundle is released officially from Eclipse Foundation.
* [oracle-javaeects8-certification-full](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-full/)
This job is meant for running the entire Oracle Java EE 8 CTS tests using the latest released bundle published by Oracle and available in the staging area against the latest Eclipse GlassFish full profile bundle built from master branch. This job is triggered manually on a need basis. The latest results would be published [here](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-full/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/). 

* [oracle-javaeects8-certification-web](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-web/)
This job is meant for running the web profile related Oracle Java EE 8 CTS tests using the latest released bundle published by Oracle and available in the staging area against the latest Eclipse GlassFish web profile bundle built from master branch. This job is triggered manually on a need basis. The latest results would be published [here](https://ci.eclipse.org/jakartaee-tck/job/oracle-javaeects8-certification-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).


## Jakarata EE TCK bundle related Jobs
The following jobs are used for building and running the Jakarta EE TCKs from [Jakarta EE TCK Github Repository](https://github.com/eclipse-ee4j/jakartaee-tck) against Eclipse GlassFish RI.

* [jakartaeetck-nightly-build](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/)
This job is meant for building the Jakarta EE TCK bundle (bundle used for certifying all JavaEE/JakaratEE Technologies). It builds the Jakarta EE TCK bundle (jakartaeetck.zip) and publishes it to the [nightly staging area](https://download.eclipse.org/ee4j/jakartaee-tck/jakartaee8/nightly/jakartaeetck.zip). This job is triggered on a daily basis and does not require any user intervention. It runs the samples suite to validate the built bundle. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).


* [jakartaeetck-nightly-run](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run/)
This job is meant for running the entire Jakarta EE TCK tests using the latest nightly TCK bundle available in the staging area against the latest GlassFish full profile bundle built from master branch. This job is triggered on a daily basis and does not require any user intervention. The latest results would be published [here](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).


* [jakartaeetck-nightly-run-web](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run-web/)
This job is meant for running the web profile related Jakarta EE TCK tests using the latest nightly TCK bundle available in the staging area against the latest GlassFish web profile bundle built from master branch. This job is triggered on a daily basis and does not require any user intervention. The latest results would be published [here](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run-web/lastSuccessfulBuild/junit-reports-with-handlebars/testSuitesOverview.html). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).


* [jakartaeetck-certification](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-certification/)
This job is meant for certifying milestone builds like (RC1, RC2 etc.) or for validating a custom development build to run a subset of tests for validation and debugging failures. This is not triggered periodically and used only on a need basis. This job is parameterized and if its required to run confirm only a subsets of technologies, then those alone can be selected and run. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).



## Standalone TCK bundle related Jobs
The following jobs are used for building and running the various standalone technology TCKS against Eclipse GlassFish RI.

* [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/)
This job runs on a nightly basis and builds all the available standalone technology TCKs in full profile mode from scratch and runs it against the latest nightly [GlassFish full bundle](https://jenkins.eclipse.org/glassfish/job/glassfish/job/master/lastSuccessfulBuild/artifact/bundles/glassfish.zip) built in master branch. The job publishes the standalone TCK bundles to the nightly staging area available [here](https://download.eclipse.org/ee4j/jakartaee-tck/jakartaee8/nightly/). The latest nightly run results are available [here](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars). The details of the GlassFish Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking.
The downstream pipeline jobs triggered by this job are:

    * [bvtck-porting](https://ci.eclipse.org/jakartaee-tck/job/bvtck-porting/)
    * [cditck-porting](https://ci.eclipse.org/jakartaee-tck/job/cditck-porting/)
    * [ditck-porting](https://ci.eclipse.org/jakartaee-tck/job/ditck-porting/)
    * [debugging-support-for-other-languages-tck](https://ci.eclipse.org/jakartaee-tck/job/debugging-support-for-other-languages-tck/)
    * [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/)

* [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
This job runs on a nightly basis and builds all the available standalone technology TCKs in web profile mode from scratch and runs it against the latest nightly [GlassFish Web bundle](https://jenkins.eclipse.org/glassfish/job/glassfish/job/master/lastSuccessfulBuild/artifact/bundles/web.zip) built in master branch. The latest nightly run results are available [here](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars). The details of the GlassFish Web Bundle used identified by its unique Git Commit ID and the JakartaEE TCK bundle used for the run, are mentioned in the build description for easy tracking.
The downstream pipeline jobs triggered by this job are:

    * [bvtck-porting](https://ci.eclipse.org/jakartaee-tck/job/bvtck-porting/)
    * [cditck-porting](https://ci.eclipse.org/jakartaee-tck/job/cditck-porting/)
    * [ditck-porting](https://ci.eclipse.org/jakartaee-tck/job/ditck-porting/)
    * [debugging-support-for-other-languages-tck](https://ci.eclipse.org/jakartaee-tck/job/debugging-support-for-other-languages-tck/)
    * [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/)

* [jaxb-tck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck-nightly-build-run/)
This job runs on a nightly basis and builds jaxb standalone technology TCK in from scratch and runs it against the latest nightly [GlassFish bundle](https://jenkins.eclipse.org/glassfish/job/glassfish/job/master/lastSuccessfulBuild/artifact/bundles/glassfish.zip) built in master branch. The job publishes the jaxb TCK bundle to the nightly staging area available [here](https://download.eclipse.org/ee4j/jakartaee-tck/8.0.1/nightly/). The latest nightly run results are available [here](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck-nightly-build-run/lastSuccessfulBuild/junit-reports-with-handlebars). The downstream pipeline job that is triggered by this job is [jaxb-tck](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck) 

* [standalonetck-certification](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-certification/)
This job is meant for certifying milestone builds like (RC1, RC2 etc.) or for validating a custom development build to run a subset of tests for validation and debugging failures. This is not triggered periodically and used only on a need basis. This job is parameterized and if its required to run confirm only a subsets of technologies, then those alone can be selected and run. The downstream pipeline job that is triggered by this job is [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/).



## All Downstream Pipeline Jobs 

The pipeline jobs that are triggered by the upstream jobs are mentioned below :

 * [bvtck-porting](https://ci.eclipse.org/jakartaee-tck/job/bvtck-porting/) : This Bean Validation porting TCK job corresponds to the Jenkinsfile at [bvtck repo](https://github.com/eclipse-ee4j/bvtck-porting/) and is triggered by [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/) and [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
 * [cditck-porting](https://ci.eclipse.org/jakartaee-tck/job/cditck-porting/) : This CDI porting TCK job corresponds to the Jenkinsfile at [cditck repo](https://github.com/eclipse-ee4j/cditck-porting/) and is triggered by [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/) and [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
 * [ditck-porting](https://ci.eclipse.org/jakartaee-tck/job/ditck-porting/) : This Dependency Injection porting TCK job corresponds to the Jenkinsfile at [ditck repo](https://github.com/eclipse-ee4j/ditck-porting) and is triggered by [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/) and [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
 * [debugging-support-for-other-languages-tck](https://ci.eclipse.org/jakartaee-tck/job/debugging-support-for-other-languages-tck/) : This debugging-support-for-other-languages-tck job corresponds to the Jenkinsfile at [debugging-support-for-other-languages-tck repo](https://github.com/eclipse-ee4j/debugging-support-for-other-languages-tck) and is triggered by [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/) and [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
 * [jaxb-tck](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck/) : This TCK job corresponds to the Jenkinsfile at [jaxb-tck repo](https://github.com/eclipse-ee4j/jaxb-tck) and is triggered by [jaxb-tck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/jaxb-tck-nightly-build-run/)
 * [jakartaee-tck](https://ci.eclipse.org/jakartaee-tck/job/jakartaee-tck/) : This jakartaee-tck job corresponds to the Jenkinsfile at [jakartaee-tck repo](https://github.com/eclipse-ee4j/jakartaee-tck) 
   This is triggered by below jobs:
   * [jakartaeetck-nightly-run](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run/) for jakartaee-tck full profile run
   * [jakartaeetck-nightly-run-web](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-run-web/) for web profile run 
   * [jakartaeetck-nightly-build](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-nightly-build/) for jakartaee tck nightly bundle generation
   * [standalonetck-nightly-build-run](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run/)               
   * [standalonetck-nightly-build-run-web](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-nightly-build-run-web/)
   * [jakartaeetck-certification](https://ci.eclipse.org/jakartaee-tck/job/jakartaeetck-certification) for  parameterized tck test suite runs 
   * [standalonetck-certification](https://ci.eclipse.org/jakartaee-tck/job/standalonetck-certification/) for  parameterized standalone tck test runs 
