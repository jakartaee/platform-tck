/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment.jndi;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INTERCEPTOR_ENVIRONMENT;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Matus Abaffy
 */
@RunAsClient
@SpecVersion(spec = "interceptors", version = "1.2")
public class InterceptorEnvironmentJNDITest extends AbstractTest {

    private static final String GREETING = "greeting";
    private static final String JAVA_LANG_STRING = "java.lang.String";

    private static final String HELLO = "Hello";
    private static final String BYE = "Bye";

    private static final String FOO_GET = "foo?get=";
    private static final String BAR_GET = "bar?get=";

    @Deployment(testable = false)
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .notTestArchive()
                .withClasses(MyBinding.class, MyInterceptor.class, Animal.class)
                .noDefaultWebModule().build();

        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri("test1.war")
                .contextRoot("/test1").up().up().createModule().getOrCreateWeb().webUri("test2.war").contextRoot("/test2").up()
                .up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName("test1.war")
                .withClasses(FooServlet.class, Foo.class, Dog.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName(GREETING)
                                .envEntryType(JAVA_LANG_STRING).envEntryValue(BYE).up())
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName("test2.war")
                .withClasses(BarServlet.class, Bar.class, Cat.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName(GREETING)
                                .envEntryType(JAVA_LANG_STRING).envEntryValue(HELLO).up())
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @ArquillianResource(FooServlet.class)
    URL fooContextPath;

    @ArquillianResource(BarServlet.class)
    URL barContextPath;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = INTERCEPTOR_ENVIRONMENT, id = "a")
    public void testInterceptorEnvironment() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

        WebClient webClient = new WebClient();

        // create an instance of Foo and fail if interceptor was not called
        webClient.getPage(fooContextPath + FOO_GET + "init");

        String fooAnimalName = webClient.getPage(fooContextPath + FOO_GET + "name").getWebResponse().getContentAsString();
        String fooGreeting = webClient.getPage(fooContextPath + FOO_GET + "greeting").getWebResponse().getContentAsString();

        String fooInterceptorAnimalName = webClient.getPage(fooContextPath + FOO_GET + "intName").getWebResponse()
                .getContentAsString();
        String fooInterceptorGreeting = webClient.getPage(fooContextPath + FOO_GET + "intGreeting").getWebResponse()
                .getContentAsString();

        assertEquals(fooInterceptorGreeting, fooGreeting);
        assertEquals(fooInterceptorAnimalName, fooAnimalName);
        assertEquals(fooGreeting, BYE);
        assertEquals(fooAnimalName, "Dog");

        // create an instance of Bar and fail if interceptor was not called
        webClient.getPage(barContextPath + BAR_GET + "init");

        String barAnimalName = webClient.getPage(barContextPath + BAR_GET + "name").getWebResponse().getContentAsString();
        String barGreeting = webClient.getPage(barContextPath + BAR_GET + "greeting").getWebResponse().getContentAsString();

        String barInterceptorAnimalName = webClient.getPage(barContextPath + BAR_GET + "intName").getWebResponse()
                .getContentAsString();
        String barInterceptorGreeting = webClient.getPage(barContextPath + BAR_GET + "intGreeting").getWebResponse()
                .getContentAsString();

        assertEquals(barInterceptorGreeting, barGreeting);
        assertEquals(barInterceptorAnimalName, barAnimalName);
        assertEquals(barGreeting, HELLO);
        assertEquals(barAnimalName, "Cat");
    }
}
