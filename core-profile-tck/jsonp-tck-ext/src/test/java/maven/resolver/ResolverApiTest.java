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

package maven.resolver;

import java.util.Arrays;

import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.jupiter.api.Test;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class ResolverApiTest {
    @Test
    public void testResolveAllTests() {
        String[] activeMavenProfiles = Utils.getListOfActiveMavenProfiles();

        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .importDependencies(ScopeType.COMPILE, ScopeType.TEST)
                .resolve().withTransitivity().asResolvedArtifact();
        System.out.printf("resolvedArtifacts:\n");
        for(MavenResolvedArtifact mra : resolvedArtifacts) {
            System.out.printf("\t%s:.%s:%s;%s\n", mra.getCoordinate(), mra.getResolvedVersion(), mra.getExtension(), mra.getScope());
        }
    }

    @Test
    public void testResolveJsonpTckTests() {
        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver()
                .resolve("jakarta.json:jakarta.json-tck-tests:jar:2.1.0")
                .withTransitivity()
                .asResolvedArtifact();
        System.out.printf("resolvedArtifacts:\n");
        for(MavenResolvedArtifact mra : resolvedArtifacts) {
            System.out.printf("\t%s:.%s:%s;%s[%s]\n", mra.getCoordinate(), mra.getResolvedVersion(),
                              mra.getExtension(), mra.getScope(), Arrays.asList(mra.getDependencies()));
        }
    }

}
