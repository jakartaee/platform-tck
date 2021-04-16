/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.context.conversation.determination;

import java.io.IOException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StatusServlet", urlPatterns = { "/Status" })
public class StatusServlet extends HttpServlet {

    @Inject
    StatusBean statusBean;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        writeInfo(resp);
    }

    private void writeInfo(ServletResponse response) throws IOException {
        response.getWriter().print(getInfo());
        response.getWriter().flush();
    }

    public String getInfo() {
        return String
                .format("onStartAsync: %s, onError: %s, onTimeout: %s, onComplete: %s",
                        statusBean.isOnStartAsync(), statusBean.isOnError(), statusBean.isOnTimeout(), statusBean.isOnComplete());
    }
}
