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
    <title>decodetest</title>    
</head>

<body>
<f:view>
    <h:form id="form">
        <h:selectOneMenu id="menu1"> 
	    <f:selectItem id="menu1Item1"
	               itemLabel="foo"
	               itemValue="red" />
	    <f:selectItem id="menu1Item2"
	               itemLabel="bar"
	               itemValue="green" />
        </h:selectOneMenu> 
        <h:commandButton id="button1" value="Submit"/>
    </h:form>    
</f:view>

</body>
</html>
