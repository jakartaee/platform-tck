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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

@Any
public class TestTagHandler extends SimpleTagSupport {
    private static final long serialVersionUID = -3048065065359948044L;
    public static final String INJECTION_SUCCESS = "Injection works.";
    public static final String INITIALIZER_SUCCESS = "Initializer works.";

    @Inject
    private Sheep sheep;
    private boolean initializerCalled = false;

    @Inject
    public void initialize(Sheep sheep) {
        initializerCalled = sheep != null;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (sheep != null) {
            getJspContext().getOut().write(INJECTION_SUCCESS);
        }
        if (initializerCalled) {
            getJspContext().getOut().append(INITIALIZER_SUCCESS);
        }
    }
}
