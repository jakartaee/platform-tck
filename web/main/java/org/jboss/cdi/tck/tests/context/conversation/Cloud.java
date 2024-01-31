/*
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

import java.io.Serializable;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jboss.cdi.tck.util.SimpleLogger;

@Named
@Default
@ConversationScoped
public class Cloud implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5765109971012677278L;

    private static final SimpleLogger logger = new SimpleLogger(Cloud.class);

    public static final String NAME = Cloud.class.getName() + ".Pete";

    public static final String RAINED_HEADER_NAME = Cloud.class.getName() + ".rained";

    public static final String CUMULUS = "cumulus";

    private static boolean destroyed = false;

    private boolean rained;

    private String name = NAME;

    @Inject
    Conversation conversation;

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void setDestroyed(boolean destroyed) {
        Cloud.destroyed = destroyed;
    }

    public String getName() {
        return name;
    }

    public void rain() {
        rained = true;
        logger.log("rain!");
    }

    public boolean isRained() {
        return rained;
    }

    public void cumulus() {
        this.name = CUMULUS;
        conversation.begin();
    }

}
