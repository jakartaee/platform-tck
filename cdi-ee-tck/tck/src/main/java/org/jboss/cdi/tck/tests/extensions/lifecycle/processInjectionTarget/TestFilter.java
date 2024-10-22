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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

@Vetoed
@WebFilter("/test")
public class TestFilter implements Filter {

    @Inject
    private Sheep sheep;

    private static boolean isWrappedInjectionSuccessfull = false;

    @Inject
    public void initialize(Sheep sheep) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        chain.doFilter(request,response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public static boolean isIsWrappedInjectionSuccessfull() {
        return isWrappedInjectionSuccessfull;
    }

    public static void setIsWrappedInjectionSuccessfull(boolean isWrappedInjectionSuccessfull) {
        TestFilter.isWrappedInjectionSuccessfull = isWrappedInjectionSuccessfull;
    }
}
