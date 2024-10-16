package tck.arquillian.protocol.common;

public interface ProtocolCommonConfig {
    default boolean isAppClient() {
        return false;
    };

    public boolean isTrace();
    public void setTrace(boolean trace);

    public String getWorkDir();
    public void setWorkDir(String workDir);

    public String getTsJteFile();
    public void setTsJteFile(String tsJteFile);

    public String getTsSqlStmtFile();
    public void setTsSqlStmtFile(String tsJteFile);
}
