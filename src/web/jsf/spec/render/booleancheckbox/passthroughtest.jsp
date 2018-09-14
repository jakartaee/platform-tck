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
    <title>passthroughtest</title>
</head>

<body>
<f:view>
    <h:selectBooleanCheckbox id="checkbox1"
                    accesskey="P"
                    dir="LTR"                   
                    lang="en"  
                    onblur="js1"
                    onchange="js2"
                    onclick="js3"                    
                    ondblclick="js4"
                    onfocus="js5"       
                    onkeydown="js6"
                    onkeypress="js7"
                    onkeyup="js8"
                    onmousedown="js9"
                    onmousemove="js10"
                    onmouseout="js11"                                 
                    onmouseover="js12"
                    onmouseup="js13"
                    onselect="js14"
                    style="Color: red;"
                    tabindex="0"                    
                    title="title"/>
</f:view>
</body>
</html>
