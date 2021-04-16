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
package org.jboss.cdi.tck.tests.context.session;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

@SpecVersion(spec = "cdi", version = "2.0")
public class SessionContextTest extends AbstractTest {

	private static final long DEFAULT_SLEEP_INTERVAL = 3000;

	@ArquillianResource
	private URL contextPath;

	@Deployment(testable = false)
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage(SessionContextTest.class)
				.withWebResource("SimplePage.html", "SimplePage.html").build();
	}

	@Test(groups = INTEGRATION)
	@SpecAssertions({ @SpecAssertion(section = SESSION_CONTEXT_EE, id = "aa"),
			@SpecAssertion(section = SESSION_CONTEXT_EE, id = "ae") })
	public void testSessionScopeActiveDuringServiceMethod() throws Exception {
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(true);
		webClient.getPage(contextPath + "test");
	}

	@Test(groups = INTEGRATION)
	@SpecAssertions({ @SpecAssertion(section = SESSION_CONTEXT_EE, id = "ab"),
			@SpecAssertion(section = SESSION_CONTEXT_EE, id = "ae") })
	public void testSessionScopeActiveDuringDoFilterMethod() throws Exception {
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(true);
		webClient.getPage(contextPath + "SimplePage.html");
	}

	@Test(groups = INTEGRATION)
	@SpecAssertion(section = SESSION_CONTEXT_EE, id = "b")
	public void testSessionContextSharedBetweenServletRequestsInSameHttpSession()
			throws Exception {
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(true);
		TextPage firstRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(firstRequestResult.getContent());
		String sessionBeanId = firstRequestResult.getContent();
		assertNotNull(sessionBeanId);
		// Make a second request and make sure the same context is used
		TextPage secondRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(secondRequestResult.getContent());
		assertEquals(secondRequestResult.getContent(), sessionBeanId);
	}

	/**
	 * Test that the session context is destroyed at the very end of any request
	 * in which invalidate() was called, after all filters and
	 * ServletRequestListeners have been called.
	 *
	 * @throws Exception
	 */
	@Test(groups = INTEGRATION)
	@SpecAssertion(section = SESSION_CONTEXT_EE, id = "ca")
	public void testSessionContextDestroyedWhenHttpSessionInvalidated()
			throws Exception {
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(true);

		TextPage firstRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(firstRequestResult.getContent());
		String sessionBeanId = firstRequestResult.getContent();
		// Invalidate the session
		webClient.getPage(contextPath + "introspect?mode=invalidate");
		// Make a second request and make sure the same context is not there
		TextPage secondRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(secondRequestResult.getContent());
		assertNotEquals(secondRequestResult.getContent(), sessionBeanId);

		// Verify context is destroyed after all filters and
		// ServletRequestListeners
		TextPage verifyResult = webClient.getPage(contextPath
				+ "introspect?mode=verify");
		ActionSequence correctSequence = new ActionSequence()
				.add(IntrospectServlet.class.getName())
				.add(IntrospectHttpSessionListener.class.getName())
				.add(IntrospectFilter.class.getName())
				.add(IntrospectServletRequestListener.class.getName())
				.add(SimpleSessionBean.class.getName());
		assertEquals(verifyResult.getContent(), correctSequence.toString());
	}

	/**
	 * The session context is destroyed when the HTTPSession times out, after
	 * all HttpSessionListeners have been called.
	 *
	 * @throws Exception
	 */
	@Test(groups = INTEGRATION)
	@SpecAssertion(section = SESSION_CONTEXT_EE, id = "cb")
	public void testSessionContextDestroyedWhenHttpSessionTimesOut()
			throws Exception {
		WebClient webClient = new WebClient();
		webClient.setThrowExceptionOnFailingStatusCode(true);
		TextPage firstRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(firstRequestResult.getContent());
		String sessionBeanId = firstRequestResult.getContent();
		assertNotNull(sessionBeanId);

		webClient.getPage(contextPath + "introspect?mode=timeout");
		Timer.startNew(DEFAULT_SLEEP_INTERVAL);

		// Make a second request and make sure the same context is not there
		TextPage secondRequestResult = webClient.getPage(contextPath
				+ "introspect");
		assertNotNull(secondRequestResult.getContent());
		assertNotEquals(secondRequestResult.getContent(), sessionBeanId);

		// Verify context is destroyed after all filters and
		// ServletRequestListeners
		TextPage verifyResult = webClient.getPage(contextPath
				+ "introspect?mode=verify");
		ActionSequence correctSequence = new ActionSequence()
				.add(IntrospectHttpSessionListener.class.getName())
				.add(SimpleSessionBean.class.getName());
		assertEquals(verifyResult.getContent(), correctSequence.toString());
	}

}
