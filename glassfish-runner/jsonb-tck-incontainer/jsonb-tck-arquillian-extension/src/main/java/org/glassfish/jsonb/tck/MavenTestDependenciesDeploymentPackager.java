/*
 * Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.glassfish.jsonb.tck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.jboss.arquillian.container.spi.client.deployment.DeploymentDescription;
import org.jboss.arquillian.container.test.spi.client.deployment.*;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.resolver.api.maven.*;

public class MavenTestDependenciesDeploymentPackager implements DeploymentScenarioGenerator {

    private List<String> coordinatePrefixesToIgnore = null;

    private DeploymentPackageType deploymentPackageType = DeploymentPackageType.WAR;

    public MavenTestDependenciesDeploymentPackager() {
        initListOfIgnoredArtifactPrefixes();
        initDeploymentPackage();
    }

    public List<String> getGroupPrefixesToIgnore() {
        return coordinatePrefixesToIgnore;
    }

    public void setGroupPrefixesToIgnore(List<String> groupPrefixesToIgnore) {
        this.coordinatePrefixesToIgnore = groupPrefixesToIgnore;
    }

    public DeploymentPackageType getDeploymentPackageType() {
        return deploymentPackageType;
    }

    public void setDeploymentPackageType(DeploymentPackageType deploymentPackageType) {
        this.deploymentPackageType = deploymentPackageType;
    }

    private Archive<?> generateDeployment() {
        String[] activeMavenProfiles = getListOfActiveMavenProfiles();

        MavenResolvedArtifact[] resolvedArtifacts = Maven.resolver().loadPomFromFile("pom.xml", activeMavenProfiles)
                .importDependencies(ScopeType.COMPILE, ScopeType.TEST)
                .resolve().withTransitivity().asResolvedArtifact();

        DeploymentPackageType.PackageBuilder packageBuilder = deploymentPackageType.getPackageBuilder();

        Stream.of(resolvedArtifacts)
                .filter(this::artifactShouldntBeIgnored)
                .filter(artifact -> {
                    return "jar".equals(artifact.getExtension());
                })
                .map(MavenResolvedArtifact::asFile)
                .forEach(packageBuilder::addArtifact);

        return packageBuilder.build();
    }

    private String[] getListOfActiveMavenProfiles() {
        String activeMavenProfilesRawValue = System.getProperty(PropertyKeys.ACTIVE_MAVEN_PROFILES);
        if (activeMavenProfilesRawValue != null) {
            return activeMavenProfilesRawValue.split("\\s*,\\s*");
        } else {
            return new String[] {};
        }
    }

    private boolean artifactShouldntBeIgnored(MavenResolvedArtifact artifact) {
        String groupId = artifact.getCoordinate().getGroupId();
        String artifactId = artifact.getCoordinate().getArtifactId();
        String coordinates = groupId + ":" + artifactId;
        final boolean forceGroupInclude = coordinatePrefixesToIgnore.stream()
                .anyMatch(prefix -> ("!" + coordinates).startsWith(prefix));
        if (forceGroupInclude) {
            return true;
        }
        final boolean groupMatchesAPrefix = coordinatePrefixesToIgnore.stream()
                .anyMatch(prefix -> coordinates.startsWith(prefix));
        return !groupMatchesAPrefix;
    }

    @Override
    public List<DeploymentDescription> generate(TestClass testClass) {
        final DeploymentDescription defaultDeployment = new DeploymentDescription("jsonb-"
                + testClass.getName(),generateDeployment());
        return Collections.singletonList(defaultDeployment);
    }

    private void initListOfIgnoredArtifactPrefixes() {
        coordinatePrefixesToIgnore = new ArrayList<>(Arrays.asList(
                "org.jboss.shrinkwrap", // ShrinkWrap - creates a deployment, not needed in the deployment itself
                "org.codehaus.plexus", // Maven classes - not needed in the deployment itself
                "org.apache.maven"));   // Maven classes - not needed in the deployment itself
        String additionalPrefixesFromProperties = System.getProperty(PropertyKeys.GROUP_PREFIXES_TO_IGNORE);
        if (additionalPrefixesFromProperties != null) {
            List<String> prefixesFromPropertiesList = Arrays.asList(additionalPrefixesFromProperties.split("\\s*,\\s*"));
            coordinatePrefixesToIgnore.addAll(prefixesFromPropertiesList);
        }
    }

    private void initDeploymentPackage() {
        String packageValue = System.getProperty(PropertyKeys.PACKAGE);
        if (packageValue != null) {
            deploymentPackageType = DeploymentPackageType.fromString(packageValue);
        }
    }

}
