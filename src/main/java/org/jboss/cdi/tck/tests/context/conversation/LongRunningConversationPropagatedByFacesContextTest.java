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
package org.jboss.cdi.tck.tests.context.conversation;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_SCOPES;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Duration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.selenium.DriverPool;
import org.jboss.cdi.tck.selenium.ExtendedWebDriver;
import org.jboss.cdi.tck.selenium.WebPage;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class LongRunningConversationPropagatedByFacesContextTest extends AbstractConversationTest {

    private static final String STORM_STRENGTH = "12";
    private static final String REDIRECT_STORM_STRENGTH = "15";
    private static final String AJAX_STORM_STRENGTH = "20";

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassDefinition(LongRunningConversationPropagatedByFacesContextTest.class)
                .withClasses(Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class,
                        OutermostFilter.class).withWebResource("storm.xhtml").withWebResource("storm-ajax.xhtml")
                .withWebResource("thunder.xhtml").withWebResource("lightening.xhtml")
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml").withWebXml("web.xml").build();
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "l")
    @SpecAssertion(section = BUILTIN_SCOPES, id = "ba")
    public void testConversationPropagated() throws Exception {

        WebClient webClient = new WebClient();

        HtmlPage storm = webClient.getPage(getPath("storm.jsf"));

        // Begin long-running conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class,
                "beginConversationButton");
        storm = beginConversationButton.click();
        // Set input value
        HtmlTextInput stormStrength = getFirstMatchingElement(storm, HtmlTextInput.class, "stormStrength");
        stormStrength.setValueAttribute(STORM_STRENGTH);
        String stormCid = getCid(storm);
        // Submit value and forward to the next form
        HtmlSubmitInput thunderButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "thunderButton");
        HtmlPage thunder = thunderButton.click();

        assertEquals(stormCid, getCid(thunder));
        stormStrength = getFirstMatchingElement(thunder, HtmlTextInput.class, "stormStrength");
        assertEquals(stormStrength.getValueAttribute(), STORM_STRENGTH);
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "m")
    public void testConversationPropagatedOverRedirect() throws Exception {

        WebClient webClient = new WebClient();

        HtmlPage storm = webClient.getPage(getPath("storm.jsf"));

        // Begin long-running conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class,
                "beginConversationButton");
        storm = beginConversationButton.click();
        // Set input value
        HtmlTextInput stormStrength = getFirstMatchingElement(storm, HtmlTextInput.class, "stormStrength");
        stormStrength.setValueAttribute(REDIRECT_STORM_STRENGTH);
        String stormCid = getCid(storm);
        // Submit value and redirect to the next form
        HtmlSubmitInput lighteningButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "lighteningButton");
        HtmlPage lightening = lighteningButton.click();

        assertTrue(lightening.getWebResponse().getWebRequest().getUrl().toString().contains("lightening.jsf"));
        assertEquals(stormCid, getCid(lightening));
        stormStrength = getFirstMatchingElement(lightening, HtmlTextInput.class, "stormStrength");
        assertEquals(stormStrength.getValueAttribute(), REDIRECT_STORM_STRENGTH);
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "l")
    public void testConversationPropagatedAjax() throws Exception {

        String selenium = System.getProperty("run.selenium", "false");
        if ("true".equals(selenium)) {
            DriverPool driverPool = new DriverPool();
            ExtendedWebDriver webDriver = driverPool.getOrNewInstance();

            webDriver.get(getPath("storm-ajax.jsf"));
            WebPage page = new WebPage(webDriver);
            page.waitForPageToLoad(Duration.ofSeconds(120));

            WebElement beginConversationButton = page.findElement(By.id("ajaxForm:beginConversationButton"));
            assertNotNull(beginConversationButton);
            beginConversationButton.click();

            page.waitReqJs();

            WebElement conversationId = page.findElement(By.id("ajaxForm:conversationId"));
            String cid = conversationId.getAttribute("value");
            assertFalse(cid.isEmpty());

            WebElement stormStrength = page.findElement(By.id("ajaxForm:stormStrength"));
            stormStrength.sendKeys(AJAX_STORM_STRENGTH);
            WebElement thunderButton = page.findElement(By.id("ajaxForm:thunderButton"));
            assertNotNull(thunderButton);
            thunderButton.click();

            page.waitReqJs();

            webDriver.get(getPath("thunder.jsf", cid));
            WebPage thunderPage = new WebPage(webDriver);
            thunderPage.waitForPageToLoad(Duration.ofSeconds(120));

            stormStrength = thunderPage.findElement(By.id("form:stormStrength"));
            assertEquals(stormStrength.getAttribute("value"), AJAX_STORM_STRENGTH);

            driverPool.returnInstance(webDriver);
            driverPool.quitAll();
        } else {
            WebClient webClient = new WebClient();
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());

            HtmlPage storm = webClient.getPage(getPath("storm-ajax.jsf"));

            // Begin long-running conversation - note that we use ajax
            HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class,
                    "beginConversationButton");
            storm = beginConversationButton.click();
            String cid = getFirstMatchingElement(storm, HtmlInput.class, "conversationId").getValueAttribute();
            assertFalse(cid.isEmpty());

            // Set input value
            HtmlTextInput stormStrength = getFirstMatchingElement(storm, HtmlTextInput.class, "stormStrength");
            stormStrength.setValueAttribute(AJAX_STORM_STRENGTH);
            // Submit value - note that we use ajax
            HtmlSubmitInput thunderButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "thunderButton");
            thunderButton.click();

            HtmlPage thunder = webClient.getPage(getPath("thunder.jsf", cid));
            stormStrength = getFirstMatchingElement(thunder, HtmlTextInput.class, "stormStrength");
            assertEquals(stormStrength.getValueAttribute(), AJAX_STORM_STRENGTH);
	    }
    }

}
