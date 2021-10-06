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
            a.sansserif {font-family: sans-serif}
            span.sansserif {font-family: sans-serif}
        </style>
        
    </head>       
    <body>               
         <f:view>  
            <h:form id="form">
                <h:commandLink id="link1" value="Click Me1"/>
                <h:commandLink id="link2" 
                               styleClass="#{'sansserif'}"
                               value="Click Me2"/>
                <h:commandLink id="link3">
                    <h:outputText value="Click Me3"/>
                </h:commandLink>
                <h:commandLink id="link4" value="#{'Click Me4'}">
                    <f:param name="param1" value="value1"/>
                    <f:param name="param2" value="value2"/>
                </h:commandLink>
                <h:commandLink id="link5" 
                               value="Disabled Link" 
                               disabled="true"
                               styleClass="sansserif"/>
                <h:commandLink id="link6"
                               disabled="true">
                    <h:outputText value="Disabled Link(Nested)"/>
                </h:commandLink>
                <h:commandLink id="link7" 
                               value="go there" 
                               binding="#{linker.gothere}"/>
             </h:form>
        </f:view>        
    </body>
</html>
