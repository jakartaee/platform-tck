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
<tck:test testName="positiveConfigGetSetRemoveSessionTest">
    <!-- Validate the following methods:
           public static Object get(HttpSession session, String name)
           public static void set(HttpSession session, String name, Object var)
           public static void remove(HttpSession session, String name) -->
    <%
        // Set the same variable in diffent scopes, with different values.
        Config.set(session, Config.FMT_LOCALE, "sessionScope");
        
        // Get the values from the PageContext.  The values returned should all be different.
        String ses  = (String) Config.get(session, Config.FMT_LOCALE);

        
        if (!ses.equals("sessionScope")) {
            out.println("Test FAILED.  Unexpected value returned from SESSION_SCOPE<br>\n");
            out.println("Value: " + ses + "<br>\n");
            return; 
        } 

        // Remove the values
        Config.remove(session, Config.FMT_LOCALE);

        // All the values returned from another get will be null
        ses = (String) Config.get(session, Config.FMT_LOCALE);
        
        if (ses != null) {
            out.println("Test FAILED.  Unexpected value returned from SESSION_SCOPE.  Expected Null.<br>\n");
            out.println("Value: " + ses + "<br>\n");
            return;
        } 

        // Add the value back and invalidate the session
       Config.set(session, Config.FMT_LOCALE, "sessionScope");
       session.invalidate();

       // value must be null
       ses = (String) Config.get(session, Config.FMT_LOCALE);
       if (ses != null) {
          out.println("Test FAILED.  Unexpected value returned from SESSION_SCOPE.  Execpted 'null'.<br>\n");
          out.println("Value: " + ses + "<br>\n");
          return;
       }
       
       // all good, so pass the test
       out.println("Test PASSED");
    %>
        
</tck:test>
