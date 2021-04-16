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

package org.jboss.cdi.tck.tests.context.session.listener;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;

import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 */
@WebListener
public class TestServletRequestListener implements ServletRequestListener {

    private static final SimpleLogger logger = new SimpleLogger(TestServletRequestListener.class);

    @Inject
    private BeanManager beanManager;

    @Inject
    private SimpleSessionBean simpleBean;

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.log("Request destroyed...");
        checkSessionContextActive();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    	logger.log("Request initialized...");
    	checkSessionContextActive();
    }

    private void checkSessionContextActive() throws IllegalStateException {
		try {
			if (!beanManager.getContext(SessionScoped.class).isActive()
					|| simpleBean == null) {
				IntrospectServlet.isSessionScopeActive = false;
			}
			// Check bean invocation
			simpleBean.getId();
		} catch (Exception e) {
			IntrospectServlet.isSessionScopeActive = false;
		}
	}

}
