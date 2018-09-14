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

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
    <head>
        <title>encodetest</title>
        <style type="text/css">
            .text {
                background-color: blue;
            }
        </style>
    </head>
    
    <body>
        <f:view>
            <h:form id="form">
                <h:outputText id="text1" 
                              styleClass="text"/>
                <h:outputText id="text2"
                              value="<p>"
                              styleClass="text"/>
                <h:outputText id="text3"
                              escape="true"
                              value="<p>"
                              styleClass="text"/>
                <h:outputText id="text4"
                              escape="false"
                              value="<p>"
                              styleClass="text"/>
                <h:outputText id="text5" binding="#{Out.text}" />         
            </h:form>
        </f:view>
    </body>
</html>
