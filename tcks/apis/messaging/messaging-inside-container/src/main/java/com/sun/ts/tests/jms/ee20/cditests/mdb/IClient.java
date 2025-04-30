package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.lib.harness.RemoteStatus;

public interface IClient {
    public RemoteStatus testCDIInjectionOfMDBWithQueueReplyFromEjb();
    public RemoteStatus testCDIInjectionOfMDBWithTopicReplyFromEjb();
}
