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
    Boolean False = new Boolean(false);
    Boolean True = new Boolean(true);
    pageContext.setAttribute("True", True);
    pageContext.setAttribute("False", False);
    pageContext.setAttribute("float1", new Float(1.6f));
    pageContext.setAttribute("double1", new Double(1.6d));
    pageContext.setAttribute("byte1", new Byte((byte) 1));
    pageContext.setAttribute("short1", new Short((short) 2));
    pageContext.setAttribute("integer1", new Integer(3));
    pageContext.setAttribute("character1", new Character('9'));
    pageContext.setAttribute("long1", new Long(4L));
    pageContext.setAttribute("string1", "3");
    pageContext.setAttribute("bigdecimal1", new BigDecimal(1.6));
    pageContext.setAttribute("biginteger1", new BigInteger("3"));
%>

<%-- Validate relation operators '==', '!=', 'eq', 'ne' --%>

<el:checkOperator name="null equality ==" control='<%= True %>' object="${null == null}">
 <el:checkOperator name="null equality eq" control='<%= True %>' object="${null eq null}">
  <el:checkOperator name="null ==" control='<%= False %>' object="${null == 'string'}">
   <el:checkOperator name="null eq" control='<%= False %>' object="${'string' eq null}">
    <el:checkOperator name="null !=" control='<%= True %>' object="${'string' != null}">
     <el:checkOperator name="null ne" control='<%= True %>' object="${null ne 'string'}">
      <el:checkOperator name="DoubleByte ==" control='<%= False %>' object="${double1 == byte1}">
       <el:checkOperator name="FloatShort !=" control='<%= True %>' object="${float1 != short1}">
        <el:checkOperator name="DoubleInt eq" control='<%= False %>' object="${double1 eq integer1}">
         <el:checkOperator name="FloatLong ne" control='<%= True %>' object="${float1 ne long1}">
          <el:checkOperator name="DoubleFloat ==" control='<%= False %>' object="${float1 == double1}">
           <el:checkOperator name="DoubleFloat ne" control='<%= True %>' object="${float1 ne double1}">
            <el:checkOperator name="ByteShort ==" control='<%= False %>' object="${byte1 == short1}">
             <el:checkOperator name="ShortCharacter !=" control='<%= True %>' object="${short1 != character1}">
              <el:checkOperator name="CharacterInt eq" control='<%= False %>' object="${character1 eq integer1}">
               <el:checkOperator name="IntLong ne" control='<%= True %>' object="${integer1 ne long1}">
                <el:checkOperator name="BooleanBoolean ==" control='<%= True %>' object="${True == True}">
                 <el:checkOperator name="BooleanBoolean !=" control='<%= False %>' object="${True != True}">

                  <el:checkOperator name="StringByte ==" control='<%= False %>' object="${string1 == byte1}">
                   <el:checkOperator name="StringCharacter ne" control='<%= True %>' object="${string1 ne character1}">
                    <el:checkOperator name="StringShort eq" control='<%= False %>' object="${string1 eq short1}">
                     <el:checkOperator name="StringInt ne" control='<%= False %>' object="${integer1 ne string1}">
                      <el:checkOperator name="StringLong !=" control='<%= True %>' object="${string1 != long1}">
                       <el:checkOperator name="StringFloat ==" control='<%= False %>' object="${float1 == string1}">
                        <el:checkOperator name="StringDouble eq" control='<%= False %>' object="${double1 eq string1}">
                         <el:checkOperator name="BigDecimalBigInteger ==" control='<%= False %>' object="${bigdecimal1 == biginteger1}">
                          <el:checkOperator name="BigDecimalFloat eq" control='<%= False %>' object="${float1 eq bigdecimal1}">
                           <el:checkOperator name="BigDecimalDouble !=" control='<%= False %>' object="${bigdecimal1 != double1}">
                            <el:checkOperator name="BigDecimalString ne" control='<%= True %>' object="${string1 ne bigdecimal1}">
                             <el:checkOperator name="BigIntegerFloat ==" control='<%= False %>' object="${biginteger1 == float1}">
                              <el:checkOperator name="BigIntegerInteger eq" control='<%= True %>' object="${integer1 eq biginteger1}">
                               <el:checkOperator name="BigIntegerShort !=" control='<%= True %>' object="${biginteger1 != short1}">
                                <el:checkOperator name="BigIntegerInteger1 ne" control='<%= False %>' object="${biginteger1 ne integer1}"
                                                  display="true"/>
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
       </el:checkOperator>
      </el:checkOperator>
     </el:checkOperator>
    </el:checkOperator>
   </el:checkOperator>
  </el:checkOperator>
 </el:checkOperator>
</el:checkOperator>
