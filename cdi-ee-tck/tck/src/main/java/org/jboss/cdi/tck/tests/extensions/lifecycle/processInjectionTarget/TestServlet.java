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

import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Vetoed
@WebServlet("/test")
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = -7672096092047821010L;
    private static boolean isWrappedInjectionSuccessfull = false;

    Sheep sheep;

    @Inject
    Fence fence;

    @Inject
    public void initialize(Sheep sheep) {
        this.sheep = sheep;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();

        // to invoke interceptor
        fence.ping();
        String param = req.getParameter("type");
        if (param.equals("servlet")) {
            writer.append(String.valueOf(isWrappedInjectionSuccessfull));
        }
        if (param.equals("interceptor")) {
            writer.append(String.valueOf(FenceInterceptor.isIsWrappedInjectionSuccessfull()));
        }
        if (param.equals("filter")) {
            writer.append(String.valueOf(TestFilter.isIsWrappedInjectionSuccessfull()));
        }
        if (param.equals("sessionbean")) {
            writer.append(String.valueOf(Fence.isIsWrappedInjectionSuccessfull()));
        }
        if (param.equals("listener")) {
            writer.append(String.valueOf(TestListener.isIsWrappedInjectionSuccessfull()));
        }
        if (param.equals("taglibrary")) {
            writer.append(String.valueOf(TagLibraryListener.isIsWrappedInjectionSuccessfull()));
        }

        writer.append("\n"+req.getServletContext().getAttribute("initialized").toString() + "\n");
    }

    public static void setIsWrappedInjectionSuccessfull(boolean isWrappedInjectionSuccessfull) {
        TestServlet.isWrappedInjectionSuccessfull = isWrappedInjectionSuccessfull;
    }

    public static boolean isIsWrappedInjectionSuccessfull() {
        return isWrappedInjectionSuccessfull;
    }

}
