package org.jboss.arquillian.protocol;

import org.jboss.arquillian.config.impl.extension.ConfigurationRegistrar;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.container.test.impl.MapObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tck.arquillian.protocol.appclient.AppClientProtocolConfiguration;

import java.io.File;
import java.util.Map;

public class AppClientConfigTest {
    @Test
    public void testFile() throws Exception {
        File dir = new File(".");
        System.out.println(dir.exists());
        System.out.println(dir.isDirectory());
        Process ls = Runtime.getRuntime().exec("/bin/ls", null, dir);
        int exit = ls.waitFor();
        System.out.println(exit);
    }
    @Test
    public void testConfig1() throws Exception {
        System.setProperty(ConfigurationRegistrar.ARQUILLIAN_XML_PROPERTY, "appclient1-arquillian.xml");
        ConfigurationRegistrar registrar = new ConfigurationRegistrar();
        ArquillianDescriptor descriptor = registrar.loadConfiguration();
        
        Assertions.assertNotNull(descriptor);
        Assertions.assertNotNull(descriptor.defaultProtocol("appclient"));
        String type = descriptor.defaultProtocol("appclient").getType();
        Assertions.assertEquals("appclient", type);

        Map<String, String> props = descriptor.defaultProtocol("appclient").getProperties();

        AppClientProtocolConfiguration config = new AppClientProtocolConfiguration();
        MapObject.populate(config, props);

        // Raw strings
        Assertions.assertEquals("-p;/home/jakartaeetck/bin/xml/../../tmp/tstest.jte", config.getClientCmdLineString());
        String expectedEnv = "JAVA_OPTS=-Djboss.modules.system.pkgs=com.sun.ts.lib,com.sun.javatest;CLASSPATH=${project.build.directory}/appclient/javatest.jar:${project.build.directory}/appclient/libutil.jar:${project.build.directory}/appclient/libcommon.jar";
        Assertions.assertEquals(expectedEnv, config.getClientEnvString());
        Assertions.assertTrue(config.isRunClient());
        Assertions.assertEquals(".", config.getClientDir());

        // Parsed strings
        String[] args = config.clientCmdLineAsArray();
        Assertions.assertEquals(2, args.length);
        Assertions.assertEquals("-p", args[0]);
        Assertions.assertEquals("/home/jakartaeetck/bin/xml/../../tmp/tstest.jte", args[1]);

        String[] envp = config.clientEnvAsArray();
        Assertions.assertEquals(2, envp.length);
        Assertions.assertTrue(envp[0].startsWith("JAVA_OPTS="));
        Assertions.assertEquals("-Djboss.modules.system.pkgs=com.sun.ts.lib,com.sun.javatest", envp[0].substring(10));
        Assertions.assertTrue(envp[1].startsWith("CLASSPATH="));
        String expectedCP = "${project.build.directory}/appclient/javatest.jar:${project.build.directory}/appclient/libutil.jar:${project.build.directory}/appclient/libcommon.jar";
        Assertions.assertEquals(expectedCP, envp[1].substring(10));
        File expectedDir = new File(".");
        Assertions.assertEquals(expectedDir, config.clientDirAsFile());
    }
}
