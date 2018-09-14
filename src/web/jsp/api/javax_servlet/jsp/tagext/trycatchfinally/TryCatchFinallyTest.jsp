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
<%@ taglib uri="http://java.sun.com/tck/jsp/trycatchfinally" prefix="try"  %>

<% try { %>
<try:tcf location="attribute">
</try:tcf>
<% } catch (Throwable t) { } %>

<try:tcf location="doStartTag">
</try:tcf>

<try:tcf location="doEndTag">
</try:tcf>

<try:tcf location="doInitBody">
</try:tcf>

<try:tcf location="doAfterBody">
</try:tcf>

<try:tcf location="body">
    <try:throwit />
</try:tcf>

<jsp:useBean id="verifier" scope="page"
             class="com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.trycatchfinally.ResultVerifierBean"/>
<jsp:setProperty name="verifier" property="context" value="<%= pageContext %>" />

${verifier.result}
