#!/bin/bash -xe

# A valid username and token should be set in the environment as JENKINS_USER and JENKINS_TOKEN

java src/main/java/aggregator/JenkinsTckCertificationWorkflow.java --aggregator-url 'https://ci.eclipse.org/jakartaee-platform/job/JakartaEE-TCK/view/EFTL-Certification-Jobs-11/job/11/job/tck/job/eftl-jakartaeetck-run-full/' --jobs-base-url 'https://ci.eclipse.org/jakartaee-platform/job/JakartaEE-TCK/view/EFTL-Certification-Jobs-11/job/11/job/tck/' 