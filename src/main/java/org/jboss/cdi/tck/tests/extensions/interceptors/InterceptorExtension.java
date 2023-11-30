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
package org.jboss.cdi.tck.tests.extensions.interceptors;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import java.util.HashSet;
import java.util.Set;
import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedMethodWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

/**
 * @author Stuart Douglas stuart@baileyroberts.com.au
 * @author Martin Kouba
 */
public class InterceptorExtension implements Extension {

    /**
     * registers two interceptors via the SPI
     */
    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, final BeanManager beanManager)
            throws SecurityException, NoSuchMethodException {

        event.addInterceptorBinding(Incremented.class);
        event.addInterceptorBinding(FullMarathon.class);
        event.addInterceptorBinding(new AnnotatedTypeWrapper<Suffixed>(beanManager.createAnnotatedType(Suffixed.class), true) {
            Set<AnnotatedMethod<? super Suffixed>> methods;

            {
                methods = new HashSet<AnnotatedMethod<? super Suffixed>>();
                for (AnnotatedMethod<? super Suffixed> method : super.getMethods()) {
                    if ("value".equals(method.getJavaMember().getName())) {
                        methods.add(new AnnotatedMethodWrapper<Suffixed>((AnnotatedMethod<Suffixed>) method, this, true, new Nonbinding.Literal()));
                    } else {
                        methods.add(new AnnotatedMethodWrapper<Suffixed>((AnnotatedMethod<Suffixed>) method, this, true));
                    }
                }
            }

            @Override
            public Set<AnnotatedMethod<? super Suffixed>> getMethods() {
                return methods;
            }
        });

        new AddForwardingAnnotatedTypeAction<IncrementingInterceptor>() {
            @Override
            public String getBaseId() {
                return InterceptorExtension.class.getName();
            }

            @Override
            public AnnotatedType<IncrementingInterceptor> delegate() {
                return beanManager.createAnnotatedType(IncrementingInterceptor.class);
            }
        }.perform(event);

        new AddForwardingAnnotatedTypeAction<LifecycleInterceptor>() {
            @Override
            public String getBaseId() {
                return InterceptorExtension.class.getName();
            }

            @Override
            public AnnotatedType<LifecycleInterceptor> delegate() {
                return beanManager.createAnnotatedType(LifecycleInterceptor.class);
            }
        }.perform(event);

        new AddForwardingAnnotatedTypeAction<SuffixingInterceptor>() {
            @Override
            public String getBaseId() {
                return InterceptorExtension.class.getName();
            }

            @Override
            public AnnotatedType<SuffixingInterceptor> delegate() {
                return beanManager.createAnnotatedType(SuffixingInterceptor.class);
            }
        }.perform(event);
    }
}
