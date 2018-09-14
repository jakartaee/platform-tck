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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveIfVarTest">
    <x:parse var="doc">
        <a attr="attribute">text</a>
    </x:parse>

    <!-- If var is specified, the result of the test condition
             is stored in the specified variable name. The type of 
             the exported var is java.lang.Boolean. -->
    XPath expression will evaluate to true, exported var should evaluate to true.<br>
    Body content should be processed.<br>
    <x:if select="$doc//a[name(@attr)]" var="rtResult">
        XPath expression evaluated to true.<br>
    </x:if>
    <tck:isInstance varName="rtResult" type="java.lang.Boolean"/>
    Var is: <c:out value="${rtResult}" default="Test FAILED"/><br><br>

    XPath expression will evaluate to false, exported var should evaluate to false.<br>
    Body content should be not be processed.<br>
    <x:if select="$doc//b" var="rtResult">
        XPath expression evaluated to true.<br>
    </x:if>
    <tck:isInstance varName="rtResult" type="java.lang.Boolean"/>
    Var is: <c:out value="${rtResult}" default="Test FAILED"/><br><br>

    <!-- An empty body is also valid for x:if.  -->
    If tag with an empty body and var is specified.<br>
    XPath expression will evaluated to true, exported var should evaluate to true.<br>
    <x:if select="$doc//a[name(@attr)]" var="retResult"/>
    <tck:isInstance varName="retResult" type="java.lang.Boolean"/>
    Var is: <c:out value="${retResult}" default="Test FAILED"/><br><br>
    
    If tag with an empty body and var is specified.<br>
    XPath expression will evaluate to false, exported var should evaluated to false.<br>
    <x:if select="$doc//b" var="refResult"/>
    <tck:isInstance varName="refResult" type="java.lang.Boolean"/>
    Var is: <c:out value="${refResult}" default="Test FAILED"/><br><br>
</tck:test>
