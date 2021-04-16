/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.test.porting;

import jakarta.enterprise.context.spi.Context;
import org.jboss.cdi.tck.spi.Contexts;

public class DummyContexts implements Contexts<Context> {

    @Override
    public void setActive(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInactive(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getRequestContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Context getDependentContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroyContext(Context context) {
        throw new UnsupportedOperationException();
    }

}
