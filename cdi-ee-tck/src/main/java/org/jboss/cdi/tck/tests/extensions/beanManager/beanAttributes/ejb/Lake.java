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
package org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.ejb;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Named;
import org.jboss.cdi.tck.tests.full.extensions.beanManager.beanAttributes.Fish;
import org.jboss.cdi.tck.tests.full.extensions.beanManager.beanAttributes.Natural;
import org.jboss.cdi.tck.tests.full.extensions.beanManager.beanAttributes.TundraStereotype;

@Stateless
@TundraStereotype
@Natural
public class Lake implements LakeLocal {

    @SuppressWarnings("unused")
    @ApplicationScoped
    @Natural
    @TundraStereotype
    @Typed(Fish.class)
    @Named
    private Fish fish;

    @Named
    protected long volume;

    @ApplicationScoped
    @Natural
    @TundraStereotype
    @Typed(Fish.class)
    @Named
    public Fish getFish() {
        return null;
    }

    @Named
    public long getVolume() {
        return 100;
    }
}
