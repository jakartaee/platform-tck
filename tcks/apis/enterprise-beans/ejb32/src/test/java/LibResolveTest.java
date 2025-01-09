import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenFormatStage;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.jupiter.api.Test;

public class LibResolveTest {
    @Test
    public void resolveCommonsLib() {
        String[] activeMavenProfiles = {"staging"};
        MavenFormatStage mavenFormatStage = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .resolve("org.apache.commons:commons-lang3")
                .withoutTransitivity();
        MavenResolvedArtifact resolvedArtifacts = mavenFormatStage.asSingleResolvedArtifact();
        System.out.println("Resolved artifact: " + resolvedArtifacts.asFile());
    }
}
