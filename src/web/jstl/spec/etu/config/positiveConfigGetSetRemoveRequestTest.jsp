<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<%@ page import="javax.servlet.jsp.jstl.core.Config" %>
<tck:test testName="positiveConfigGetSetRemoveRequestTest">
    <!-- Validate the following methods:
           public static Object get(ServletRequest request, String name)
           public static void set(ServletRequest request, String name, Object var)
           public static void remove(ServletRequest, String name) -->
    <%
        // Set the same variable in diffent scopes, with different values.
        Config.set(request, Config.FMT_LOCALE, "requestScope");
        
        // Get the values from the PageContext.  The values returned should all be different.
        String req  = (String) Config.get(request, Config.FMT_LOCALE);

        
        if (req.equals("requestScope")) {
            out.println("Expected value returned from REQUEST_SCOPE<br>\n");
        } else {
            out.println("Unexpected value returned from REQUEST_SCOPE<br>\n");
            out.println("Value: " + req + "<br>\n");
        }

        // Remove the values
        Config.remove(request, Config.FMT_LOCALE);

        // All the values returned from another get will be null
        String nReq  = (String) Config.get(request, Config.FMT_LOCALE);
        
        if (nReq == null) {
            out.println("Value removed from REQUEST_SCOPE<br>\n");
        } else {
            out.println("Unexpected value returned from REQUEST_SCOPE.  Expected Null.<br>\n");
            out.println("Value: " + nReq + "<br>\n");
        }
    %>
        
</tck:test>
