package test.com.sun.ts.tests.common.vehicle.none.proxy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class MyAppMainTest {
    @Test
    public void testMain() throws IOException {
        Properties props = new Properties();
        props.setProperty("webHostServer", "localhost");
        props.setProperty("webHostPort", "8191");
        Path tmpPath = Files.createTempFile("ts", ".jte");
        props.store(Files.newBufferedWriter(tmpPath), "MyAppMainTest.testMain");
        String[] args = {"-x", "-t", "test1", "-p", tmpPath.toAbsolutePath().toString()};
        MyAppMain.main(args);
    }
}
