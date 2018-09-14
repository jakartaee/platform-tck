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
<tck:test testName="positiveVarForEachTest">
     <x:parse var="doc">
        <a>
            <b>btext1</b>
            <b>btext2</b>
        </a>
    </x:parse>

   <!-- If var is specified, a nested scoped variable is made availabe
             and signifies the current item of the iteration. The type of the
             nested var is dependent on the result of the XPath expression. -->
    The type of iter in this case is a node-set --> java.lang.Object<br>
    Two iterations should occur.<br>
    <c:set var="iter" value="nested var"/>
    <x:forEach var="iter" select="$doc//b">
        <tck:isInstance varName="iter" type="java.lang.Object"/>
        <x:out select="$iter"/><br>
    </x:forEach>
    <c:out value="${iter}" default="<strong>iter</strong> correctly removed after action completed" escapeXml="false"/>
</tck:test>
