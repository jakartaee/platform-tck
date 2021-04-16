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
package org.jboss.cdi.tck.tests.context.conversation;

import java.io.IOException;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.NonexistentConversationException;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class OutermostFilter implements Filter {
    @Inject
    private Conversation conversation;
    @Inject
    private Cloud cloud;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if ("foo".equals(request.getParameter("cid"))) {
            try {
                chain.doFilter(request, response);
                throw new RuntimeException("Expected exception not thrown");
            } catch (NonexistentConversationException e) {
                writeNonexistenConversationException(response);
            }
        } else {
          try {
            chain.doFilter(request, response);
          } catch (NonexistentConversationException e) {
              writeNonexistenConversationException(response);
          }
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }


    private void writeNonexistenConversationException(ServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().print("NonexistentConversationException thrown properly\n");
        response.getWriter().print("Conversation.isTransient: " + conversation.isTransient());
        response.getWriter().print("Cloud: " + cloud.getName());
    }
}
