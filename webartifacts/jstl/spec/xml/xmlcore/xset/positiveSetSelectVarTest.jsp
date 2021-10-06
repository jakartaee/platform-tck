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
<tck:test testName="positiveSetSelectVarTest">
    <x:parse var="doc">
        <parent>
            <a>text</a>
        </parent>
    </x:parse>

    <!-- The select attribute specifies the XPath expression to be
             applied against a parsed object.  The result of the select
             will be exported to the PageContent and associated with the
             variable name specified by var. -->
    <x:set var="rxResult" select="$doc//parent"/>
    xResult should evaluated to 'text': <x:out select="$rxResult//a"/><br>
</tck:test>
