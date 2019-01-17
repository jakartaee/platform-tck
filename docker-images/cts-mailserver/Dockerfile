FROM linagora/james-jpa-sample:3.0.1

RUN chgrp -R 0 /root /logs /var && \
    chmod -R g=u /root /logs /var && \
    sed -i s/:143/:1143/g /root/conf/imapserver.xml && \
    sed -i s/:993/:1993/g /root/conf/imapserver.xml && \
    sed -i s/:110/:1110/g /root/conf/pop3server.xml && \
    sed -i s/:25/:1025/g /root/conf/smtpserver.xml && \
    sed -i s/:465/:1465/g /root/conf/smtpserver.xml && \
    sed -i s/:587/:1587/g /root/conf/smtpserver.xml 

CMD /root/startup.sh
