package tck.arquillian.protocol.javatest;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.vehicle.VehicleClient;
import org.jboss.arquillian.container.spi.client.deployment.Deployment;
import org.jboss.arquillian.container.spi.context.annotation.DeploymentScoped;
import org.jboss.arquillian.container.test.spi.ContainerMethodExecutor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestMethodExecutor;
import org.jboss.arquillian.test.spi.TestResult;
import tck.arquillian.protocol.common.TargetVehicle;
import tck.arquillian.protocol.common.TsTestPropsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.logging.Logger;

/**
 * A protocol that invokes that JavaTest CTS test client in the same JVM using the vehicle client or
 * the non-vehicle client run method.
 */
public class JavaTestMethodExecutor implements ContainerMethodExecutor {
    static Logger log = Logger.getLogger(JavaTestMethodExecutor.class.getName());
    private JavaTestProtocolConfiguration config;
    @Inject
    @DeploymentScoped
    private Instance<Deployment> deploymentInstance;

    public JavaTestMethodExecutor(JavaTestProtocolConfiguration config) {
        this.config = config;
    }

    @Override
    public TestResult invoke(TestMethodExecutor testMethodExecutor) {
        log.fine("Executing test method: " + testMethodExecutor.getMethod().getName());
        long start = System.currentTimeMillis();
        // Get deployment archive name and remove the .* suffix
        Deployment deployment = deploymentInstance.get();
        TargetVehicle testVehicle = testMethodExecutor.getMethod().getAnnotation(TargetVehicle.class);
        String vehicle = "none";
        if(testVehicle != null) {
            vehicle = testVehicle.value();
        }

        String[] args;
        try {
            args = TsTestPropsBuilder.runArgs(config, deployment, testMethodExecutor);
        } catch (IOException e) {
            TestResult result = TestResult.failed(e);
            result.addDescription("Failed to write test properties");
            return result;
        }

        // We are running in the same JVM, JavaTest CTS runs in a separate JVM
        Status status;
        if(!vehicle.equals("none")) {
            status = runVehicleClient(args);
        } else {
            status = runClient(testMethodExecutor.getInstance(), args);
        }

        TestResult result = switch (status.getType()) {
            case Status.PASSED -> TestResult.passed(status.getReason());
            case Status.FAILED -> TestResult.failed(new Exception(status.getReason()));
            case Status.ERROR -> TestResult.failed(new EETest.Fault(status.getReason()));
            case Status.NOT_RUN -> TestResult.skipped(status.getReason());
            default -> TestResult.failed(new IllegalStateException("Unkown status type: " + status.getType()));
        };
        result.setStart(start);
        result.setEnd(System.currentTimeMillis());
        return result;
    }

    /**
     * Run the vehicle client with the given arguments using the {@link VehicleClient} class.
     * @param args - the arguments to pass to the client
     * @return the status of the client run
     */
    Status runVehicleClient(String[] args) {
        VehicleClient client = new VehicleClient();
        return client.run(args, System.out, System.err);
    }

    /**
     * Run a non-vehicle client with the given arguments using by invoking the run method on the client instance.
     * @param client - the client instance to run
     * @param args - the arguments to pass to the client
     * @return the status of the client run
     */
    Status runClient(Object client, String[] args) {
        try {
            Class<?> baseTestClass = client.getClass();
            MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
            // Get the Status run(String[]) method from the base test class
            MethodType methodType = MethodType.methodType(Status.class, String[].class, PrintWriter.class, PrintWriter.class);
            MethodHandle run = publicLookup.findVirtual(baseTestClass, "run", methodType);
            PrintWriter out = new PrintWriter(System.out, true);
            PrintWriter err = new PrintWriter(System.err, true);
            Status status = (Status) run.invoke(client, args, out, err);
            return status;
        } catch (Throwable e) {
            return new Status(Status.ERROR, "Failed to run test client: " + e.getMessage());
        }
    }
}
