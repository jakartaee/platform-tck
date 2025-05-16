import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

public class LibResolveTest {
    @Test
    public void resolveCommonsLib() {
        /* Obsolete, just add the classes to the archive
        String[] activeMavenProfiles = {"staging"};
        MavenFormatStage mavenFormatStage = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .resolve("org.apache.commons:commons-lang3")
                .withoutTransitivity();
        MavenResolvedArtifact resolvedArtifacts = mavenFormatStage.asSingleResolvedArtifact();
        System.out.println("Resolved artifact: " + resolvedArtifacts.asFile());

         */
        JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "commons-lang3.jar");
        lib.addClasses(org.apache.commons.lang3.StringUtils.class,
                org.apache.commons.lang3.time.DateUtils.class);
        System.out.println(lib.toString(true));
    }
}
