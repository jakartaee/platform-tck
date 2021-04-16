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
package org.jboss.cdi.tck.tests.decorators.definition.lifecycle;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.ActionSequence;

@WebServlet("/bank")
@SuppressWarnings("serial")
public class BankServlet extends HttpServlet {

    @Inject
    ShortTermAccount shortTermAccount;

    @Inject
    DurableAccount durableAccount;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if ("withdraw".equals(req.getParameter("action"))) {
            shortTermAccount.withdraw(getAmountValue(req));
            durableAccount.withdraw(getAmountValue(req));
        } else if ("deposit".equals(req.getParameter("action"))) {
            shortTermAccount.deposit(getAmountValue(req));
            durableAccount.deposit(getAmountValue(req));
        }

        resp.getWriter().append("ShortTermBalance:" + shortTermAccount.getBalance());
        resp.getWriter().append("\n");
        resp.getWriter().append("DurableBalance:" + durableAccount.getBalance());
        resp.getWriter().append("\n");
        resp.getWriter().append("PostConstructCallers:" + ActionSequence.getSequenceSize("postConstructCallers"));
        resp.getWriter().append("\n");
        resp.getWriter().append("PreDestroyCallers:" + ActionSequence.getSequenceSize("preDestroyCallers"));
        resp.getWriter().append("\n");

        resp.setContentType("text/plain");
    }

    private Integer getAmountValue(HttpServletRequest req) {
        return Integer.valueOf(req.getParameter("amount"));
    }
}
