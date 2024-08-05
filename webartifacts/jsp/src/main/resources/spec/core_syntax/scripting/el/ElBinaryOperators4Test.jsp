<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

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

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%
    Boolean True = new Boolean(true);
    Boolean False = new Boolean(false);
    pageContext.setAttribute("True", True);
    pageContext.setAttribute("False", False);
%>

<%-- validate binary operators &&, and, ||, and or --%>
<el:checkOperator control='<%= True %>' object="${True || False}">
    <el:checkOperator control='<%= True %>' object="${True || True}">
        <el:checkOperator control='<%= True %>' object="${False or True}">
            <el:checkOperator control='<%= False %>' object="${False or False}">
                <el:checkOperator control='<%= False %>' object="${True && False}">
                    <el:checkOperator control='<%= True %>' object="${True && True}">
                        <el:checkOperator control='<%= False %>' object="${False and True}">
                            <el:checkOperator control='<%= False %>' object="${False and False}"
                                              display="true"/>
                        </el:checkOperator>
                    </el:checkOperator>
                </el:checkOperator>
            </el:checkOperator>
        </el:checkOperator>
    </el:checkOperator>
</el:checkOperator>


