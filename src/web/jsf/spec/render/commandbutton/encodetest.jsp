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

<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="javax.faces.context.ExternalContext"%>
<%@ page import="javax.faces.application.ViewHandler"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
    <head>
        <title>encodetest</title>
        <style type="text/css">
            .blue {
            background-color: blue;
            }
        </style>
    </head>       
    <body>       
        <f:view>  
            <h:form>
                <h:commandButton id="command1"
                                 value="Click Me"/>
                <h:commandButton id="command2"
                                 value="#{'Click Me'}"
                                 type="submit"/>
                <h:commandButton id="command3"
                                 value="Click Me"
                                 type="reset"/>
                <h:commandButton id="command4"
                                 value="Click Me"
                                 type="#{'Reset'}"/>
                <h:commandButton id="command5"
                                 value="Click Me"
                                 styleClass="#{'Color: red;'}"/>
                <h:commandButton id="command6"
                                 image="#{resource['images:pnglogo.png']}"
                                 type="submit"
                                 value="Click Me"/>
                <h:commandButton id="command7"
                                 value="Click Me"
                                 disabled="true"/>
                <h:commandButton id="command8"
                                 value="Click Me"
                                 disabled="false"/>
                <h:commandButton id="command9"
                                 value="Click Me"
                                 readonly="true"/>
                <h:commandButton id="command10"
                                 value="Click Me"
                                 readonly="false"/>
                <h:commandButton id="command11"
                                 binding="#{status.onoff}"/>
            </h:form>
        </f:view>        
    </body>
</html>
