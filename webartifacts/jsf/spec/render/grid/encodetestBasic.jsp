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
        <title>encodetestBasic</title> 
        <style type="text/css">
            .odd {
                color: cadetblue;
            }
            .even {
                color: darkgray;
            }
        </style>
    </head>
    
    <body>
        <f:view>
            <%-- 
        Most basic table - no facets
            --%>
            <h:panelGrid id="grid1" styleClass="color: yellow;" >
                <h:column>
                    <h:outputText value="3"/>
                </h:column>
                <h:column>
                    <h:outputText value="7"/>
                </h:column>
                <h:column>
                    <h:outputText value="31"/>
                </h:column>
                <h:column>
                    <h:outputText value="127"/>
                </h:column>
                <h:column>
                    <h:outputText value="8191"/>
                </h:column>
                <h:column>
                    <h:outputText value="131071"/>
                </h:column>
            </h:panelGrid>    
            
            <h:panelGrid id="grid2" styleClass="color: yellow;" 
                         columns="2" >
                <h:column>
                    <h:outputText value="3"/>
                </h:column>
                <h:column>
                    <h:outputText value="7"/>
                </h:column>
                <h:column>
                    <h:outputText value="31"/>
                </h:column>
                <h:column>
                    <h:outputText value="127"/>
                </h:column>
                <h:column>
                    <h:outputText value="8191"/>
                </h:column>
                <h:column>
                    <h:outputText value="131071"/>
                </h:column>
            </h:panelGrid>    
            
            <h:panelGrid id="grid3" styleClass="color: yellow;" 
                         columns="3" >
                <h:column>
                    <h:outputText value="3"/>
                </h:column>
                <h:column>
                    <h:outputText value="7"/>
                </h:column>
                <h:column>
                    <h:outputText value="31"/>
                </h:column>
                <h:column>
                    <h:outputText value="127"/>
                </h:column>
                <h:column>
                    <h:outputText value="8191"/>
                </h:column>
                <h:column>
                    <h:outputText value="131071"/>
                </h:column>
            </h:panelGrid>    
            
            <h:panelGrid id="grid4" styleClass="color: yellow;" 
                         columns="6" >
                <h:column>
                    <h:outputText value="3"/>
                </h:column>
                <h:column>
                    <h:outputText value="7"/>
                </h:column>
                <h:column>
                    <h:outputText value="31"/>
                </h:column>
                <h:column>
                    <h:outputText value="127"/>
                </h:column>
                <h:column>
                    <h:outputText value="8191"/>
                </h:column>
                <h:column>
                    <h:outputText value="131071"/>
                </h:column>
            </h:panelGrid>    
            
            <%--
        Same as grid3 , but mix in rowClasses and columnClasses
            --%>
            <h:panelGrid id="grid5" 
                         columns="3"
                         rowClasses="odd,even"
                         columnClasses="even,odd,even">
                <h:column>
                    <h:outputText value="3"/>
                </h:column>
                <h:column>
                    <h:outputText value="7"/>
                </h:column>
                <h:column>
                    <h:outputText value="31"/>
                </h:column>
                <h:column>
                    <h:outputText value="127"/>
                </h:column>
                <h:column>
                    <h:outputText value="8191"/>
                </h:column>
                <h:column>
                    <h:outputText value="131071"/>
                </h:column>
            </h:panelGrid>
            
            <%--
            Test the binding attribute.
            --%>
            <h:panelGrid id="grid6" binding="#{location.gps}"/>
            
        </f:view>
    </body>
</html>
