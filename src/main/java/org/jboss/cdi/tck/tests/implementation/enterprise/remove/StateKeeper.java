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
package org.jboss.cdi.tck.tests.implementation.enterprise.remove;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StateKeeper implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7168331316716402245L;
    private boolean removeCalled = false;
    private boolean beanDestroyed = false;

    public boolean isRemoveCalled() {
        return removeCalled;
    }

    public void setRemoveCalled(boolean removeCalled) {
        this.removeCalled = removeCalled;
    }

    public boolean isBeanDestroyed() {
        return beanDestroyed;
    }

    public void setBeanDestroyed(boolean beanDestroyed) {
        this.beanDestroyed = beanDestroyed;
    }

}
