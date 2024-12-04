package tck.arquillian.protocol.common;

import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.test.spi.TestMethodExecutor;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class TsTestPropsBuilder {
    static Logger log = Logger.getLogger(TsTestPropsBuilder.class.getName());

    // Property names passed from the ts.jte file to the tstest.jte file
    // Parsed from the test @class.setup_props: values + additional seen to be used by harness
    static String[] tsJtePropNames = {
            "s1as",
            "s1as.modules",
            "Driver",
            "authpassword",
            "authuser",
            "binarySize",
            "cofSize",
            "cofTypeSize",
            "db.dml.file",
            "db.supports.sequence",
            "db1",
            "db2",
            "DriverManager",
            "ftable",
            "generateSQL",
            "harness.log.port",
            "harness.log.traceflag",
            "harness.socket.retry.count",
            "harness.temp.directory",
            "imap.port",
            "iofile",
            "java.naming.factory.initial",
            "javamail.mailbox",
            "javamail.password",
            "javamail.protocol",
            "javamail.root.path",
            "javamail.server",
            "javamail.username",
            "jdbc.db",
            "jms_timeout",
            "jstl.db.user",
            "jstl.db.password",
            "log.file.location",
            "logical.hostname.servlet",
            "longvarbinarySize",
            "mailuser1",
            "org.omg.CORBA.ORBClass",
            "password",
            "password1",
            "platform.mode",
            "porting.ts.HttpsURLConnection.class.1",
            "porting.ts.HttpsURLConnection.class.2",
            "porting.ts.login.class.1",
            "porting.ts.login.class.2",
            "porting.ts.url.class.1",
            "porting.ts.url.class.2",
            "porting.ts.jms.class.1",
            "porting.ts.jms.class.2",
            // These two are probably not useful
            "porting.ts.deploy.class.1",
            "porting.ts.deploy.class.2",
            "ptable",
            "rapassword1",
            "rapassword2",
            "rauser1",
            "rauser2",
            "securedWebServicePort",
            "sigTestClasspath",
            "smtp.port",
            "transport_protocol",
            "ts_home",
            "user",
            "user1",
            "varbinarySize",
            "variable.mapper",
            "vehicle_ear_name",
            "webServerHost",
            "webServerPort",
            "whitebox-anno_no_md",
            "whitebox-mdcomplete",
            "whitebox-mixedmode",
            "whitebox-multianno",
            "whitebox-notx",
            "whitebox-notx-param",
            "whitebox-permissiondd",
            "whitebox-tx",
            "whitebox-tx-param",
            "whitebox-xa",
            "whitebox-xa-param",
            "work.dir",
            "ws_wait",
    };

    /**
     * Get the deployment vehicle archive name from the deployment archive. This needs to be a vehicle deployment
     * for the result to be valid.
     * @param deployment - current test deployment
     * @return base vehicle archive name
     */
    public static String vehicleArchiveName(Deployment deployment) {
        // Get deployment archive name and remove the .* suffix
        String vehicleArchiveName = deployment.getDescription().getArchive().getName();
        int dot = vehicleArchiveName.lastIndexOf('.');
        if(dot != -1) {
            vehicleArchiveName = vehicleArchiveName.substring(0, dot);
        }
        return vehicleArchiveName;
    }

    /**
     * Get the test runs args for the vehicle or appclient. If this is a non-vehicle appclient tests, the args
     * @param config
     * @param deployment
     * @param testMethodExecutor
     * @return
     * @throws IOException
     */
    public static String[] runArgs(ProtocolCommonConfig config, Deployment deployment,
                                   TestMethodExecutor testMethodExecutor) throws IOException {
        Class<?> testClass = testMethodExecutor.getMethod().getDeclaringClass();
        Class<?> testSuperclass = testClass.getSuperclass();
        TargetVehicle testVehicle = testMethodExecutor.getMethod().getAnnotation(TargetVehicle.class);
        String testMethodName = testMethodExecutor.getMethod().getName();
        // The none vehicle is a basic appclient test, not a vehicle based test
        String vehicle = "none";
        if (testVehicle != null) {
            vehicle = testVehicle.value();
        }

        log.info(String.format("Base class: %s, vehicle: %s", testSuperclass.getName(), vehicle));
        // Get deployment archive name and remove the .* suffix
        String vehicleArchiveName = vehicleArchiveName(deployment);

        // We need the JavaTest ts.jte file for now
        Path tsJte = Paths.get(config.getTsJteFile());
        Path tssqlStmt = null;
        if (config.getTsSqlStmtFile() != null) {
            tssqlStmt = Paths.get(config.getTsSqlStmtFile());
        }
        // Create a test properties file
        String workDir = config.getWorkDir();
        if(workDir == null) {
             throw new IllegalStateException("Missing workDir value for test properties file");
        }
        Path workDirPath = Paths.get(workDir);
        if(!workDirPath.toFile().exists()) {
            log.info("Creating work directory: "+workDirPath.toAbsolutePath());
            Files.createDirectory(workDirPath);
        }
        Path testProps = workDirPath.resolve("tstest.jte");

        // Seed the test properties file with select ts.jte file settings
        Properties tsJteProps = new Properties();
        tsJteProps.load(new FileReader(tsJte.toFile()));
        log.info("Read in ts.jte file: "+tsJte);
        // The test specific properties file
        Properties props = new Properties();
        // A property set by the TSScript class
        if(vehicle.equals("ejb") && config.isAppClient()) {
            props.setProperty("finder", "jck");
        } else {
            props.setProperty("finder", "cts");
        }
        // Vehicle
        props.setProperty("service_eetest.vehicles", vehicle);
        props.setProperty("vehicle", vehicle);
        props.setProperty("vehicle_archive_name", vehicleArchiveName);
        //
        props.setProperty("harness.log.delayseconds", "0");
        if(config.isTrace()) {
            // This overrides the ts.jte harness.log.traceflag value
            props.setProperty("harness.log.traceflag", "true");
        }
        // Copy over common ts.jte settings
        for (String propName : tsJtePropNames) {
            String propValue = tsJteProps.getProperty(propName);
            if(propValue != null) {
                if(propValue.startsWith("${") && propValue.endsWith("}")) {
                    String refName = propValue.substring(2, propValue.length() - 1);
                    propValue = tsJteProps.getProperty(refName);
                    if(propValue == null && refName != null) {
                        propValue = System.getProperty(refName);
                    }
                    log.info(String.format("Setting property %s -> %s to %s", propName, refName, propValue));
                }
                if(propValue == null) {
                    propValue = System.getProperty(propName);
                }

                if(propValue == null) {
                    continue;
                }
                props.setProperty(propName, propValue);
            }
        }

        // The vehicle harness operates on the legacy CTS superclass of the Junit5 class.
        // unless the Junit5 test class directly extends the EETest/ServiceEETest class.
        if(isAbstract(testSuperclass)) {
            props.setProperty("test_classname", testClass.getName());
        } else {
            props.setProperty("test_classname", testSuperclass.getName());
        }

        // Write out the test properties file, overwriting any existing file
        try(OutputStream out = Files.newOutputStream(testProps)) {
            props.store(out, "Properties for test: "+testMethodName);
            log.info(props.toString());
        }

        String[] args = {
                // test props are needed by EETest.run
                "-p", testProps.toFile().getAbsolutePath(),
                "-ap", tssqlStmt != null ? tssqlStmt.toFile().getAbsolutePath() : "/dev/null",
                "-classname", testMethodExecutor.getMethod().getDeclaringClass().getName(),
                "-t", testMethodName,
                "-vehicle", vehicle,
        };
        return args;
    }

    public static boolean isAbstract(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        return Modifier.isAbstract(modifiers);
    }
}
