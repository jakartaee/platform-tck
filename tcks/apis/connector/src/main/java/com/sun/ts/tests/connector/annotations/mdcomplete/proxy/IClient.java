package com.sun.ts.tests.connector.annotations.mdcomplete.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.util.Properties;

public interface IClient {
    public RemoteStatus setup(String[] args, Properties props);
    public RemoteStatus testMDCompleteConfigProp();
    public RemoteStatus testMDCompleteMCFAnno();
}
