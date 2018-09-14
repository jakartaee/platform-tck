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
        th.sansserif {
            font-family: sans-serif
        }

        td.sansserif {
            font-family: sans-serif
        }                
    </style>
</head>

<body>
<f:view>

    <%--
        Table with column spanning header and footer
    --%>
    <h:panelGrid id="grid1" 
                 columns="3">
        <f:facet name="header">
            <h:outputText value="Header Text For Grid1"/>
        </f:facet>
        <h:column>
            <h:outputText value="3"/>
        </h:column>
        <h:column>
            <h:outputText value="7"/>
        </h:column>
        <h:column>
            <h:outputText value="31"/>
        </h:column>
        <f:facet name="footer">
            <h:outputText value="Footer Text For Grid1"/>
        </f:facet>
    </h:panelGrid>

    <%--
       validate rendered headerClass and footerClass on grid table
    --%>
    <h:panelGrid id="grid2" 
                 columns="3"
                 headerClass="sansserif"
                 footerClass="sansserif">
        <f:facet name="header">
            <h:outputText value="Header Text For Grid2"/>
        </f:facet>
        <h:column>
            <h:outputText value="3"/>
        </h:column>
        <h:column>
            <h:outputText value="7"/>
        </h:column>
        <h:column>
            <h:outputText value="31"/>
        </h:column>
        <f:facet name="footer">
            <h:outputText value="Footer Text For Grid2"/>
        </f:facet>
    </h:panelGrid>       
    
</f:view>
</body>
</html>
