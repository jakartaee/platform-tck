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
<tck:test testName="positiveCWOTest">
    <x:parse var="doc">
        <a attr="attrvalue">
            a-text
        </a>
    </x:parse>


    <!-- Validate that the first nested when within a choose,
              whose select condition evaluates to true will process
              its body content.  This means no other body content
              from subsequent whens that could evaluated to true,
              would be evaluated. -->
    <x:choose>
        <x:when select="$doc//a[@attr='attrvalue']">
            Body content properly processed.<br>
        </x:when>
        <x:when select="$doc//a[@attr='attrvalue']">
            Body content improperly proccessed.<br>
        </x:when>
    </x:choose>

    <!-- Validate that the first (not the first physical action)
              nested when within a choose,
              whose select condition evaluates to true will process
              its body content.  This means no other body content
              from subsequent whens that could evaluated to true,
              or the body content of an otherwise action
              would be evaluated. -->
    <x:choose>
        <x:when select="$doc//a[@attr='ttrvalue']">
            Body content improperly processed.<br>
        </x:when>
        <x:when select="$doc//a[@attr='attrvalue']">
            Body content properly proccessed.<br>
        </x:when>
        <x:otherwise>
            Otherwise body content improperly processed.<br>
        </x:otherwise>
    </x:choose>

    <!-- Validate that if no when action evaluates to true and an
             othewise action is not present, nothing is written to the
             current JspWriter. -->
    <x:choose>
        <x:when select="$doc//a[@attr='ivalid']">
            Body content improperly processed.<br>
        </x:when>
    </x:choose>

    <!-- Validate that if no when action evaluates to true and
             an otherwise action is present, the body of the otherwise
             action is written to the current JspWriter. -->
    <x:choose>
        <x:when select="$doc//a[@attr='ivalid']">
            Body content improperly processed.<br>
        </x:when>
        <x:when select="$doc//a[@attr='ivalid']">
            Body content improperly processed.<br>
        </x:when>
        <x:otherwise>
            Body content properly processed.<br>
        </x:otherwise>
    </x:choose>
</tck:test>
