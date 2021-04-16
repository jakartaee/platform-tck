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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.ejb;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.Bravo;
import org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.BravoDecorator;
import org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.BravoInterceptor;

public class VerifyingExtension implements Extension {

    private int alphaAttributesObserved = 0;
    private BeanAttributes<Bravo> producedBravoAttributes;
    private BeanAttributes<BravoInterceptor> bravoInterceptorAttributes;
    private BeanAttributes<BravoDecorator> bravoDecoratorAttributes;
    private BeanAttributes<Delta> deltaAttributes;

    private Map<Class<?>, Annotated> annotatedMap = new HashMap<Class<?>, Annotated>();

    void observeBravo(@Observes ProcessBeanAttributes<Bravo> event) {
        if (event.getAnnotated() instanceof AnnotatedMethod) {
            // We only want producer method
            producedBravoAttributes = event.getBeanAttributes();
            annotatedMap.put(Bravo.class, event.getAnnotated());
        }
    }

    void observeBravoInterceptor(@Observes ProcessBeanAttributes<BravoInterceptor> event) {
        bravoInterceptorAttributes = event.getBeanAttributes();
    }

    void observeBravoDecorator(@Observes ProcessBeanAttributes<BravoDecorator> event) {
        bravoDecoratorAttributes = event.getBeanAttributes();
    }

    void observeDelta(@Observes ProcessBeanAttributes<Delta> event) {
        deltaAttributes = event.getBeanAttributes();
        annotatedMap.put(Delta.class, event.getAnnotated());
    }

    public int getAlphaAttributesObserved() {
        return alphaAttributesObserved;
    }

    protected BeanAttributes<Bravo> getProducedBravoAttributes() {
        return producedBravoAttributes;
    }

    public BeanAttributes<BravoInterceptor> getBravoInterceptorAttributes() {
        return bravoInterceptorAttributes;
    }

    public BeanAttributes<BravoDecorator> getBravoDecoratorAttributes() {
        return bravoDecoratorAttributes;
    }


    public BeanAttributes<Delta> getDeltaAttributes() {
        return deltaAttributes;
    }

    public Map<Class<?>, Annotated> getAnnotatedMap() {
        return annotatedMap;
    }

}
