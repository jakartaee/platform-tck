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

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jstl/xml" %>
<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveParseVarTest">
    <!-- EL: The var attribute specifies the variable name with with
             to associate the parse result with in the PageContext.
             The type is implementation specific, so verify that it's
             an instance of java.lang.Object. -->
    <x:parse xml="<test>xmltext</test>" var="doc1"/>
    Exported var: <tck:isInstance varName="doc1" type="java.lang.Object"/>

    <!-- RT: The var attribute specifies the variable name with with
             to associate the parse result with in the PageContext.
             The type is implementation specific, so verify that it's
             an instance of java.lang.Object. -->
    <x_rt:parse xml="<test>xmltext</test>" var="rdoc1"/>
    Exported var: <tck:isInstance varName="rdoc1" type="java.lang.Object"/>
</tck:test>
