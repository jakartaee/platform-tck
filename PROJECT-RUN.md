# Project Specific JakartaEE TCK pipeline
## System Requirements.
1. CJE instance specific for the project.
2. BlueOcean plugin.
3. Required access for creating a new pipeline.

## Instructions for creating the pipeline
  1. Go to the project specific CI instance URL provided by Eclipse foundation team. For eg: JMS project CI instance is hosted here - [https://jenkins.eclipse.org/jms/](https://jenkins.eclipse.org/jms/).
  2. Open the BlueOcean plugin and create a new pipeline. If you have configured the github key already, you should be able to select the Organization as eclipse-ee4j and for repository, you can select `"jakartaee-tck"`
  3. Once the pipeline is created, go to the master branch and click the build with parameters option. Provide the values for the following parameters.
- GF_BUNDLE_URL	- The custom build of GlassFish with the development version of the project artifacts integrated.
- PROFILE - Select full/web profile. Default value is full and can be left as it is.
- BUILD_TYPE - Whether to build and run JakartaEE TCK (`CTS`) bundle or the technology specific StandaloneTCK bundle (`STANDALONE-TCK`)
- test_suites - This parameter is used only when `BUILD_TYPE` selected is `CTS`. This text field contains the space separated values of test suites within the JakartaEE TCK to run. By default all test suites are run.
- standalone_tcks - This parameter is used only when the `BUILD_TYPE` is selected as `STANDALONE_TCK`.
