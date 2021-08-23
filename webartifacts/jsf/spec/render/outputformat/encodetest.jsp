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
            background-color: yellow;
            }
        </style>
    </head>
    
    <body>
        <f:view>
            <h:outputLabel value="Case One"/>
            <br>
            <h:outputFormat id="formatter1" 
                            value="Testing styleClass"
                            styleClass="text"/>
            <br>
            
            <h:outputLabel value="Case Two"/>
            <br>
            <h:outputFormat id="formatter2" value="Technology: {0}, Tag: {1}">
                <f:param value="JSF"/>
                <f:param value="h:outputFormat"/>
            </h:outputFormat>
            <br>
            
            <h:outputLabel value="Case Three"/>
            <br>
            <h:outputFormat id="formatter3" value="<default>"/>
            <br>
            
            <h:outputLabel value="Case Four"/>
            <br>
            <h:outputFormat id="formatter4" 
                            value="<true>"
                            escape="true"/>
            <br>
            
            <h:outputLabel value="Case Five"/>
            <br>
            <h:outputFormat id="formatter5" 
                            value="<false>"
                            escape="false"/>
            <br>
            
            <h:outputLabel value="Case Six"/>
            <br>
            <h:outputFormat id="formatter6" 
                            value="Technology: {0}, Tag: {1}">
                <f:param value="#{info.technology}"/>
                <f:param value="#{info.component}"/>
            </h:outputFormat>
            <br>
            
            <h:outputLabel value="Case Seven"/>
            <br>
            <h:panelGrid id="Final_Score">
                <h:outputFormat binding="#{score.fscore}"/>
            </h:panelGrid>
        </f:view>
    </body>
</html>
