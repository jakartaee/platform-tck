<%--

    Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ page import="javax.faces.application.ViewHandler" %>
<%@ page import="javax.faces.context.ExternalContext" %>
<%@ page import="javax.faces.context.FacesContext" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
<head>
    <title>encodetest</title>
    <style type="text/css">
        form.fancy {
            border: 1px solid #666699;
            padding: 5px;
        }
    </style>
</head>

<body>

<%-- Commented out due to Bug ID:6485582
<%
    FacesContext fContext = FacesContext.getCurrentInstance();
    ExternalContext eContext = fContext.getExternalContext();
    ViewHandler viewHandler =
        fContext.getApplication().getViewHandler();

    response.addHeader("expectedActionUrl",
                       eContext.encodeResourceURL(
                           viewHandler.getResourceURL(fContext,
                                                      "/faces/encodetest.jsp")));
%>
--%>
<f:view>
    <h:form id="form1"/>
    <h:form id="form2" styleClass="fancy"/>
    <h:form id="form3" binding="#{greeting.myForm}"/>
</f:view>
</body>
</html>
