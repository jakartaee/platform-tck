/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.discovery.implicit;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;
import jakarta.enterprise.inject.spi.ProcessProducerField;
import jakarta.enterprise.inject.spi.ProcessProducerMethod;

public class TestExtension implements Extension {
    
    public static AtomicInteger processProducerMethodCounter = new AtomicInteger();
    public static AtomicInteger processProducerFieldCounter = new AtomicInteger();
    public static AtomicInteger processObserverCounter = new AtomicInteger();
    public static AnnotatedParameter<ProducedBean> disposerParam;
    
    public void processProducerMethod(@Observes ProcessProducerMethod<ProducedBean, NotDiscoveredBean> event){
        processProducerMethodCounter.incrementAndGet();
        disposerParam = event.getAnnotatedDisposedParameter();
    }

    public void processProducerField(@Observes ProcessProducerField<ProducedBean, NotDiscoveredBean> event){
        processProducerFieldCounter.incrementAndGet();
    }

    public void processObserverMethod(@Observes ProcessObserverMethod<ProducedBean, NotDiscoveredBean> event){
        processObserverCounter.incrementAndGet();
    }      
}
