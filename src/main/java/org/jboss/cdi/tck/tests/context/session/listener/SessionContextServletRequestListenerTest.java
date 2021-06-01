/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.session.listener;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_CONTEXT_EE;
import static org.testng.Assert.assertTrue;

import java.net.URL;

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

@SpecVersion(spec = "cdi", version = "2.0")
public class SessionContextServletRequestListenerTest extends AbstractTest {

	@ArquillianResource
	private URL contextPath;

	@Deployment(testable = false)
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassDefinition(SessionContextServletRequestListenerTest.class)
				.withClasses(SimpleSessionBean.class, IntrospectServlet.class, TestServletRequestListener.class)
				.build();
	}

	@Test(groups = INTEGRATION)
	@SpecAssertion(section = SESSION_CONTEXT_EE, id = "ae")
	public void testSessionScopeActiveDuringServletRequestListenerCall() throws Exception {
		WebClient webClient = new WebClient();
		// Create session
		webClient.getPage(contextPath + "introspect");
		// Invalidate session
		webClient.getPage(contextPath + "introspect?mode=invalidate");
		// Verify result
		TextPage page = webClient.getPage(contextPath + "introspect?mode=verify");
		assertTrue(Boolean.valueOf(page.getContent()));
	}

}
