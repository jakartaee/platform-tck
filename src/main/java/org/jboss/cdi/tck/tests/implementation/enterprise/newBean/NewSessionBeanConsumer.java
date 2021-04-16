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
package org.jboss.cdi.tck.tests.implementation.enterprise.newBean;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

/**
 * The container discovers <code>@New</code> qualified beans by inspecting injection points other enabled beans.
 * 
 * See also CDITCK-250.
 * 
 * @author Martin Kouba
 */
public class NewSessionBeanConsumer {

    @Inject
    @New(Order.class)
    private OrderLocal order;

    @SuppressWarnings("unused")
    @Inject
    @New(Monkey.class)
    private MonkeyLocal monkey;

    @SuppressWarnings("unused")
    @Inject
    @New(Lion.class)
    private LionLocal lion;

    @SuppressWarnings("unused")
    @Inject
    @New(InitializerSimpleBean.class)
    private InitializerSimpleBeanLocal initializerSimpleBean;

    @SuppressWarnings("unused")
    @Inject
    @New(Fox.class)
    private FoxLocal fox;

    @SuppressWarnings("unused")
    @Inject
    @New(ExplicitConstructorSessionBean.class)
    private ExplicitConstructor explicitConstructor;

    @SuppressWarnings("unused")
    @Inject
    @New(WrappedEnterpriseBean.class)
    private WrappedEnterpriseBeanLocal wrappedEnterpriseBean;

    public OrderLocal getOrder() {
        return order;
    }

}
