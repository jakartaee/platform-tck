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
    pageContext.setAttribute("float1", new Float(1.5f));
    pageContext.setAttribute("double1", new Double(1.6d));
    pageContext.setAttribute("byte1", new Byte((byte) 1));
    pageContext.setAttribute("short1", new Short((short) 2));
    pageContext.setAttribute("integer1", new Integer(3));
    pageContext.setAttribute("character1", new Character('9'));
    pageContext.setAttribute("long1", new Long(4L));
    pageContext.setAttribute("string1", "3");
    pageContext.setAttribute("bigdecimal1", new BigDecimal("5.1"));
    pageContext.setAttribute("biginteger1", new BigInteger("6"));
    pageContext.setAttribute("double2", new Double(5.1));
    pageContext.setAttribute("integer2", new Integer(6));
%>

<el:checkOperator name="null <" control='<%= False %>' object="${null < 1}">
 <el:checkOperator name="null >" control='<%= False %>' object="${1 > null}">
  <el:checkOperator name="null lt" control='<%= False %>' object="${1 lt null}">
   <el:checkOperator name="null gt" control='<%= False %>' object="${null gt 1}">
    <el:checkOperator name="null <=" control='<%= False %>' object="${1 <= null}">
     <el:checkOperator name="null >=" control='<%= False %>' object="${null >= 1}">
      <el:checkOperator name="null le" control='<%= False %>' object="${1 le null}">
       <el:checkOperator name="null ge" control='<%= False %>' object="${null ge 1}">
        <el:checkOperator name="DoubleByte <" control='<%= True %>' object="${byte1 < double1}">
         <el:checkOperator name="DoubleShort >" control='<%= False %>' object="${double1 > short1}">
          <el:checkOperator name="DoubleInteger lt" control='<%= True %>' object="${double1 lt integer1}">
           <el:checkOperator name="DoubleLong gt" control='<%= True %>' object="${long1 gt double1}">
            <el:checkOperator name="DoubleFloat <=" control='<%= False %>' object="${float1 <= doubl1}">
             <el:checkOperator name="DoubleDouble >=" control='<%= True %>' object="${double1 >= double1}">
              <el:checkOperator name="FloatInt le" control='<%= True %>' object="${float1 le integer1}">
               <el:checkOperator name="DoubleFloat ge" control='<%= False %>' object="${float1 ge double1}">
                <el:checkOperator name="ByteShort >" control='<%= True %>' object="${short1 > byte1}">
                 <el:checkOperator name="ShortInt <" control='<%= True %>' object="${short1 < integer1}">
                  <el:checkOperator name="IntChar lt" control='<%= True %>' object="${integer1 lt character1}">
                   <el:checkOperator name="CharLong gt" control='<%= False %>' object="${long1 gt character1}">
                    <el:checkOperator name="LongByte <=" control='<%= True %>' object="${byte1 <= long1}">
                     <el:checkOperator name="CharacterLong >=" control='<%= False %>' object="${long1 >= character1}">
                      <el:checkOperator name="ByteCharacter le" control='<%= True %>' object="${byte1 le character1}">
                       <el:checkOperator name="LongLong ge" control='<%= True %>' object="${long1 ge long1}">
                        <el:checkOperator name="StringByte <" control='<%= False %>' object="${string1 < byte1}">
                         <el:checkOperator name="StringCharacter >" control='<%= True %>' object="${character1 > string1}">
                          <el:checkOperator name="StringShort lt" control='<%= False %>' object="${string1 lt short1}">
                           <el:checkOperator name="StringInt gt" control='<%= False %>' object="${string1 gt integer1}">
                            <el:checkOperator name="StringLong <=" control='<%= True %>' object="${string1 <= long1}">
                             <el:checkOperator name="StringFloat >=" control='<%= True %>' object="${string1 >= float1}">
                              <el:checkOperator name="StringDouble le" control='<%= False %>' object="${string1 le double1}">
                               <el:checkOperator name="StringString ge" control='<%= True %>' object="${string1 ge string1}">
                                <el:checkOperator name="BigDecimalString >" control='<%= True %>' object="${bigdecimal1 > string1}">
                                 <el:checkOperator name="BigDecimalBigInteger gt" control='<%= False %>' object="${bigdecimal1 gt biginteger1}">
                                  <el:checkOperator name="BigDecimalInteger <" control='<%= False %>' object="${bigdecimal1 < integer1}">
                                   <el:checkOperator name="BigDecimalShort lt" control='<%= False %>' object="${bigdecimal1 lt short1}">
                                    <el:checkOperator name="BigDecimalLong >=" control='<%= True %>' object="${bigdecimal1 >= long1}">
                                     <el:checkOperator name="BigDecimalDouble ge" control='<%= True %>' object="${bigdecimal1 ge double2}">
                                      <el:checkOperator name="BigDecimalFloat <=" control='<%= True %>' object="${float1 <= bigdecimal1}">
                                       <el:checkOperator name="BigDecimalDouble2 le" control='<%= True %>' object="${double2 le bigdecimal1}">
                                        <el:checkOperator name="FloatBigInteger >" control='<%= False %>' object="${float1 > biginteger1}">
                                         <el:checkOperator name="BigIntegerDouble le" control='<%= False %>' object="${biginteger1 le double1}">
                                          <el:checkOperator name="BigIntegerInteger >" control='<%= True %>' object="${biginteger1 > integer1}">
                                           <el:checkOperator name="BigIntegerChar gt" control='<%= True %>' object="${character1 gt biginteger1}">
                                            <el:checkOperator name="BigIntegerShort <" control='<%= False %>' object="${biginteger1 < short1}">
                                             <el:checkOperator name="BigIntegerByte lt" control='<%= True %>' object="${byte1 lt biginteger1}">
                                              <el:checkOperator name="BigIntegerInteger1 >=" control='<%= True %>' object="${integer2 >= biginteger1}">
                                               <el:checkOperator name="BigIntegerInteger2 ge" control='<%= False %>' object="${integer1 ge biginteger1}">
                                                <el:checkOperator name="bigIntegerInteger3 <=" control='<%= True %>' object="${biginteger1 <= integer2}">
                                                 <el:checkOperator name="bigIntegerInteger4 le" control='<%= True %>' object="${integer2 le biginteger1}"
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



