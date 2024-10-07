package tck.arquillian.protocol.appclient;

import org.jboss.arquillian.container.test.spi.client.protocol.ProtocolConfiguration;
import tck.arquillian.protocol.common.ProtocolCommonConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Configuration for the AppClient protocol. This is used to configure the appclient process that will be launched to
 * run the appclient main class for the test.
 */
public class AppClientProtocolConfiguration implements ProtocolConfiguration, ProtocolCommonConfig {
    private boolean runClient = true;
    /**
     * Is this appclient being used as a runner for another vehicle type
     */
    private boolean runAsVehicle = false;
    /**
     * Provide an optional envp array to pass as is to {@link Runtime#exec(String[], String[])}
     * @return a possibly empty string providing env1=value1;env2=value2 environment variable settings
     */
    private String clientEnvString;
    /**
     * A ';' (by default) separated string for the command line arguments to pass as the cmdarray to
     * {@link Runtime#exec(String[], String[])}
     */
    private String clientCmdLineString;
    /**
     * The separator to use for splitting the clientCmdLineString
     */
    private String cmdLineArgSeparator = ";";
    /**
     * An optional directory string to use as the appclient process directory. This is passed as the dir arguemnt
     * to {@link Runtime#exec(String[], String[], File)}
     */
    private String clientDir;
    /**
     * The directory to extract the final applclient ear test artifact
     */
    private String clientEarDir = "target/appclient";
    // Timeout waiting for appclient process to exit in MS
    private long clientTimeout = 60000;
    // test working directory
    private String workDir;
    // EE10 type of ts.jte file location
    private String tsJteFile;
    // EE10 type of tssql.stmt file location
    private String tsSqlStmtFile;
    // harness.log.traceflag
    private boolean trace;
    private boolean unpackClientEar = false;

    public boolean isAppClient() {
        return true;
    }
    public boolean isRunClient() {
        return runClient;
    }
    public void setRunClient(boolean runClient) {
        this.runClient = runClient;
    }

    public boolean isRunAsVehicle() {
        return runAsVehicle;
    }
    public void setRunAsVehicle(boolean runAsVehicle) {
        this.runAsVehicle = runAsVehicle;
    }
    public boolean isTrace() {
        return trace;
    }
    public void setTrace(boolean trace) {
        this.trace = trace;
    }

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

    public String getClientEnvString() {
        return clientEnvString;
    }

    public void setClientEnvString(String clientEnvString) {
        this.clientEnvString = clientEnvString;
    }

    public String getClientCmdLineString() {
        return clientCmdLineString;
    }

    /**
     * Set the command line to use for launching the appclient. The individual arguments are separated by the cmdLineArgSeparator
     * setting, which defaults to ';'. A long command line can be split across multiple lines in the arquillian.xml file because
     * the parsed command line array elements are trimmed of leading and trailing whitespace.
     * The command line should be filtered against the ts.jte file if it contains any property references. In addition
     * to ts.jte property references, the command line can contain ${clientEarDir} which will be replaced with the
     * #clientEarDir value. Any ${vehicleArchiveName} ref will be replaced with the vehicleArchiveName passed to the
     * @param clientCmdLineString
     */
    public void setClientCmdLineString(String clientCmdLineString) {
        this.clientCmdLineString = clientCmdLineString;
    }

    public String getCmdLineArgSeparator() {
        return cmdLineArgSeparator;
    }

    /**
     * Set the separator to use for splitting the clientCmdLineString
     * @param cmdLineArgSeparator
     */
    public void setCmdLineArgSeparator(String cmdLineArgSeparator) {
        this.cmdLineArgSeparator = cmdLineArgSeparator;
    }

    public String getClientDir() {
        return clientDir;
    }
    public void setClientDir(String clientDir) {
        this.clientDir = clientDir;
    }

    public String getClientEarDir() {
        return clientEarDir;
    }
    public void setClientEarDir(String clientEarDir) {
        this.clientEarDir = clientEarDir;
    }

    public boolean isUnpackClientEar() {
        return unpackClientEar;
    }

    /**
     * Set to true to unpack the client ear into the clientEarDir. The default is false. This is useful if the
     * vendor appclient requires the ear to be exploded in order to access the appclient jar and bundled ear
     * lib jars.
     * @param unpackClientEar
     */
    public void setUnpackClientEar(boolean unpackClientEar) {
        this.unpackClientEar = unpackClientEar;
    }

    public long getClientTimeout() {
        return clientTimeout;
    }

    /**
     * Set the timeout in milliseconds for waiting for the appclient process to exit. The default is 60000 (1 minute).
     * @param clientTimeout
     */
    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }


    /** Helper methods to turn the strings into the types used by Runtime#exec
     * @return a File object for the clientDir
     */
    public File clientDirAsFile() {
        File dir = null;
        if (clientDir != null) {
            dir = new File(clientDir);
        }
        return dir;
    }

    /**
     * Parse the clientCmdLineString into an array of strings using the cmdLineArgSeparator. This calls String#split on the
     * clientCmdLineString and then trims each element of the resulting array.
     * @return a command line array of strings for use with Runtime#exec.
     */
    public String[] clientCmdLineAsArray() {
        String[] cmdArray = clientCmdLineString.trim().split(cmdLineArgSeparator);
        // Now trim each element
        for (int i = 0; i < cmdArray.length; i++) {
            cmdArray[i] = cmdArray[i].trim();
        }
        return cmdArray;
    }
    public String[] clientEnvAsArray() {
        String[] envp = null;
        if (clientEnvString != null) {
            ArrayList<String> tmp = new ArrayList<String>();
            // Split on the env1=value1 ; separator
            envp = clientEnvString.trim().split(";");
            // Now trim each element
            for (int i = 0; i < envp.length; i++) {
                envp[i] = envp[i].trim();
            }

        }
        return envp;
    }
}
