/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jakarta.inject.Inject;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(value = { "/foo-async" }, asyncSupported = true)
@SuppressWarnings("serial")
public class AsyncFooServlet extends HttpServlet {

    public static final String TIMEOUT = "timeout";
    public static final String COMPLETE = "complete";
    public static final String ERROR = "error";
    public static final String LOOP = "loop";

    private static boolean inLoop = false;
    private ExecutorService executorService;

    @Inject
    StatusBean statusBean;

    @Override
    public void init() throws ServletException {
        // Note that executor thread does not use the same CL
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String param = req.getParameter("action");
        String cid = req.getParameter("cid");

        statusBean.reset();
        final AsyncContext actx = req.startAsync();
        actx.addListener(actx.createListener(QuxAsyncListener.class));
        resp.setContentType("text/plain");

        if (TIMEOUT.equals(param)) {
            actx.setTimeout(150l);
        } else if (COMPLETE.equals(param)) {
            executorService.execute(new AsyncRequestProcessor(actx, 50l, false, null));
        } else if (ERROR.equals(param)) {
            executorService.execute(new AsyncRequestProcessor(actx, 50l, true, "/FailingServlet?cid="+cid));
        } else if (LOOP.equals(param)) {
            if (inLoop) {
                executorService.execute(new AsyncRequestProcessor(actx, 50l, false, null));

            } else {
                executorService.execute(new AsyncRequestProcessor(actx, 50l, true, null));
                inLoop = true;
            }
        }
    }
}
