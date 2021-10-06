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
                <h:selectBooleanCheckbox id="checkbox1" />
                <h:selectBooleanCheckbox id="checkbox2" value="true"/>
                <h:selectBooleanCheckbox id="checkbox3" value="false"/>
                <h:selectBooleanCheckbox id="checkbox4" value="foo"/>
                <h:selectBooleanCheckbox id="checkbox5" 
                                         styleClass="text"/>
                <h:selectBooleanCheckbox id="checkbox6"
                                         disabled="true"/>
                <h:selectBooleanCheckbox id="checkbox7"
                                         disabled="false"/>
                <h:selectBooleanCheckbox id="checkbox8"
                                         readonly="true"/>
                <h:selectBooleanCheckbox id="checkbox9"
                                         readonly="false"/>
                <h:selectBooleanCheckbox id="yesno"
                                         binding="#{status.yesno}"/>
            </h:form>
        </f:view>
    </body>
</html>
