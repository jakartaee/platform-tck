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

package org.jboss.cdi.tck.tests.deployment.shutdown;

import java.net.URL;
import java.net.URLConnection;

import org.jboss.cdi.tck.util.SimpleLogger;

public final class InfoClient {

    private static final SimpleLogger logger = new SimpleLogger(InfoClient.class);

    private static String infoContext = null;

    public static void setInfoContext(String infoContext) {
        InfoClient.infoContext = infoContext;
    }

    public static void doGetInfo(String action) {
        try {
            URLConnection connection = new URL(infoContext + "info" + "?action=add&id=" + action).openConnection();
            connection.getInputStream().close();
            logger.log("Add action [{0}]", infoContext);
        } catch (Exception e) {
            logger.log("Cannot add action {0}: {1}", action, e);
        }
    }
}
