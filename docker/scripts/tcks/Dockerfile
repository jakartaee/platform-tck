FROM centos:7

RUN mkdir /tmp/tck
ENV TCK_HOME /tmp/tck
RUN mkdir /tmp/tck/jwsdp
ENV JWSDP_HOME /tmp/tck/jwsdp

WORKDIR ${TCK_HOME}

ARG JDK_BUNDLE_URL
ARG TCK_BUNDLE_URL
ARG GLASSFISH_BUNDLE_URL
ARG JWSDP_BUNDLE
ARG JDK_FOR_JWSDP
ARG http_proxy

COPY docker-setup.sh docker-entrypoint.sh killJava.sh ${TCK_HOME}/
COPY *tck.sh ${TCK_HOME}/

RUN ${TCK_HOME}/docker-setup.sh

ENV JAVA_HOME $TCK_HOME/jdk1.8.0_171
ENV ANT_HOME $TCK_HOME/apache-ant-1.9.7
ENV PATH $JAVA_HOME/bin:$ANT_HOME/bin:$PATH

WORKDIR ${TCK_HOME}

CMD /bin/bash -e docker-entrypoint.sh
