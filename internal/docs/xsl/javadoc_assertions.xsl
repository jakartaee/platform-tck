<?xml version="1.0"?>
<!--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:Assertions="http://busgo1208.us.oracle.com/CTS/XMLassertions" version="1.0">
	<xsl:output method="html" indent="yes"/>
	<xsl:strip-space elements="description"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>JavaDoc Assertion Detail</title>
			</head>
			<body bgcolor="white">
				<br/>
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="javadoc">
		<CENTER>
			<h2>
				<xsl:value-of select="name"/>
				<br/>
				<xsl:value-of select="id"/> - <xsl:value-of select="version"/>
				<br/>
				JavaDoc Assertion Detail 
			</h2>
		</CENTER>
		<xsl:variable name="TECHNOLOGY" select="technology"/>
		<xsl:call-template name="assertions">
			<xsl:with-param name="technology" select="$TECHNOLOGY"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="assertions">
		<xsl:param name="technology"/>
		<xsl:call-template name="DISPLAY_ASSERTION_COUNT"/>
		<!-- Create the table header -->
		<TABLE width="900" border="1" frame="box" cellpadding="1" summary="Javadoc Assertions">
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">ID</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Return</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Method/Field</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Description</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Required</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Deprecated</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Testable</FONT>
			</TH>
			<xsl:for-each select="assertions">
				<xsl:for-each select="assertion">
                                    <xsl:choose>
                                       <xsl:when test="@status!='removed'">

					<!-- Create the row -->
					<TR>
						<TD align="center" valign="center">
							<xsl:element name="a">
							  <xsl:attribute name="name">
							    <xsl:variable name="the-id">
   				                              <xsl:value-of select="normalize-space(id)"/>
				                            </xsl:variable>
			                                    <xsl:choose>
				                              <xsl:when test="contains($the-id, 'JAVADOC')">
 					                        <xsl:value-of select="$the-id"/>
				                              </xsl:when>
				                              <xsl:otherwise>
					                        <xsl:value-of select="$technology"/>:JAVADOC:<xsl:value-of select="$the-id"/>
				                              </xsl:otherwise>
				                            </xsl:choose>
                                                          </xsl:attribute>
							</xsl:element>
							<font size="1PT">
                                                          <xsl:variable name="the-id">
				                            <xsl:value-of select="normalize-space(id)"/>
				                          </xsl:variable>
			                                  <xsl:choose>
				                            <xsl:when test="contains($the-id, 'JAVADOC')">
					                      <xsl:value-of select="$the-id"/>
				                            </xsl:when>
				                            <xsl:otherwise>
					                      <xsl:value-of select="$technology"/>:JAVADOC:<xsl:value-of select="$the-id"/>
				                            </xsl:otherwise>
				                          </xsl:choose>
							</font>
						</TD>
						<xsl:choose>
							<xsl:when test="count(method)!=0">
								<xsl:choose>
									<xsl:when test="count(method/@return-type)!=0 ">
										<TD align="center" valign="center">
											<font size="1PT">
												<xsl:call-template name="GETNAME">
													<xsl:with-param name="name" select="method/@return-type"/>
												</xsl:call-template>
											</font>
										</TD>
									</xsl:when>
									<xsl:otherwise>
										<TD align="left" valign="center">
											<font size="1PT">
												<br/>
											</font>
										</TD>
									</xsl:otherwise>
								</xsl:choose>
								<TD align="left" valign="center">
									<font size="1PT">
										<xsl:value-of select="package"/>.<xsl:value-of select="class-interface"/>.<xsl:value-of select="method/@name"/>
										<br/>
										<xsl:choose>
											<xsl:when test="count(method/parameters)!=0 ">
												<xsl:call-template name="parameters">
													<xsl:with-param name="parameters" select="method/parameters"/>
												</xsl:call-template>
											</xsl:when>
										</xsl:choose>
										<xsl:if test="count(method/throw)!=0">
											<br/>throws
											<xsl:call-template name="GETNAME">
												<xsl:with-param name="name" select="method/throw"/>
											</xsl:call-template>
											<br/>
										</xsl:if>
									</font>
								</TD>
							</xsl:when>
							<xsl:otherwise>
								<!-- the field -->
								<xsl:choose>
									<xsl:when test="count(field)!=0 ">
										<xsl:choose>
											<xsl:when test="count(field/@type)!=0 ">
												<TD align="center" valign="center">
													<font size="1PT">
														<xsl:call-template name="GETNAME">
															<xsl:with-param name="name" select="field/@type"/>
														</xsl:call-template>
													</font>
												</TD>
											</xsl:when>
											<xsl:otherwise>
												<TD align="left" valign="center">
													<font size="1PT">
														<br/>
													</font>
												</TD>
											</xsl:otherwise>
										</xsl:choose>
										<TD align="left" valign="center">
											<font size="1PT">
												<xsl:value-of select="package"/>.<xsl:value-of select="class-interface"/>.<xsl:value-of select="field/@name"/>
												<br/>
												<xsl:choose>
													<xsl:when test="count(method/parameters)!=0 ">
														<xsl:call-template name="parameters">
															<xsl:with-param name="parameters" select="method/parameters"/>
														</xsl:call-template>
													</xsl:when>
												</xsl:choose>
												<xsl:if test="count(method/throw)!=0">
													<br/>throws
											<xsl:call-template name="GETNAME">
														<xsl:with-param name="name" select="method/throw"/>
													</xsl:call-template>
													<br/>
												</xsl:if>
											</font>
										</TD>
									</xsl:when>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="description != ''">
								<TD align="left" valign="center">
									<font size="1PT">
										<xsl:value-of select="description"/>
									</font>
								</TD>
							</xsl:when>
							<xsl:otherwise>
								<TD align="left" valign="center">
									<font size="1PT">
										<br/>
									</font>
								</TD>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="count(@required)!=0">
								<TD align="center" valign="center">
									<font size="1PT">
										<xsl:value-of select="@required"/>
									</font>
								</TD>
							</xsl:when>
							<xsl:otherwise>
								<TD align="center" valign="center">
									<font size="1PT">
										<br/>
									</font>
								</TD>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="@status='deprecated'">
								<TD align="center" valign="center">
									<font size="1PT">
										<xsl:value-of select="'true'"/>
									</font>
								</TD>
							</xsl:when>
							<xsl:otherwise>
								<TD align="center" valign="center">
									<font size="1PT">
										<br/>
									</font>
								</TD>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test="count(@testable)!=0">
								<TD align="center" valign="center">
									<font size="1PT">
										<xsl:value-of select="@testable"/>
									</font>
								</TD>
							</xsl:when>
							<xsl:otherwise>
								<TD align="center" valign="center">
									<font size="1PT">
										<br/>
									</font>
								</TD>
							</xsl:otherwise>
						</xsl:choose>
					</TR>
                                      </xsl:when>
                                    </xsl:choose>
				</xsl:for-each>
			</xsl:for-each>
		</TABLE>
	</xsl:template>
	<xsl:template name="parameters">
		<xsl:param name="parameters"/>

	(
		<xsl:variable name="num-params" select="count($parameters/parameter)"/>
		<xsl:for-each select="$parameters/parameter">
			<xsl:call-template name="GETNAME">
				<xsl:with-param name="name" select="."/>
			</xsl:call-template>
			<xsl:if test="position() &lt; $num-params">
				,<br/>
			</xsl:if>
		</xsl:for-each>
	)
	</xsl:template>
	<xsl:template name="GETNAME">
		<xsl:param name="name"/>
		<xsl:choose>
			<xsl:when test="contains($name,'.')">
				<xsl:call-template name="GETNAME">
					<xsl:with-param name="name" select="substring-after($name,'.')"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="DISPLAY_ASSERTION_COUNT">
		<xsl:variable name="Total" select="count(descendant::node()[@required])"/>
		<xsl:variable name="Total_Active" select='count(descendant::node()[@status="active"])'/>
		<xsl:variable name="Total_Deprecated" select='count(descendant::node()[@status="deprecated"])'/>
		<xsl:variable name="Total_Removed" select='count(descendant::node()[@status="removed"])'/>
		<xsl:variable name="Total_Required" select='count(descendant::node()[@required="true"])'/>
		<xsl:variable name="Total_Required_Active" select='count(descendant::node()[@required="true" and @status="active"])'/>
		<xsl:variable name="Total_Required_Deprecated" select='count(descendant::node()[@required="true" and @status="deprecated"])'/>
		<xsl:variable name="Total_Required_Removed" select='count(descendant::node()[@required="true" and @status="removed"])'/>
		<xsl:variable name="Total_Optional" select='count(descendant::node()[@required="false"])'/>
		<xsl:variable name="Total_Optional_Active" select='count(descendant::node()[@required="false" and @status="active"])'/>
		<xsl:variable name="Total_Optional_Deprecated" select='count(descendant::node()[@required="false" and @status="deprecated"])'/>
		<xsl:variable name="Total_Optional_Removed" select='count(descendant::node()[@required="false" and @status="removed"])'/>
		<TABLE width="900" border="1" frame="box" cellpadding="5">
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Totals</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Total</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Active</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Deprecated</FONT>
			</TH>
			<TH bgcolor="#BFBFBF">
				<FONT size="1PT">Removed</FONT>
			</TH>
			<tr>
				<TD align="center" valign="center">
					<font size="2PT">
						# of Assertions
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Active"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Deprecated"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Removed"/>
					</font>
				</TD>
			</tr>
			<tr>
				<TD align="center" valign="center">
					<font size="2PT">
						# of Required Assertions
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Required"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Required_Active"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Required_Deprecated"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Required_Removed"/>
					</font>
				</TD>
			</tr>
			<tr>
				<TD align="center" valign="center">
					<font size="2PT">
						# of Optional Assertions
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Optional"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Optional_Active"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Optional_Deprecated"/>
					</font>
				</TD>
				<TD align="center" valign="center">
					<font size="2PT">
						<xsl:value-of select="$Total_Optional_Removed"/>
					</font>
				</TD>
			</tr>
		</TABLE>
		<br/>
	</xsl:template>
</xsl:stylesheet>
