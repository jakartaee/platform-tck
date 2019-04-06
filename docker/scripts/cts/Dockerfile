FROM centos:7

RUN mkdir /tmp/cts
ENV CTS_HOME /tmp/cts
WORKDIR ${CTS_HOME}
RUN mkdir /tmp/cts/jwsdp
ENV JWSDP_HOME /tmp/cts/jwsdp

ARG JDK_BUNDLE_URL
ARG CTS_BUNDLE_URL
ARG GLASSFISH_BUNDLE_URL
ARG JWSDP_BUNDLE
ARG JDK_FOR_JWSDP
ARG http_proxy
ARG test_suite

COPY docker-setup.sh docker-entrypoint.sh killJava.sh admin-password.txt change-admin-password.txt jwsdp-setup.sh ${CTS_HOME}/

RUN ${CTS_HOME}/docker-setup.sh
RUN ${CTS_HOME}/jwsdp-setup.sh

ENV JAVA_HOME $CTS_HOME/jdk1.8.0_171
ENV ANT_HOME $CTS_HOME/apache-ant-1.9.7
ENV TS_HOME $CTS_HOME/jakartaeetck
ENV PATH $JAVA_HOME/bin:$ANT_HOME/bin:$TS_HOME/bin:$PATH

WORKDIR ${CTS_HOME}

CMD /bin/bash -e docker-entrypoint.sh
