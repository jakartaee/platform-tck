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

package org.jboss.cdi.tck.tests.context.session.listener.shutdown;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import jakarta.enterprise.context.SessionScoped;

import org.jboss.cdi.tck.util.SimpleLogger;

@SuppressWarnings("serial")
@SessionScoped
public class SessionScopedTestFlagClient implements Serializable {

    private SimpleLogger logger = new SimpleLogger(SessionScopedTestFlagClient.class);

    private String bravoWebappContext = null;

    public void setBravoWebappContext(String bravoContextUrl) {
        this.bravoWebappContext = bravoContextUrl;
        logger.log("Test flag client initialized [{0}]", bravoContextUrl);
    }

    public void setTestFlag() {
        try {
            URLConnection connection = new URL(bravoWebappContext + "info" + "?action=set").openConnection();
            connection.getInputStream().close();
            logger.log("Test flag set [{0}]", bravoWebappContext);
        } catch (Exception e) {
            logger.log("Cannot set test flag: {0}", e);
        }
    }

}
