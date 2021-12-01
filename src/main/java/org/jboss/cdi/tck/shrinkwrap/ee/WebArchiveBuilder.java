/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.shrinkwrap.ee;

import org.jboss.cdi.tck.shrinkwrap.ArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webcommon30.WebAppVersionType;

/**
 * ShrinkWrap {@link WebArchive} builder for CDI TCK Arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 *
 * @author Martin Kouba
 */
public class WebArchiveBuilder extends ArchiveBuilder<WebArchiveBuilder, WebArchive> {

    public static final String DEFAULT_WAR_NAME = "test.war";

    private boolean hasDefaultEjbModuleDependency = false;

    private String beansDescriptorTargetBase = null;

    /**
     * Add default EJB module dependency to manifest. Useful when building custom web module of enterprise archive.
     *
     * @return self
     * @see EnterpriseArchiveBuilder#DEFAULT_EJB_MODULE_NAME
     */
    public WebArchiveBuilder withDefaultEjbModuleDependency() {
        this.hasDefaultEjbModuleDependency = true;
        return this;
    }

    /**
     * Set the beans.xml descriptor target path base.
     * <p/>
     * By default the target base is <code>null</code> and the beans.xml descriptor is placed in WEB-INF dir. However CDI 1.1
     * allows an alternative location: WEB-INF/classes/META-INF.
     *
     * @param beansDescriptorTargetBase
     * @return self
     */
    public WebArchiveBuilder withBeansDescriptorTargetBase(String beansDescriptorTargetBase) {
        this.beansDescriptorTargetBase = beansDescriptorTargetBase;
        return this;
    }

    @Override
    public WebArchiveBuilder self() {
        return this;
    }

    @Override
    protected WebArchive buildInternal() {

        WebArchive webArchive = null;

        if (getName() == null) {
            // Let arquillian generate archive name in order to avoid reload issues in AS7 (AS7-1638)
            // webArchive = ShrinkWrap.create(WebArchive.class, DEFAULT_WAR_NAME);
            String hash = getSha1OfTestClass();
            if (hash != null) {
                webArchive = ShrinkWrap.create(WebArchive.class, hash + ".war");
            } else {
                webArchive = ShrinkWrap.create(WebArchive.class);
            }
        } else {
            webArchive = ShrinkWrap.create(WebArchive.class, getName());
        }

        processPackages(webArchive);
        processClasses(webArchive);
        processLibraries(webArchive);
        processManifestResources(webArchive);
        processResources(webArchive);

        // Deployment descriptors
        if (includeBeansXml) {
            webArchive.addAsWebInfResource(getBeansDescriptorAsset(), buildBeansDescriptorTargetPath(getBeansDescriptorTarget()));
        }

        if (webXmlDescriptor != null) {
            webArchive.setWebXML(new StringAsset(webXmlDescriptor.exportAsString()));
        } else if (webXml != null) {
            webArchive.setWebXML(webXml.getSource());
        } else {
            webArchive.setWebXML(new StringAsset(Descriptors.create(WebAppDescriptor.class).version(WebAppVersionType._3_0)
                    .exportAsString()));
        }

        if (persistenceDescriptor != null) {
            webArchive.addAsResource(new StringAsset(persistenceDescriptor.exportAsString()), "META-INF/persistence.xml");
        }

        if (ejbJarDescriptor != null) {
            webArchive.addAsWebInfResource(new StringAsset(ejbJarDescriptor.exportAsString()), "ejb-jar.xml");
        } else if (ejbJarXml != null) {
            webArchive.addAsWebInfResource(ejbJarXml.getSource(), ejbJarXml.getTarget());
        }

        // Web resources
        if (webResources != null) {
            for (ResourceDescriptor resource : webResources) {
                if (resource.getTarget() == null) {
                    webArchive.addAsWebResource(resource.getSource());
                } else {
                    webArchive.addAsWebResource(resource.getSource(), resource.getTarget());
                }
            }
        }

        // Default EJB module dependency
        if (this.hasDefaultEjbModuleDependency) {
            webArchive.setManifest(new StringAsset(Descriptors.create(ManifestDescriptor.class)
                    .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).exportAsString()));
        }
        return webArchive;
    }

    private String buildBeansDescriptorTargetPath(String descriptorName) {

        if (beansDescriptorTargetBase == null)
            return descriptorName;

        return beansDescriptorTargetBase + descriptorName;
    }

}
