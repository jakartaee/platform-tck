package test.com.sun.ts.tests.common.vehicle.none.proxy;

import com.sun.ts.lib.harness.RemoteStatus;

import java.util.Properties;

public interface IExample {
    RemoteStatus setup(String[] args);
    RemoteStatus setup(String[] args, Properties props);
    RemoteStatus test1();
    RemoteStatus test1Ex();
    RemoteStatus test2();
    void ignored();
}
