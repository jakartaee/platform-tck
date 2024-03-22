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

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/body" prefix="body" %>

<%--
    If one or more jsp:attribute elements appear in the body
    of an action, and there is no jsp:body element appears, it
    is the equivalent of having an empty body
--%>

<body:cemptybody>
    <jsp:attribute name="attr1">value1</jsp:attribute>
    <jsp:attribute name="attr2">value2</jsp:attribute>
</body:cemptybody>

<body:semptybody>
    <jsp:attribute name="attr1">value1</jsp:attribute>
</body:semptybody>

<%--
     Any empty body can also be provided by jsp:body
     by the production <jsp:body /> and <jsp:body></jsp:body>
--%>

<body:cemptybody>
    <jsp:body/>
</body:cemptybody>

<body:cemptybody>
    <jsp:body></jsp:body>
</body:cemptybody>

<body:semptybody>
    <jsp:body/>
</body:semptybody>

<body:semptybody>
    <jsp:body></jsp:body>
</body:semptybody>


