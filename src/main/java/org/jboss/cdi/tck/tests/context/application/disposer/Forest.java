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

package org.jboss.cdi.tck.tests.context.application.disposer;

import jakarta.enterprise.context.ApplicationScoped;

import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 */
@ApplicationScoped
public class Forest {

    private static final SimpleLogger logger = new SimpleLogger(Forest.class);

    private final long id = System.currentTimeMillis();

    private boolean empty = false;

    public void setEmpty() {
        empty = true;
        logger.log("Set empty - {0}", this.toString());
    }

    public boolean isEmpty() {
        return empty;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Forest [id=" + id + ", empty=" + empty + "]";
    }

}
