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
                <h:inputText id="text1" value="text value"/>
                <h:inputText id="text2"
                             value="text value"
                             styleClass="text"/>
                <h:inputText id="text3"
                             value="text value"
                             autocomplete="off"/>
                <h:inputText id="text4"
                             value="text value"
                             autocomplete="on"/>
                <h:inputText id="text5"
                             value="value"
                             disabled="true"/>
                <h:inputText id="text6"
                             value="value"
                             disabled="false"/>
                <h:inputText id="text7"
                             value="value"
                             readonly="true"/>
                <h:inputText id="text8"
                             value="value"
                             readonly="false"/>
                <h:inputText id="text9" binding="#{Hello.greeting}"/>
            </h:form>
        </f:view>
    </body>
</html>
