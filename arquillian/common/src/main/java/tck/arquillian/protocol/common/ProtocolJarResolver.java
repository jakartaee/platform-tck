package tck.arquillian.protocol.common;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class ProtocolJarResolver {
    static Logger log = Logger.getLogger(ProtocolJarResolver.class.getName());

    /**
     * Resolve the protocol.jar from the runner pom.xml dependencies
     * @return The protocol.jar file if found, null otherwise
     */
    public static File resolveProtocolJar() {
        File protocolJar = null;
        String[] activeMavenProfiles = {"staging"};
        String libGAV = "jakarta.tck.arquillian:arquillian-protocol-lib";
        String version = versionInfo();
        if(version != null && !version.isEmpty()) {
            libGAV += ":" + version;
        }
        MavenResolvedArtifact protocolLib = null;
        try {
            MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                    .resolve(libGAV)
                    .withTransitivity()
                    .asResolvedArtifact();
            for (MavenResolvedArtifact resolvedArtifact : resolvedArtifacts) {
                MavenCoordinate gav = resolvedArtifact.getCoordinate();
                if (gav.getGroupId().equals("jakarta.tck.arquillian") && gav.getArtifactId().equals("arquillian-protocol-lib")) {
                    protocolLib = resolvedArtifact;
                    break;
                }
            }
        } catch (Exception e) {
            log.warning("Failed to resolve jakarta.tck.arquillian:arquillian-protocol-lib: " + e.getMessage());
        }

        if(protocolLib != null) {
            protocolJar = protocolLib.asFile();
        } else {
            log.warning("Failed to resolve jakarta.tck.arquillian:arquillian-protocol-lib, check the runner pom.xml dependencies");
            // Fallback to the local unpacked protocol.jar
            protocolJar = new File("target/protocol/protocol.jar");
            if(!protocolJar.exists()) {
                log.warning("Failed to find downloaded jakarta.tck.arquillian:arquillian-protocol-lib in target/protocol/protocol.jar");
                protocolJar = null;
            }
        }
        return protocolJar;
    }

    /**
     * Read the javatest.version file to get the version of the protocol.jar
     * @return
     */
    public static String versionInfo() {
        URL versionURL = ProtocolJarResolver.class.getResource("/javatest.version");
        String versionInfo = "";
        try {
            assert versionURL != null;
            try(InputStream is = versionURL.openStream()) {
                if(is != null) {
                    byte[] info = is.readAllBytes();
                    versionInfo = new String(info);
                }
            }
        } catch (Exception e) {
            log.warning("Failed to read javatest.version: " + e.getMessage());
        }
        return versionInfo;
    }

}
