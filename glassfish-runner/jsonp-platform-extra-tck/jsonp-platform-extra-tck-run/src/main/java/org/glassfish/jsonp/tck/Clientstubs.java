package org.glassfish.jsonp.tck;

import java.nio.charset.StandardCharsets;

public class Clientstubs {

    public static void main(String... args) throws Exception {

        String javaHome = System.getProperty("java.home");

        String[] envp = new String[] { "JAVA_HOME=" + javaHome };

        // asadmin get-client-stubs --appName assembly_altDD target

        String[] cmdarray = {
            args[0] + "/asadmin",
            "get-client-stubs",
            "--appName",
            args[1],
            "target"
        };

        Process process = Runtime.getRuntime().exec(cmdarray, envp);
        int exitCode = process.waitFor();

        System.out.println(new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        System.out.println(new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8));


        System.exit(exitCode);
    }

}