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
<%
    pageContext.setAttribute("asdf", "asdf");

    Float f1 = new Float(1.5f);
    Float f2 = new Float(1.5e-4f);
    Float f3 = new Float(1.5E+4);
    BigDecimal bd1 = new BigDecimal(1.6d);
    BigInteger bi1 = new BigInteger("5");
    Long l1 = new Long(4);
    Integer i1 = new Integer(5);
    Short s1 = new Short((short) 6);
    pageContext.setAttribute("long1", l1);
    pageContext.setAttribute("integer1", i1);
    pageContext.setAttribute("short1", s1);
    pageContext.setAttribute("float1", f1);
    pageContext.setAttribute("float2", f2);
    pageContext.setAttribute("float3", f3);
    pageContext.setAttribute("double1", new Double(1.5d));
    pageContext.setAttribute("double2", new Double(1.5e-4d));
    pageContext.setAttribute("double3", new Double(1.5E+4d));
    pageContext.setAttribute("string1", "1.5");
    pageContext.setAttribute("string2", "1.5e-4");
    pageContext.setAttribute("string3", "1.5E+4");
    pageContext.setAttribute("bigdecimal1", bd1);
    pageContext.setAttribute("biginteger1", bi1);
    Double fDiv = new Double(f1.doubleValue() / f2.doubleValue() / f3.doubleValue());
    Double dDiv = new Double(1.5d / 1.5e-4 / 1.5E+4);
    Integer iZero = new Integer(0);
    Double lDiv = new Double(l1.doubleValue() / s1.doubleValue());
    Double iDiv = new Double(i1.doubleValue() / s1.doubleValue());
    BigDecimal bdDiv1 = bd1.divide(new BigDecimal(bi1), BigDecimal.ROUND_HALF_UP);
    BigDecimal bdDiv2 = bd1.divide(new BigDecimal(new Double(1.5d).doubleValue()), BigDecimal.ROUND_HALF_UP);
    BigDecimal bdDiv3 = new BigDecimal(bi1).divide(new BigDecimal(new Double(1.5d).doubleValue()), BigDecimal.ROUND_HALF_UP);
    BigDecimal bdDiv4 = new BigDecimal(bi1).divide(bd1, BigDecimal.ROUND_HALF_UP);

%>
<%-- Validates binary operators / and div --%>

<% try { %>
    ${asdf / 6}
    Test FAILED.  Exception did not occur when trying to evaluate
    "'asdf' / 6" using binary operator +.
<% } catch (Throwable t) { } %>



<el:checkOperator name="FloatDiv" control='<%= fDiv %>'
                  object="${float1 / float2 / float3}">
    <el:checkOperator name="DoubleDiv" control='<%= dDiv %>'
                      object="${double1 div double2 div double3}">
        <el:checkOperator name="StringDiv" control='<%= dDiv %>'
                          object="${string1 / string2 / string3}">
            <el:checkOperator name="MixDiv" control='<%= dDiv %>'
                              object="${float1 div double2 div string3}">
                <el:checkOperator name="NullDiv" control='<%= iZero %>'
                                  object="${null / null}">
                    <el:checkOperator name="LongShortDiv" control='<%= lDiv %>'
                                      object="${long1 div short1}">
                        <el:checkOperator name="IntShortDiv" control='<%= iDiv %>'
                                          object="${integer1 / short1}">
                           <el:checkOperator name="BigDecimalDivBigInteger" control='<%= bdDiv1 %>'
                                             object="${bigdecimal1 / biginteger1}">
                              <el:checkOperator name="BigDecimalDivDouble" control='<%= bdDiv2 %>'
                                                object="${bigdecimal1 div double1}">
                                <el:checkOperator name="BigIntegerDivDouble" control='<%= bdDiv3 %>'
                                                  object="${biginteger1 / double1}">
                                   <el:checkOperator name="BigIntegerDivBigDecimal" control='<%= bdDiv4 %>'
                                                  object="${biginteger1 div bigdecimal1}" display="true"/>
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
