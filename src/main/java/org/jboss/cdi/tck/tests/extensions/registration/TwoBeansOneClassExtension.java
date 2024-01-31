/*
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.registration;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

// This test extension expects that AddForwardingAnnotatedTypeAction distinguishes
// annotated types by more that just the baseId and delegate class
public class TwoBeansOneClassExtension implements Extension {
    public void registerBeans(@Observes BeforeBeanDiscovery event, final BeanManager bm) {

        // add basic Beanie
        new AddForwardingAnnotatedTypeAction<Beanie>() {

            final AnnotatedType<Beanie> delegate = bm.createAnnotatedType(Beanie.class);

            @Override
            public String getBaseId() {
                return "basic";
            }

            @Override
            public AnnotatedType<Beanie> delegate() {
                return delegate;
            }
        }.perform(event);

        // add @BeanieType("propeller")Beanie
        new AddForwardingAnnotatedTypeAction<Beanie>() {

            final AnnotatedType<Beanie> delegate = new AnnotatedTypeWrapper<Beanie>(bm.createAnnotatedType(Beanie.class), false, new BeanieTypeLiteral() {

                @Override
                public String value() {
                    return "propeller";
                }
            });

            @Override
            public String getBaseId() {
                return "propeller";
            }

            @Override
            public AnnotatedType<Beanie> delegate() {
                return delegate;
            }
        }.perform(event);
    }
}
