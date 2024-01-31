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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment.jndi;

import java.io.IOException;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/foo")
public class FooServlet extends HttpServlet {

    private Foo foo;

    @Inject
    Instance<Foo> fooInstance;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        String get = req.getParameter("get");
        if (get.equals("init")) {
            MyInterceptor.reset();
            this.foo = fooInstance.get();
            // fail if interceptor was not called
            resp.setStatus(MyInterceptor.isInterceptorCalled() ? 200 : 500);
        } else if (get.equals("name")) {
            resp.getWriter().write(foo.getAnimal().getName());
        } else if (get.equals("intName")) {
            resp.getWriter().write(MyInterceptor.getAnimal().getName());
        } else if (get.equals("greeting")) {
            resp.getWriter().write(foo.getGreeting());
        } else if (get.equals("intGreeting")) {
            resp.getWriter().write(MyInterceptor.getGreeting());
        } else {
            resp.setStatus(404);
        }
    }
}
