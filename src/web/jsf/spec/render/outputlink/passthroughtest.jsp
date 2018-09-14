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
            <h:outputLink  value="decodetest.jsp"
                           id="output1"
                           accesskey="P"
                           charset="ISO_8859-1:1987"
                           coords="nothing"
                           dir="LTR"
                           hreflang="en"
                           lang="en"
                           onblur="js1"
                           onclick="js2"                    
                           ondblclick="js3"
                           onfocus="js4"       
                           onkeydown="js5"
                           onkeypress="js6"
                           onkeyup="js7"
                           onmousedown="js8"
                           onmousemove="js9"
                           onmouseout="js10"                                 
                           onmouseover="js11"
                           onmouseup="js12"
                           rel="somewhere"
                           rev="beenthere"
                           shape="poly"
                           style="Color: red;"
                           tabindex="1"
                           type="good"
                           title="title"/>
        </f:view>
    </body>
</html>
