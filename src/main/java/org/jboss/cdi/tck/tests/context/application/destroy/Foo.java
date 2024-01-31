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

package org.jboss.cdi.tck.tests.context.application.destroy;

import java.net.URL;
import java.net.URLConnection;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.cdi.tck.util.SimpleLogger;

@ApplicationScoped
public class Foo {

    private SimpleLogger logger = new SimpleLogger(Foo.class);

    private String barContext = null;

    public void setBarContext(String barContextUrl) {
        this.barContext = barContextUrl;
    }

    @PreDestroy
    public void destroy() {
        try {
            URLConnection connection = new URL(barContext + "info" + "?action=fooDestroyed").openConnection();
            connection.getInputStream().close();
            logger.log("Test flag set [{0}]", barContext);
        } catch (Exception e) {
            logger.log("Cannot set test flag: {0}", e);
        }
    }

}
