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
<tck:test testName="positiveConfigFindTest">
    <!-- Validate the following methods:
           public static Object find(PageContext pageContext, String name) -->
    <%
        // Set the same variable in diffent scopes, with different values.
        Config.set(application, Config.FMT_LOCALE, "scopedObject");
        
        // Find the value in the PageContext.
        String found  = (String) Config.find(pageContext, Config.FMT_LOCALE);

        
        if (found.equals("scopedObject")) {
            out.println("Expected value returned from find()<br>\n");
        } else {
            out.println("Unexpected value returned from find()<br>\n");
            out.println("Value: " + found + "<br>\n");
        }

        // Remove the value
        Config.remove(application, Config.FMT_LOCALE);

        // If no value found in the PageContext, check for an 
        // context initialialization parameter identified by name.
        String init  = (String) Config.find(pageContext, Config.FMT_LOCALE);
        
        if (init != null && init.equals("en-US")) {
            out.println("Context initialization parameter obtained.\n");
        } else {
            out.println("Unexpected value returned when getting Context initialization parameter.<br>\n");
            out.println("Value: " + init + "<br>\n");
        }
    %>
        
</tck:test>
