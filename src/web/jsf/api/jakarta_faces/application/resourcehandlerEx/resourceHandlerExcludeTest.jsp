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
        <title>resourceHandlerExcludeTest</title>    
    </head>
    
    <body>
        <f:view>
            <h:outputLink id="xhtml" 
                          value="#{resource['neg_xhtml_test.xhtml']}">
                "BROKEN .xhtml RESOURCE LINK"
            </h:outputLink>
            <h:outputLink id="properties" 
                          value="#{resource['neg_prop_test.properties']}">
                "BROKEN .properties RESOURCE LINK"
            </h:outputLink>
            <h:outputLink id="class" 
                          value="#{resource['neg_class_test.class']}">
                "BROKEN .class RESOURCE LINK"
            </h:outputLink>
            <h:outputLink id="jsp" 
                          value="#{resource['neg_jsp_test.jsp']}">
                "BROKEN .jsp RESOURCE LINK"
            </h:outputLink>
            <h:outputLink id="jspx" 
                          value="#{resource['neg_jspx_test.jspx']}">
                "BROKEN .jspx RESOURCE LINK"
            </h:outputLink>
        </f:view>
        
    </body>
</html>
