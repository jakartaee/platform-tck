FROM linagora/james-jpa-sample:3.0.1

ADD create_users.sh /root
ADD james.log /logs

RUN chgrp -R 0 /root /logs /logs/james.log /var && \
    chmod -R g=u /root /logs /logs/james.log /var && \
    chmod +x /root/create_users.sh && \
    sed -i s/:143/:1143/g /root/conf/imapserver.xml && \
    sed -i s/:993/:1993/g /root/conf/imapserver.xml && \
    sed -i s/:110/:1110/g /root/conf/pop3server.xml && \
    sed -i s/:25/:1025/g /root/conf/smtpserver.xml && \
    sed -i s/:465/:1465/g /root/conf/smtpserver.xml && \
    sed -i s/:587/:1587/g /root/conf/smtpserver.xml

#CMD /root/startup.sh
ENTRYPOINT cat
