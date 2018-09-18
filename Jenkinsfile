/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

env.label = "cts-ci-pod-${UUID.randomUUID().toString()}"
 
def parallelCTSSuitesMap = params.test_suites.split().collectEntries {
  ["${it}": generateCTSStage(it)]
}
 
def generateCTSStage(job) {
  return {
    podTemplate(label: env.label) {
      node(label) {
        stage("${job}") {
          container('cts-ci') {
            unstash 'cts-bundles'
            sh """
              env
              unzip -o ${WORKSPACE}/cts-bundles/javaeetck.zip -d ${WORKSPACE}
              unzip -o ${WORKSPACE}/cts-bundles/cts-internal.zip -d ${WORKSPACE}
              bash -x ${WORKSPACE}/javaeetck/docker/runcts.sh ${job}
            """
            archiveArtifacts artifacts: "*-results.tar.gz", allowEmptyArchive: true
            junit testResults: 'results/junitreports/*.xml', allowEmptyResults: true
          }
        }
      }
    }
  }
}
 
def parallelStandaloneTCKMap = params.standalone_tcks.split().collectEntries {
  ["${it}": generateStandaloneTCKStage(it)]
}
 
def generateStandaloneTCKStage(job) {
  return {
    podTemplate(label: env.label) {
      node(label) {
        stage("${job}") {
          container('cts-ci') {
            checkout scm
            unstash 'standalone-bundles'
            sh """
              env
              bash -x ${WORKSPACE}/docker/${job}tck.sh
            """
            archiveArtifacts artifacts: "${job}tck-results.tar.gz"
            junit testResults: 'results/junitreports/*.xml', allowEmptyResults: true
          }
        }
      }
    }
  }
}
 
pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  agent {
    kubernetes {
      label "${env.label}"
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
spec:
  hostAliases:
  - ip: "127.0.0.1"
    hostnames:
    - "localhost.localdomain"
  containers:
  - name: cts-ci
    image: anajosep/cts-base:0.1
    command:
    - cat
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "6Gi"
        cpu: "1.25"
  - name: james-mail
    image: linagora/james-jpa-sample:3.0.1
    command:
    - /root/startup.sh
    ports:
    - containerPort: 25
    - containerPort: 143
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "2Gi"
        cpu: "0.5"
"""
    }
  }
  parameters {
    string(name: 'GF_BUNDLE_URL', 
           defaultValue: 'http://download.oracle.com/glassfish/5.0.1/nightly/latest-glassfish.zip', 
           description: 'URL required for downloading GlassFish Full/Web profile bundle' )
    choice(name: 'PROFILE', choices: 'full\nweb', 
           description: 'Profile to be used for running CTS either web/full' )
    choice(name: 'BUILD_TYPE', choices: 'CTS\nSTANDALONE-TCK', 
           description: 'Profile to be used for running CTS either web/full' )
    string(name: 'test_suites', defaultValue: 'compat12 compat13 concurrency connector ejb ejb30/bb ejb30/lite ejb30/assembly ejb30/timer ejb30/webservice ejb30/zombie ejb30/misc ejb30/sec ejb32 el integration interop j2eetools jacc jaspic javaee javamail jaxr jaxrpc jaxrs jaxws jdbc jms jpa_appmanaged jpa_appmanagedNoTx jpa_pmservlet jpa_puservlet jpa_stateful3 jpa_stateless3 jsf jsonb jsonp jsp jstl jta jws rmiiiop saaj samples securityapi servlet signaturetest/javaee webservices webservices12 webservices13 websocket xa',
           description: 'Space separated list of Test suites to run') 
    string(name: 'standalone_tcks', defaultValue: 'caj concurrency connector el jacc jaspic jaxr jaxrpc jaxrs jaxws jms jpa jsf jsp jsonb jsonp jstl jta saaj securityapi servlet websocket', 
           description: 'Space separated list of standalone TCKs to build and run') 
    string(name: 'HTTP_PROXY', defaultValue: '', 
           description: 'Proxy for connecting to http URL')
    string(name: 'HTTPS_PROXY', defaultValue: '', 
           description: 'Proxy for connecting to http URL')
    string(name: 'SCHEMA_HOSTING_SERVER', defaultValue: '', 
           description: 'Server Hosting the xsd and dtds required for building CTS/TCK bundles')
 
    booleanParam( name: 'buildCTSFlag', defaultValue: true, 
           description: 'Temporary parameter: Flag to control if CTS has to be built or downloaded from server')
    string(name: 'JAVAEETCK_BUNDLE_URL', 
           defaultValue: '',
           description: 'Temporary parameter: URL required for downloading JAVAEETCK bundle' )
    string(name: 'CTS_INTERNAL_BUNDLE_URL', 
           defaultValue: '',
           description: 'Temporary parameter: URL required for downloading CTS Internal bundle' )
  }
  environment {
    JAVA_HOME = "/opt/jdk1.8.0_171/"
    ANT_HOME = "/usr/share/ant"
    PATH = "${ANT_HOME}/bin:${JAVA_HOME}/bin:${PATH}"
    CTS_HOME = "${WORKSPACE}"
    http_proxy = "${params.HTTP_PROXY}" 
    https_proxy = "${params.HTTPS_PROXY}"
    ANT_OPTS = "-Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http" 
    BUILD_CTS_FLAG="${params.buildCTSFlag}"
    MAIL_USER="user01@james.local"
    LANG="en_US.UTF-8"
  }
  stages {
    stage('cts-build') {
      when {
        expression {
          return params.BUILD_TYPE == 'CTS';
        }
      }
      steps {
        container('cts-ci') {
          sh """
            env
            bash -x ${WORKSPACE}/docker/build_cts8.sh
          """
          archiveArtifacts artifacts: 'cts-bundles/*.zip'
          stash includes: 'cts-bundles/*.zip', name: 'cts-bundles'
        }
      }
    }
 
    stage('cts-run') {
      when {
        expression {
          return params.BUILD_TYPE == 'CTS';
        }
      }
 
      steps {
        script {
          parallel parallelCTSSuitesMap
        }
      }
    }
 
    stage('standalone-tck-build') {
      when {
        expression {
          return params.BUILD_TYPE == 'STANDALONE-TCK';
        }
      }
 
      steps {
        container('cts-ci') {
          sh """
            env
            bash -x ${WORKSPACE}/docker/build_standalone-tcks.sh ${standalone_tcks}
          """
          archiveArtifacts artifacts: 'standalone-bundles/*.zip'
          stash includes: 'standalone-bundles/*.zip', name: 'standalone-bundles'
        }
      }
    }
 
    stage('standalone-tck-run') {
      when {
        expression {
          return params.BUILD_TYPE == 'STANDALONE-TCK';
        }
      }
      steps {
        script {
          parallel parallelStandaloneTCKMap
        }
      }
    }
  }
}
