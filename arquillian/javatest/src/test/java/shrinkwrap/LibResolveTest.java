package shrinkwrap;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;
import org.junit.jupiter.api.Test;
import tck.arquillian.protocol.javatest.JavaTestDeploymentPackager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LibResolveTest {
    @Test
    public void testVersionInfo() throws IOException {
        URL versionURL = JavaTestDeploymentPackager.class.getResource("/javatest.version");
        System.out.println("Version URL: " + versionURL);
        try(InputStream is = versionURL.openStream()) {
            byte[] info = is.readAllBytes();
            System.out.println("Version info: " + new String(info));
        }
    }

    @Test
    public void resolveProtocolLib() {
        String[] activeMavenProfiles = {"staging"};
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .resolve("jakarta.tck.arquillian:arquillian-protocol-lib")
                .withTransitivity()
                .asResolvedArtifact();
        MavenResolvedArtifact protocolLib = null;
        for (MavenResolvedArtifact resolvedArtifact : resolvedArtifacts) {
            MavenCoordinate gav = resolvedArtifact.getCoordinate();
            if(gav.getGroupId().equals("jakarta.tck.arquillian") && gav.getArtifactId().equals("arquillian-protocol-lib")) {
                protocolLib = resolvedArtifact;
                break;
            }
        }
        System.out.println("Resolved artifact: " + protocolLib);
    }
}
