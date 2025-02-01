package tck.arquillian.protocol.javatest;

import org.jboss.arquillian.container.test.spi.client.protocol.ProtocolConfiguration;
import tck.arquillian.protocol.common.ProtocolCommonConfig;

public class JavaTestProtocolConfiguration implements ProtocolConfiguration, ProtocolCommonConfig {
    // test working directory
    private String workDir;
    // EE10 type of ts.jte file location
    private String tsJteFile;
    // EE10 type of tssql.stmt file location
    private String tsSqlStmtFile;
    // harness.log.traceflag
    private boolean trace;
    // Should the VehicleClient main be run in a separate JVM
    private boolean fork;

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getTsJteFile() {
        return tsJteFile;
    }

    public void setTsJteFile(String tsJteFile) {
        this.tsJteFile = tsJteFile;
    }

    @Override
    public String getTsSqlStmtFile() {
        return tsSqlStmtFile;
    }
    @Override
    public void setTsSqlStmtFile(String tsSqlStmtFile) {
        this.tsSqlStmtFile = tsSqlStmtFile;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }
}
