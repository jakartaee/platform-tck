/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.application.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Collector {

    //using synchronized collection - see WELD-1673
    private List<Helper> contextPaths = Collections.synchronizedList(new ArrayList<Helper>());

    public Collector() {

    }

    public List<Helper> getContextPaths() {
        return contextPaths;
    }

    public void addContextPath(Helper helper) {
        contextPaths.add(helper);
    }

    public Helper getByClassName(String className) {
        Helper helper = null;
        for (Helper val : contextPaths) {
            if (val.getClassName().equals(className))
                helper = val;
        }
        return helper;
    }

}
