FROM centos:7

ARG HTTPS_PROXY
ARG MAVEN_VERSION=3.5.4
ARG ANT_VERSION=1.10.5
ARG MAVEN_BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries
ARG ANT_BASE_URL=https://apache.osuosl.org/ant/binaries/

ADD jwsdp-1.3.tar /opt
ADD jdk-8u191-linux-x64.tar.gz /opt
ADD j2sdk-1_4_2_41-linux-i586.tar.gz /opt
COPY ant-props.jar /opt/

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
    cp /opt/ant-props.jar /usr/share/ant/lib && \
    rm -f /tmp/apache-ant.tar.gz && \
    rm -f /opt/*.tar* && \
    rm -f /opt/ant-props.jar && \
    mkdir -p /.m2 &&\
    chgrp -R 0 /opt /root /usr/share /.m2 && \
    chmod -R g=u /opt /root /usr/share /.m2 && \
    chmod -R 775 /root /.m2

ENV JAVA_HOME=/opt/jdk1.8.0_191
ENV ANT_HOME=/usr/share/ant
ENV PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH

CMD which ant && ant -version
