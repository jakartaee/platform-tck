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

<%@ page
	import="java.math.BigDecimal,java.math.BigInteger"%>
<%@ page contentType="text/plain"%>
<%@ taglib uri="http://java.sun.com/tck/jsp/el" prefix="el"%>
<%
    pageContext.setAttribute("asdf", "asdf");

    Float f1 = new Float(1.5f);
    Float f2 = new Float(1.5e-4f);
    Float f3 = new Float(1.5E+4);
    BigDecimal bd1 = new BigDecimal(1.6d);
    BigInteger bi1 = new BigInteger("5");
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
    pageContext.setAttribute("bigdecimal1", bd1);
    pageContext.setAttribute("biginteger1", bi1);
    Double fPlus = new Double(f1.doubleValue() + f2.doubleValue()
		    + f3.doubleValue());
    Double fMinus = new Double(f1.doubleValue() - f2.doubleValue()
		    - f3.doubleValue());
    Double fMult = new Double(f1.doubleValue() * f2.doubleValue()
		    * f3.doubleValue());
    Double dPlus = new Double(1.5d + 1.5e-4d + 1.5E+4d);
    Double dMinus = new Double(1.5d - 1.5e-4d - 1.5E+4d);
    Double dMult = new Double(1.5d * 1.5e-4d * 1.5E+4d);
    Double dMixPlus = new Double(1.5d + 4d);
    Double dMixMinus = new Double(1.5d - 5d);
    Double dMixMult = new Double(1.5d * 6d);

    BigDecimal bigDecPlus = bd1.add(new BigDecimal(f1.doubleValue()));
    BigDecimal bigDecMinus = bd1.subtract(new BigDecimal(bi1));
    BigDecimal bigDecMult = bd1.multiply(new BigDecimal("1.5e-4"));
    BigDecimal bigDecPlus1 = bd1.add(new BigDecimal(
		    new Short((short) 6).doubleValue()));
    BigDecimal bigDecMinus1 = bd1.subtract(new BigDecimal(
		    new Integer(5).doubleValue()));
    BigDecimal bigDecMult1 = bd1.multiply(new BigDecimal(new Long(4)
		    .doubleValue()));

    BigDecimal bigIntPlusFloat = new BigDecimal(bi1)
		    .add(new BigDecimal(f1.doubleValue()));
    BigDecimal bigIntMinusDouble = new BigDecimal(bi1)
		    .subtract(new BigDecimal("1.5"));
    BigDecimal bigIntMultString = new BigDecimal(bi1)
		    .multiply(new BigDecimal("1.5"));

    BigInteger bigIntPlus = bi1.add(new BigInteger(new Integer(5)
		    .toString()));
    BigInteger bigIntMinus = bi1.subtract(new BigInteger(new Long(4)
		    .toString()));
    BigInteger bigIntMult = bi1.multiply(new BigInteger(new Short(
		    (short) 6).toString()));

    Long lPlus = new Long(4 + 5);
    Long lMinus = new Long(4 - 5);
    Long lMult = new Long(4 * 5);
    Integer iZero = new Integer(0);
%>
<%-- Validates binary operators +, -, * --%>

<%
    try {
%>
${asdf - 6} Test FAILED. Exception did not occur when trying to evaluate
"'asdf' - 6" using binary operator -.
<%
    } catch (Throwable t) {
    }
%>

<%
    try {
%>
${asdf * 6} Test FAILED. Exception did not occur when trying to evaluate
"'asdf' * 6" using binary operator *.
<%
    } catch (Throwable t) {
    }
%>

<el:checkOperator name="Float+" control='<%= fPlus %>'
	object="${float1 + float2 + float3}">
	<el:checkOperator name="Double+" control='<%= dPlus %>'
		object="${double1 + double2 + double3}">
		<el:checkOperator name="Float-" control='<%= fMinus %>'
			object="${float1 - float2 - float3}">
			<el:checkOperator name="Double-" control='<%= dMinus %>'
				object="${double1 - double2 - double3}">
				<el:checkOperator name="String-" control='<%= dMinus %>'
					object="${string1 - string2 - string3}">
					<el:checkOperator name="Mix-" control='<%= dMinus %>'
						object="${float1 - double2 - string3}">
						<el:checkOperator name="Float*" control='<%= fMult %>'
							object="${float1 * float2 * float3}">
							<el:checkOperator name="Double*" control='<%= dMult %>'
								object="${double1 * double2 * double3}">
								<el:checkOperator name="String*" control='<%= dMult %>'
									object="${string1 * string2 * string3}">
									<el:checkOperator name="Mix*" control='<%= dMult %>'
										object="${float1 * double2 * string3}">
										<el:checkOperator name="Null+" control='<%= iZero %>'
											object="${null + null}">
											<el:checkOperator name="Null-" control='<%= iZero %>'
												object="${null - null}">
												<el:checkOperator name="Null*" control='<%= iZero %>'
													object="${null * null}">
													<el:checkOperator name="LongDouble+"
														control='<%= dMixPlus %>' object="${double1 + long1}">
														<el:checkOperator name="IntDouble-"
															control='<%= dMixMinus %>' object="${double1 - integer1}">
															<el:checkOperator name="ShortDouble+"
																control='<%= dMixMult %>' object="${double1 * short1}">
																<el:checkOperator name="Long+" control='<%= lPlus %>'
																	object="${long1 + integer1}">
																	<el:checkOperator name="Long-" control='<%= lMinus %>'
																		object="${long1 - integer1}">
																		<el:checkOperator name="Long+" control='<%= lMult %>'
																			object="${long1 * integer1}">
																			<el:checkOperator name="BigDec+"
																				control='<%= bigDecPlus %>'
																				object="${bigdecimal1 + float1}">
																				<el:checkOperator name="BigDec-"
																					control='<%= bigDecMinus %>'
																					object="${bigdecimal1 - biginteger1}">
																					<el:checkOperator name="BigDec*"
																						control='<%= bigDecMult %>'
																						object="${bigdecimal1 * string2}">
																						<el:checkOperator name="BigDec1+"
																							control='<%= bigDecPlus1 %>'
																							object="${bigdecimal1 + short1}">
																							<el:checkOperator name="BigDec1-"
																								control='<%= bigDecMinus1 %>'
																								object="${bigdecimal1 - integer1}">
																								<el:checkOperator name="BigDec1*"
																									control='<%= bigDecMult1 %>'
																									object="${bigdecimal1 * long1}" display="true" />
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
