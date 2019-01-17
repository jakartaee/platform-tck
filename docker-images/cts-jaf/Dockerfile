FROM centos:7

ARG HTTPS_PROXY
ARG MAVEN_VERSION=3.5.4
ARG MAVEN_BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
ARG ANT_VERSION=1.10.5
ARG ANT_BASE_URL=https://apache.osuosl.org/ant/binaries/

ADD jdk-10.0.2_linux-x64_bin.tar.gz /opt
ADD jdk-8u171-linux-x64.tar.gz /opt

RUN echo "proxy=${HTTPS_PROXY}" >> /etc/yum.conf && \
    yum install -y ld-linux.so.2 glibc.i686 git wget which tar zip unzip libXext.x86_64 libXrender.x86_64 libXtst.x86_64 && \
    mkdir -p /usr/share/maven /usr/share/maven/ref && \
    wget -e use_proxy=yes -e https_proxy=${HTTPS_PROXY} --no-cache --no-check-certificate ${MAVEN_BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz -O /tmp/apache-maven.tar.gz && \
    tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 && \
    rm -f /tmp/apache-maven.tar.gz && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn && \
    mkdir -p /usr/share/ant && \
    wget  -e use_proxy=yes -e https_proxy=${HTTPS_PROXY} --no-cache --no-check-certificate ${ANT_BASE_URL}/apache-ant-${ANT_VERSION}-bin.tar.gz -O /tmp/apache-ant.tar.gz && \
    tar -xzf /tmp/apache-ant.tar.gz -C /usr/share/ant --strip-components=1 && \
    ln -s /usr/share/ant/bin/ant /usr/bin/ant && \
    rm -f /tmp/apache-ant.tar.gz && \
    rm -f /opt/*.tar* && \
    mkdir -p /.m2 && \
    chgrp -R 0 /opt /root /usr/share /.m2 && \
    chmod -R g=u /opt /root /usr/share /.m2 && \
    chmod -R 775 /root /.m2

ENV JAVA_HOME=/opt/jdk1.8.0_171
ENV JDK9_HOME=/opt/jdk-10.0.2
ENV ANT_HOME=/usr/share/ant
ENV PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH

CMD which mvn && mvn -version
