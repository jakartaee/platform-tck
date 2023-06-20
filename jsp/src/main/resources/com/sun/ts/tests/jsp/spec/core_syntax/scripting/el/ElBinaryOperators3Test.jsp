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
    Float f2 = new Float(1.5e-4);
    Float f3 = new Float(1.5E+4);
    BigDecimal bd1 = new BigDecimal(1.6d);
    BigInteger bi1 = new BigInteger("5");
    pageContext.setAttribute("bigdecimal1", bd1);
    pageContext.setAttribute("biginteger1", bi1);
    pageContext.setAttribute("float1", f1);
    pageContext.setAttribute("float2", f2);
    pageContext.setAttribute("float3", f3);
    pageContext.setAttribute("double1", new Double(1.5d));
    pageContext.setAttribute("double2", new Double(1.5e-4d));
    pageContext.setAttribute("double3", new Double(1.5E+4d));
    pageContext.setAttribute("long1", new Long(4));
    pageContext.setAttribute("integer1", new Integer(5));
    pageContext.setAttribute("short1", new Short((short) 6));
    pageContext.setAttribute("string1", "1.5");
    pageContext.setAttribute("string2", "1.5e-4");
    pageContext.setAttribute("string3", "1.5E+4");
    Double fMod = new Double(f1.doubleValue() % f2.doubleValue() % f3.doubleValue());
    Double dMod = new Double(1.5d % 1.5e-4d % 1.5E+4d);
    Double dMixMod = new Double(1.5d % 4d);
    Double bigDecimal1 = new Double(bd1.doubleValue() % f1.doubleValue());
    Double bigDecimal2 = new Double(bd1.doubleValue() % new Double("1.5").doubleValue());
    Double bigDecimal3 = new Double(bd1.doubleValue() % bi1.doubleValue());
    Double bigDecimal4 = new Double(bd1.doubleValue() % bd1.doubleValue());
    Double bigInteger1 = new Double(1.5d % bi1.doubleValue());
    BigInteger bigInteger2 = bi1.remainder(new BigInteger(new Integer(5).toString()));

    Long lMod = new Long(4 % 5);
    Integer iZero = new Integer(0);
%>
<%-- Validates binary operators % and mod --%>

<% try { %>
    ${asdf % 6}
    Test FAILED.  Exception did not occur when trying to evaluate
    "'asdf' % 6" using binary operator +.
<% } catch (Throwable t) { } %>



<el:checkOperator name="FloatMod" control='<%= fMod %>'
                  object="${float1 % float2 % float3}">
    <el:checkOperator name="DoubleMod" control='<%= dMod %>'
                      object="${double1 % double2 % double3}">
        <el:checkOperator name="StringMod" control='<%= dMod %>'
                          object="${string1 % string2 % string3}">
            <el:checkOperator name="MixMod" control='<%= dMod %>'
                              object="${float1 mod double2 mod string3}">
                <el:checkOperator name="Null%" control='<%= iZero %>'
                                  object="${null % null}">
                    <el:checkOperator name="LongDoubleMod" control='<%= dMixMod %>'
                                      object="${double1 mod long1}">
                        <el:checkOperator name="LongMod" control='<%= lMod %>'
                                          object="${long1 % integer1}">
                           <el:checkOperator name="BigDecimalModFloat" control='<%= bigDecimal1 %>'
                                             object="${bigdecimal1 % float1}">
                              <el:checkOperator name="BigDecimalModDouble" control='<%= bigDecimal2 %>'
                                                object="${bigdecimal1 mod double1}">
                                  <el:checkOperator name="BigDecimalModBigInteger" control='<%= bigDecimal3 %>'
                                                    object="${bigdecimal1 mod biginteger1}">
                                     <el:checkOperator name="BigDecimalModBigDecimal" control='<%= bigDecimal4 %>'
                                                       object="${bigdecimal1 % bigdecimal1}">
                                        <el:checkOperator name="DoubleModBigInteger" control='<%= bigInteger1 %>'
                                                          object="${double1 mod biginteger1}">
                                           <el:checkOperator name="BigIntegerModInteger" control='<%= bigInteger2 %>'
                                                             object="${biginteger1 % integer1}" display="true"/>
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
