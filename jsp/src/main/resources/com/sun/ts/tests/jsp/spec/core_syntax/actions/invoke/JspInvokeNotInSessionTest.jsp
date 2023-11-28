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

<%@ page contentType="text/plain" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<% try { %>

<tags:NotInSessionTag>
    <jsp:attribute name="attr1">
        attribute value
    </jsp:attribute>
</tags:NotInSessionTag>

Test FAILED.  This page doesn't participate in a session.  The
tag called by this page set an attribute in the SESSION scope,
but no IllegalStateException was thrown.

<% } catch (Throwable t) {
       if (t instanceof IllegalStateException) {
           out.println("Test PASSED");
       } else {
           out.println("Test FAILED.  An Exception was thrown when the calling" +
               " page doesn't participate in a session and a tag called by the" +
               " page attempts to set an attribute in the session scope.\n" +
               "Expected an instance of IllegalStateException but received" +
               " " + t.getClass().getName());
       }
   }
%>

