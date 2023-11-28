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

<%@ page import="java.math.BigDecimal,
                 java.math.BigInteger"%>
<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el" %>

<%-- Validate the unary minus ('-') operator --%>

<%
    pageContext.setAttribute("float1", new Float(1.5f));
    pageContext.setAttribute("double1", new Double(1.5d));
    pageContext.setAttribute("long1", new Long(4L));
    pageContext.setAttribute("integer1", new Integer(-5));
    pageContext.setAttribute("short1", new Short((short) 6));
    pageContext.setAttribute("byte1", new Byte((byte) 1));
    pageContext.setAttribute("string1", "1.5");
    pageContext.setAttribute("string2", "1.5e-4");
    pageContext.setAttribute("string3", "1.5E+4");
    pageContext.setAttribute("string4", "-15");
    pageContext.setAttribute("bigdecimal1", new BigDecimal("100.5"));
    pageContext.setAttribute("biginteger1", new BigInteger("125"));
%>

<el:checkOperator name="null" control='<%= new Integer(0) %>' object="${-null}">
    <el:checkOperator name="string1" control='<%= new Double("-1.5") %>'
        object="${-string1}">
        <el:checkOperator name="string2" control='<%= new Double("-1.5e-4") %>'
            object="${-string2}">
            <el:checkOperator name="string3" control='<%= new Double("-1.5E+4") %>'
                object="${-string3}">
                <el:checkOperator name="string4" control='<%= new Long("15") %>'
                    object="${-string4}">
                    <el:checkOperator name="byte1" control='<%= new Byte((byte) -1) %>'
                        object="${-byte1}">
                        <el:checkOperator name="short1" control='<%= new Short((short) -6) %>'
                            object="${-short1}">
                            <el:checkOperator name="integer1" control='<%= new Integer(5) %>'
                                object="${-integer1}">
                                <el:checkOperator name="long1" control='<%= new Long(-4L) %>'
                                    object="${-long1}">
                                    <el:checkOperator name="float1" control='<%= new Float(-1.5f) %>'
                                        object="${-float1}">
                                        <el:checkOperator name="double1" control='<%= new Double(-1.5d) %>'
                                            object="${-double1}">
                                           <el:checkOperator name="biginteger1" control='<%= new BigInteger("125").negate() %>'
                                              object="${-biginteger1}">
                                              <el:checkOperator name="bigdecimal1" control='<%= new BigDecimal("100.5").negate() %>'
                                                 object="${-bigdecimal1}" display="true"/>
                                           </el:checkOperator>
                                        </el:checkOperator>
                                    </el:checkOperator>
                                </el:checkOperator>
                            </el:checkOperator>
                        </el:checkOperator>
                    </el:checkOperator>
                </el:checkOperator>
            </el:checkOperator>
        </el:checkOperator>
    </el:checkOperator>
</el:checkOperator>


