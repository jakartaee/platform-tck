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

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class AbstractConversationTest extends AbstractTest {

    @ArquillianResource
    protected URL contextPath;

    public static final String CID_REQUEST_PARAMETER_NAME = "cid";

    public static final String CID_HEADER_NAME = "org.jboss.cdi.tck.cid";

    public static final String LONG_RUNNING_HEADER_NAME = "org.jboss.cdi.tck.longRunning";

    protected boolean isCloudDestroyed(WebClient client) throws Exception {
        Page page = client.getPage(getConversationStatusPath("cloudDestroyed"));
        return page.getWebResponse().getStatusCode() == 200;
    }

    protected boolean isConversationContextDestroyed(WebClient client) throws Exception {
        Page page = client.getPage(getConversationStatusPath("conversationContextDestroyed"));
        return page.getWebResponse().getStatusCode() == 200;
    }

    protected void resetCloud(WebClient client) throws Exception {
        client.getPage(getConversationStatusPath("resetCloud"));
    }

    protected void resetConversationContextObserver(WebClient client) throws Exception {
        client.getPage(getConversationStatusPath("resetConversationContextObserver"));
    }

    protected void invalidateSession(WebClient client) throws Exception {
        client.getPage(getConversationStatusPath("invalidateSession"));
    }

    protected String getConversationStatusPath(String method) {
        return contextPath + "conversation-status?method=" + method;
    }

    protected String getPath(String viewId) {
        return contextPath + viewId;
    }

    protected String getPath(String viewId, String cid) {
        return contextPath + viewId + "?" + CID_REQUEST_PARAMETER_NAME + "=" + cid;
    }

    protected <T> Set<T> getElements(HtmlElement rootElement, Class<T> elementClass) {
        Set<T> result = new HashSet<T>();

        for (HtmlElement element : rootElement.getHtmlElementDescendants()) {
            result.addAll(getElements(element, elementClass));
        }

        if (elementClass.isInstance(rootElement)) {
            result.add(elementClass.cast(rootElement));
        }
        return result;

    }

    /**
     * Note that this method doesn't return the first element whose id equals specified id but the one whose id contains
     * specified id!
     * 
     * @param page
     * @param elementClass
     * @param id
     * @return the first element whose id contains specified id
     */
    protected <T extends HtmlElement> T getFirstMatchingElement(HtmlPage page, Class<T> elementClass, String id) {

        Set<T> inputs = getElements(page.getBody(), elementClass);
        for (T input : inputs) {
            if (input.getId().contains(id)) {
                return input;
            }
        }
        return null;
    }

    protected String getCid(Page page) {
        return page.getWebResponse().getResponseHeaderValue(AbstractConversationTest.CID_HEADER_NAME);
    }

    protected Boolean hasRained(Page page) {
        return Boolean.valueOf(page.getWebResponse().getResponseHeaderValue(Cloud.RAINED_HEADER_NAME));
    }

    protected Boolean isLongRunning(Page page) {
        return Boolean.valueOf(page.getWebResponse().getResponseHeaderValue(LONG_RUNNING_HEADER_NAME));
    }

}
