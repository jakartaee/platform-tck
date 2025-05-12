/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.glassfish.persistence.tck;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 *
 * @author omihalyi
 */
public enum DeploymentPackageType {

    WAR {
        @Override
        protected PackageBuilder getPackageBuilder(Archive<?> archive) {
            return new WarPackageBuilder(archive);
        }
    }, EAR {
        @Override
        protected PackageBuilder getPackageBuilder(Archive<?> archive) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public static DeploymentPackageType fromString(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Remove special chars, e.g. ejb-jar is turned into EJBJAR
        return DeploymentPackageType.valueOf(value.toUpperCase().replaceAll("-|_|\\.| ", ""));
    }

    public static DeploymentPackageType fromArchive(Archive<?> archive) {
        if (archive == null) {
            return null;
        }

        if (archive instanceof WebArchive) {
            return WAR;
        }

        if (archive instanceof EnterpriseArchive) {
            return EAR;
        }

        throw new RuntimeException("Unsupported archive type: " + archive.getClass());
    }

    protected PackageBuilder getPackageBuilder() {
        return getPackageBuilder(null);
    }

    // create builder from an existing archive - modifies the existing archive
    protected abstract PackageBuilder getPackageBuilder(Archive<?> archive);

    interface PackageBuilder {

        PackageBuilder addArtifact(File artifactFile);

        PackageBuilder addResource(Asset resourceAsset, String resourceName);

        PackageBuilder addClass(Class<?> cls);

        Archive<?> build();
    }

    class WarPackageBuilder implements PackageBuilder {

        WebArchive archive;

        public WarPackageBuilder(Archive<?> archive) {
            if (archive == null) {
                this.archive = ShrinkWrap.create(WebArchive.class, "package.war").as(WebArchive.class);
            } else {
                this.archive = archive.as(WebArchive.class);
            }
        }

        @Override
        public WarPackageBuilder addArtifact(File artifactFile) {
            final JavaArchive artifactArchive = ShrinkWrap.createFromZipFile(JavaArchive.class, artifactFile);
            artifactArchive.delete("/com/sun/ts/tests/jms/commonee/MDB_Q_TestEJB.class");
            artifactArchive.delete("/com/sun/ts/tests/jms/commonee/MDB_T_TestEJB.class");
            archive.addAsLibrary(artifactArchive);
            return this;
        }

        @Override
        public WarPackageBuilder addResource(Asset resourceAsset, String resourceName) {
            archive.addAsResource(resourceAsset, resourceName);
            return this;
        }

        @Override
        public WarPackageBuilder addClass(Class<?> cls) {
            archive.addClass(cls);
            return this;
        }

        @Override
        public Archive<?> build() {
            return archive;
        }
    }

}
