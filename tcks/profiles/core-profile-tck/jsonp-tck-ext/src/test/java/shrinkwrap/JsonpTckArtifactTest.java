package shrinkwrap;

import java.util.Arrays;
import java.util.stream.Stream;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JsonpTckArtifactTest {
    @Disabled
    @Test
    public void testCreateJsonpTckArtifact() {
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver()
                .resolve("jakarta.json:jakarta.json-tck-tests:jar:2.1.0")
                .withTransitivity()
                .asResolvedArtifact();
        System.out.printf("resolvedArtifacts:\n");
        for(MavenResolvedArtifact mra : resolvedArtifacts) {
            System.out.printf("\t%s:.%s:%s;%s[%s]\n", mra.getCoordinate(), mra.getResolvedVersion(),
                              mra.getExtension(), mra.getScope(), Arrays.asList(mra.getDependencies()));
        }

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsonp-tck-tests-all.war").as(WebArchive.class);
        Stream.of(resolvedArtifacts)
                .filter(artifact -> {
                    return "jar".equals(artifact.getExtension());
                })
                .map(MavenResolvedArtifact::asFile)
                .forEach(archive::addAsLibrary);

        System.out.println(archive.toString(true));
    }

    @Disabled
    @Test
    public void testCreateJsonpTckArtifactAll() {
        String[] activeMavenProfiles = {"staging"};
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .importDependencies(ScopeType.COMPILE, ScopeType.TEST)
                .resolve()
                .withTransitivity()
                .asResolvedArtifact();
        System.out.printf("resolvedArtifacts:\n");
        for(MavenResolvedArtifact mra : resolvedArtifacts) {
            System.out.printf("\t%s:.%s:%s;%s[%s]\n", mra.getCoordinate(), mra.getResolvedVersion(),
                              mra.getExtension(), mra.getScope(), Arrays.asList(mra.getDependencies()));
        }

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsonp-tck-tests-all.war").as(WebArchive.class);
        Stream.of(resolvedArtifacts)
                .filter(artifact -> {
                    return "jar".equals(artifact.getExtension());
                })
                .map(MavenResolvedArtifact::asFile)
                .forEach(archive::addAsLibrary);

        System.out.println(archive.toString(true));
    }

    @Disabled("Testing how to externalize version...")
    @Test()
    public void testCreateJsonpTckArtifactImplicitVersion() {
        String[] activeMavenProfiles = {};
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .resolve("jakarta.json:jakarta.json-tck-tests:jar:${jsonp.api.version}")
                .withTransitivity()
                .asResolvedArtifact();
        System.out.printf("resolvedArtifacts:\n");
        for(MavenResolvedArtifact mra : resolvedArtifacts) {
            System.out.printf("\t%s:.%s:%s;%s[%s]\n", mra.getCoordinate(), mra.getResolvedVersion(),
                              mra.getExtension(), mra.getScope(), Arrays.asList(mra.getDependencies()));
        }

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsonp-tck-tests-all.war").as(WebArchive.class);
        Stream.of(resolvedArtifacts)
                .filter(artifact -> {
                    return "jar".equals(artifact.getExtension());
                })
                .map(MavenResolvedArtifact::asFile)
                .forEach(archive::addAsLibrary);

        System.out.println(archive.toString(true));
    }
}
