## Create new Centos VM/container As root user:
`podman run -it centos /bin/bash`

## Install some tools (including openjdk version "1.8.0_252")
`yum install -y zip java-1.8.0-openjdk-devel wget vim git unzip`

## change into /tmp/ee9
```
mkdir /tmp/ee9
cd /tmp/ee9
```

## Download/extract Platform TCK nightly build
```
wget http://download.eclipse.org/ee4j/jakartaee-tck/jakartaee9/nightly/jakartaeetck-9.0.0.zip
unzip jakartaeetck-9.0.0.zip
```

## set WORKSPACE=$PWD
`export WORKSPACE=$PWD`

## ensure Java is setup completely
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
export PATH=/usr/lib/jvm/java-1.8.0-openjdk/bin:$PATH
```

## specify nightly build of GlassFish to be used
`export GF_BUNDLE_URL=https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-6.0.0-SNAPSHOT-nightly.zip`

## setup ANT
```wget https://downloads.apache.org/ant/binaries/apache-ant-1.10.8-bin.zip
unzip -o apache-ant-1.10.8-bin.zip; rm -f apache-ant-1.10.8-bin.zip
export ANT_HOME=$PWD/apache-ant-1.10.8
export PATH=$ANT_HOME/bin:$PATH
export ANT_OPTS="-Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http -Duser.home=$HOME"
wget https://repo1.maven.org/maven2/ant-contrib/ant-contrib/1.0b3/ant-contrib-1.0b3.jar
mv ant-contrib-1.0b3.jar $ANT_HOME/lib
```

## update jakartaeetck/docker/run_jakartaeetck.sh to be executable
`chmod +x jakartaeetck/docker/run_jakartaeetck.sh`

## Run samples tests (note that mail tests will fail since no mail server is running)
`bash -x jakartaeetck/docker/run_jakartaeetck.sh samples 2>&1 | tee run_samples.log`

## Run a subset of tests under webservices12/sec/descriptors/ejb/basicSSL
`
bash -x jakartaeetck/docker/run_jakartaeetck.sh webservices12/sec/descriptors/ejb/basicSSL 2>&1 | tee run_cts.log
`
