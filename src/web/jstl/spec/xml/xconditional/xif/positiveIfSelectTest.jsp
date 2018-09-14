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

<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveIfSelectTest">
    <x:parse var="doc">
        <a attr="attribute">text</a>
    </x:parse>

    <!-- If the Xpath expression evaluates to true, the body content
             of the action is written to the current JspWriter. -->
    <br><br>XPath expression will evaluate to true, body content should be displayed:<br>
    <x:if select="$doc//a[name(@attr)]">
        Test PASSED: Element with attr attribute exists.<br>
    </x:if>
    <br>XPath expression will evaluate to false, body content should not be displayed:<br>
    <x:if select="$doc//b">
        Test FAILS if this is displayed.<br>
    </x:if>
</tck:test>
