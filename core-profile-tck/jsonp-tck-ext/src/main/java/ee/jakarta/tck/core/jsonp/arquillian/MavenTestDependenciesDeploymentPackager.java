/*
 * Copyright (c) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package ee.jakarta.tck.core.jsonp.arquillian;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentScenarioGenerator;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

/**
 * Ideas copied from the Jakarta Batch MavenTestDependenciesDeploymentPackager to package up
 * the test and compile scope artifacts in the current pom.xml. Exclude artifacts like the
 * Jakarta EE artifacts by declaring them with provided scope.
 */
public class MavenTestDependenciesDeploymentPackager implements DeploymentScenarioGenerator {

    @Override
    public List<DeploymentDescription> generate(TestClass testClass) {
        final WebArchive archive = generateDeployment();
        final DeploymentDescription deployment = new DeploymentDescription("jsonp-tck-tests",
                                                                                  archive);
        return Collections.singletonList(deployment);
    }

    /**
     * This resolves all test and compile scope artifacts in the project pom.xml and includes them as
     * libraries in a jsonp-tck-tests-all.war
     *
     * @return shrinkwrap web archive for the test artifacts
     */
    private WebArchive generateDeployment() {
        String[] activeMavenProfiles = {"staging"};
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .importDependencies(ScopeType.COMPILE, ScopeType.TEST)
                .resolve()
                .withTransitivity()
                .asResolvedArtifact();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsonp-tck-tests-all.war").as(WebArchive.class);
        Stream.of(resolvedArtifacts)
                .filter(artifact -> {
                    return "jar".equals(artifact.getExtension());
                })
                .map(MavenResolvedArtifact::asFile)
                .forEach(archive::addAsLibrary);
        System.out.println(archive.toString(true));
        return archive;
    }
}
