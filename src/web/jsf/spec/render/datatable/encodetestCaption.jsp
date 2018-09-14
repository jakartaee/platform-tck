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

<%@ include file="include.jsp"%>

<html>
    <head>
        <title>encodetest</title>
        <style type="text/css">
            caption.sansserif {
                font-family: sans-serif
            }        
        </style>
    </head>
    
    <body>
        <f:view>
            
            <%--
        Table with Caption 
            --%>
            <h:dataTable id="data1" 
                         value="#{requestScope.DataList}" 
                         var="data1">
                <f:facet name="caption">
                    <h:outputText value="Caption Text For Data1"/>
                </f:facet>
                <f:facet name="header">
                    <h:outputText value="#{'Header Text For Data1'}"/>
                </f:facet>
                <h:column>
                    <h:outputText value="#{data1.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data1.gender}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data1.age}"/>
                </h:column>
                <f:facet name="footer">
                    <h:outputText value="Footer Text For Data1"/>
                </f:facet>
            </h:dataTable>    
            
            <h:dataTable id="data2" 
                         value="#{requestScope.DataList}"
                         var="data2"
                         captionStyle="Color: red;">
                <f:facet name="caption">
                    <h:outputText value="Caption Text For Data2"/>
                </f:facet>
                <f:facet name="header">
                    <h:outputText value="#{'Header Text For Data2'}"/>
                </f:facet>
                <h:column>
                    <h:outputText value="#{data2.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data2.gender}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data2.age}"/>
                </h:column>
                <f:facet name="footer">
                    <h:outputText value="Footer Text For Data2"/>
                </f:facet>
            </h:dataTable>
            
            <h:dataTable id="data3"
                         value="#{requestScope.DataList}" 
                         var="data3" 
                         captionClass="sansserif">
                <f:facet name="caption">
                    <h:outputText value="Caption Text For Data3"/>
                </f:facet>
                <f:facet name="header">
                    <h:outputText value="#{'Header Text For Data3'}"/>
                </f:facet>
                <h:column>
                    <h:outputText value="#{data3.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data3.gender}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data3.age}"/>
                </h:column>
                <f:facet name="footer">
                    <h:outputText value="Footer Text For Data3"/>
                </f:facet>
            </h:dataTable>
            
            <h:dataTable id="data4" 
                         value="#{requestScope.DataList}" 
                         var="data4" 
                         captionClass="sansserif"
                         captionStyle="Color: red;">
                <f:facet name="caption">
                    <h:outputText value="Caption Text For Data4"/>
                </f:facet>
                <f:facet name="header">
                    <h:outputText value="#{'Header Text For data4'}"/>
                </f:facet>
                <h:column>
                    <h:outputText value="#{data4.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data4.gender}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data4.age}"/>
                </h:column>
                <f:facet name="footer">
                    <h:outputText value="Footer Text For data4"/>
                </f:facet>
            </h:dataTable>
            
            <h:dataTable id="data5" 
                         value="#{requestScope.DataList}" 
                         var="data5" 
                         captionClass="sansserif"
                         captionStyle="Color: red;">
                <f:facet name="colgroups"> 
                    <h:panelGroup>
                        <colgroup id="lefty" align="left"/>
                        <colgroup id="middle" align="center"/>
                        <colgroup id="righty" align="right"/>
                    </h:panelGroup>
                </f:facet>
                <f:facet name="header">
                    <h:outputText value="Header Text For Data5"/>
                </f:facet>
                <h:column>
                    <h:outputText value="#{data5.name}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data5.gender}"/>
                </h:column>
                <h:column>
                    <h:outputText value="#{data5.age}"/>
                </h:column>
            </h:dataTable>
            
        </f:view>
    </body>
</html>
