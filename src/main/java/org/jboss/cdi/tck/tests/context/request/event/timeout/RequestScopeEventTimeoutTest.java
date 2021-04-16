/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.context.request.event.timeout;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Martin Kouba
 * 
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestScopeEventTimeoutTest extends AbstractTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestScopeEventTimeoutTest.class).build();
    }

    @ArquillianResource
    private URL contextPath;

    @Test
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "je")
    public void testEventsFired() throws Exception {

        WebClient client = new WebClient();

        TextPage page = client.getPage(contextPath + "info");
        String content = page.getContent();

        // Test request and timeout request
        checkContent(content, "(Initialized:)(\\w+)", "true");
        // Timeout request only
        checkContent(content, "(Destroyed:)(\\w+)", "true");
    }

    private void checkContent(String content, String pattern, String expected) {
        Matcher matcher = Pattern.compile(pattern).matcher(content);
        if (matcher.find()) {
            String value = matcher.group(2);
            assertEquals(value, expected);
        } else {
            fail();
        }
    }

}
