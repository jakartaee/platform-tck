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
<%@ taglib uri="http://java.sun.com/tck/jsp/tagsupport" prefix="tag"  %>

<%-- Validate synchronization when iterating over body --%>
Iteration synchronization
--------------------------------------------
<%! int counter = 2; %>
<tag:sync bodyCount="2" doStartTag="EVAL_BODY_INCLUDE" doEndTag="EVAL_PAGE">
    <%
        int beg = (begin != null) ? begin.intValue() : 0;
        int nes = (nested != null) ? nested.intValue() : 0;
        if (counter == 2) {
            // doStartTag() called initial value of 1
           if (beg < 1) {
               out.println("Test FAILED.  Variable 'begin' not synchronized after" +
                  " doStartTag().  Expected a value of '1'.  Received: " + beg);
           }
           if (beg > 1) {
               out.println("Test FAILED. Variable 'begin' seems to have been" +
                   " synchronized too many times after doStartTag().  Expected " +
                   "a value of '1'.  Received: " + beg);
           }
           if (nes < 1) {
               out.println("Test FAILED.  Variable 'nested' not synchronized after" +
                   " doStartTag().  Expected a value of '1'.  Received: " + nes);
           }
           if (nes > 1) {
               out.println("Test FAILED. Variable 'nested' seems to have been" +
                   " synchronized too many times after doStartTag().  Expected " +
                   "a value of '1'.  Received: " + nes);
           }
       } else {
           // doAfterBody() called with EVAL_BODY_AGAIN new value of 2
           if (beg < 2) {
               out.println("Test FAILED.  Variable 'begin' not synchronized after" +
                   " doAfterBody().  Expected a value of '2'.  Received: " + beg);
           }
           if (beg > 2) {
               out.println("Test FAILED. Variable 'begin' seems to have been" +
                   " synchronized too many times after doAfterBody().  Expected " +
                   "a value of '2'.  Received: " + beg);
           }
           if (nes != 2) {
               out.println("Test FAILED.  Variable 'nested' not synchronized after" +
                   " doAfterBody().  Expected a value of '2'.  Received: " + nes);
           }
           if (nes > 2) {
               out.println("Test FAILED. Variable 'nested' seems to have been" +
                   " synchronized too many times after doAfterBody().  Expected " +
                   "a value of '2'.  Received: " + nes);
           }
       }
       counter--;
    %>
</tag:sync>
<%
    // doAfterBody() has been called with SKIP_BODY, then doEndTag() value
    // incremented by 2.
    int beg = (begin != null) ? begin.intValue() : 0;
    int eend = (end != null) ? end.intValue() : 0;
    if (beg < 4) {
        out.println("Test FAILED.  Variable 'begin' not synchronized after" +
            " doEndTag().  Expected a value of '4'.  Received: " + beg);
    }
    if (beg > 4) {
        out.println("Test FAILED. Variable 'begin' seems to have been" +
            " synchronized too many times after doEndTag().  Expected " +
            "a value of '4'.  Received: " + beg);
    }
    if (eend < 4) {
        out.println("Test FAILED. Variable 'end' not synchronized after " +
            " doEndTag().  Expected a value of '4'.  Received: " + eend);
    }
    if (eend > 4) {
        out.println("Test FAILED. Variable 'end' seems to have been" +
            " synchronized too many times after doEndTag().  Expected " +
            "a value of '4'.  Received: " + eend);
    }
    pageContext.removeAttribute("begin");
    pageContext.removeAttribute("end");
%>

<%-- Validate synchronization when doStartTag() returns SKIP_BODY --%>
Tag Synchronization
--------------------------------------------
<tag:sync doStartTag="SKIP_BODY" doEndTag="EVAL_PAGE" bodyCount="0" />
<%
    // doStartTag() and doEndTag() have each been called once.
    beg = (begin != null) ? begin.intValue() : 0;
    eend = (end != null) ? end.intValue() : 0;
    if (beg < 2) {
        out.println("Test FAILED.  Variable 'begin' not synchronized after" +
            " doStartTag() and/or doEndTag().  Expected a value of '2'.  Received: " + beg);
    }
    if (beg > 2) {
        out.println("Test FAILED. Variable 'begin' seems to have been" +
            " synchronized too many times after doStartTag() and/ordoEndTag().  Expected " +
            "a value of '2'.  Received: " + beg);
    }
    if (eend < 2) {
        out.println("Test FAILED. Variable 'end' not synchronized after " +
            " doStartTag() and/or doEndTag().  Expected a value of '2'.  Received: " + eend);
    }
    if (eend > 2) {
        out.println("Test FAILED. Variable 'end' seems to have been" +
            " synchronized too many times after doStartTag() and/or doEndTag().  Expected " +
            "a value of '2'.  Received: " + eend);
    }
    pageContext.removeAttribute("begin");
    pageContext.removeAttribute("end");
%>
