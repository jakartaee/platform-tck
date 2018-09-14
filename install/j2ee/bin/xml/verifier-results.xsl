<?xml version="1.0" encoding="UTF-8"?>
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html"/>
<xsl:param name="output-mode" select="'all'"/>

<xsl:template match="/">
  <html>
    <head>
      <title>Verifier Results</title>
    </head>
    <body>
      <xsl:choose>
        <xsl:when test="static-verification/error">
          <font size="+3">Verifier Errors</font>
            <xsl:text>Error name and description below:</xsl:text><br/>
            <xsl:value-of select="static-verification/error/error-name"/><xsl:text>: </xsl:text>
            <xsl:value-of select="static-verification/error/error-description"/>
        </xsl:when>
        <xsl:otherwise>
          <font size="+3"><center>Verifier Results</center></font><br/>
          <xsl:if test="static-verification/failure-count/failure-number">
            <xsl:text>Failure Count: </xsl:text>
	    <xsl:value-of select="static-verification/failure-count/failure-number"/><br/>
          </xsl:if>
          <xsl:if test="static-verification/failure-count/warning-number">
            <xsl:text>Warning Count: </xsl:text>
	    <xsl:value-of select="static-verification/failure-count/warning-number"/><br/>
          </xsl:if>
          <xsl:if test="static-verification/failure-count/error-number">
            <xsl:text>Error Count: </xsl:text>
	    <xsl:value-of select="static-verification/failure-count/error-number"/><br/>
          </xsl:if>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
    </body>
  </html>
</xsl:template>

<xsl:template match="static-verification">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="application">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>Application Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="ejb">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>EJB Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="web">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>Web Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="appclient">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>Appclient Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="connector">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>Connector Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="other">
  <br/><hr WIDTH="100%"/><br/>
  <u><font size="+2"><bold>Other Test Results</bold></font></u><br/>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="failed">
  <br/>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr><th colspan="3">Failed Tests</th></tr>
    <tr>
      <th>Test Name</th>
      <th>Test Assertion</th>
      <th>Test Description</th>
    </tr>
    <xsl:apply-templates select="test"/>
  </table>
</xsl:template>

<xsl:template match="passed">
  <xsl:if test="$output-mode='all'">
  <br/>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr><th colspan="3">Passed Tests</th></tr>
    <tr>
      <th>Test Name</th>
      <th>Test Assertion</th>
      <th>Test Description</th>
    </tr>
    <xsl:apply-templates select="test"/>
  </table>
  </xsl:if>
</xsl:template>

<xsl:template match="warning">
  <br/>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr><th colspan="3">Warning Tests</th></tr>
    <tr>
      <th>Test Name</th>
      <th>Test Assertion</th>
      <th>Test Description</th>
    </tr>
    <xsl:apply-templates select="test"/>
  </table>
</xsl:template>

<xsl:template match="not-applicable">
  <xsl:if test="$output-mode='all'">
  <br/>
  <table border="1" cellpadding="4" cellspacing="0">
    <tr><th colspan="3">Not Applicable Tests</th></tr>
    <tr>
      <th>Test Name</th>
      <th>Test Assertion</th>
      <th>Test Description</th>
    </tr>
    <xsl:apply-templates select="test"/>
  </table>
  </xsl:if>
</xsl:template>

<xsl:template match="test">
  <tr valign="top">
    <td><xsl:value-of select="test-name"/></td>
    <td><xsl:value-of select="test-assertion"/></td>
    <td><xsl:value-of select="test-description"/></td>
  </tr>
</xsl:template>

<xsl:template match="failure-count"/>

</xsl:stylesheet>
