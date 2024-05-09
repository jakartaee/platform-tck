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

<%@ page contentType="text/plain" import="java.io.IOException" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<%!
    private void cleanup(PageContext pageContext) {
        pageContext.removeAttribute("applicationScopeAttr");
        pageContext.removeAttribute("applAddAttr");
        pageContext.removeAttribute("pageScopeAttr");
        pageContext.removeAttribute("requestScopeAttr");
        pageContext.removeAttribute("sessionScopeAttr");
        pageContext.removeAttribute("applicationScopeAttr");
        pageContext.removeAttribute("pageAddAttr");
        pageContext.removeAttribute("reqAddAttr");
        pageContext.removeAttribute("sessAddAttr");
        pageContext.removeAttribute("applAddAttr");
    }
%>

<%!
    private void initTest(PageContext pageContext) {
        pageContext.setAttribute("pageScopeAttr", "pageScope");
        pageContext.setAttribute("pageScopeRem", "pageScopeRem");
        pageContext.setAttribute("requestScopeAttr", "requestScope", PageContext.REQUEST_SCOPE);
        pageContext.setAttribute("requestScopeRem", "requestScopeRem", PageContext.REQUEST_SCOPE);
        pageContext.setAttribute("sessionScopeAttr", "sessionScope", PageContext.SESSION_SCOPE);
        pageContext.setAttribute("sessionScopeRem", "sessionScopeRem", PageContext.SESSION_SCOPE);
        pageContext.setAttribute("applicationScopeAttr", "applicationScope", PageContext.APPLICATION_SCOPE);
        pageContext.setAttribute("applicationScopeRem", "applicationScopeRem", PageContext.APPLICATION_SCOPE);
    }
%>

<%!
    private void validate(PageContext pageContext) throws IOException {
        JspWriter out = pageContext.getOut();
        String pageScope = (String) pageContext.getAttribute("pageScopeAttr");
        String reqScope = (String) pageContext.getAttribute("requestScopeAttr", PageContext.REQUEST_SCOPE);
        String sessScope = (String) pageContext.getAttribute("sessionScopeAttr", PageContext.SESSION_SCOPE);
        String applScope = (String) pageContext.getAttribute("applicationScopeAttr", PageContext.APPLICATION_SCOPE);
        String pageAdd = (String) pageContext.getAttribute("pageAddAttr");
        String reqAdd = (String) pageContext.getAttribute("reqAddAttr", PageContext.REQUEST_SCOPE);
        String sessAdd = (String) pageContext.getAttribute("sessAddAttr", PageContext.SESSION_SCOPE);
        String applAdd = (String) pageContext.getAttribute("applAddAttr", PageContext.APPLICATION_SCOPE);
        String pageRem = (String) pageContext.getAttribute("pageScopeRem");

        if ("pageScope".equals(pageScope)) {
            if ("requestScopeMod".equals(reqScope)) {
                if ("sessionScopeMod".equals(sessScope)) {
                    if ("applicationScopeMod".equals(applScope)) {
                        if (pageContext.getAttribute("requestScopeRem") == null) {
                            if (pageContext.getAttribute("sessionScoperem") == null) {
                                if (pageContext.getAttribute("applicationScopeRem") == null) {
                                    if ("pageScopeRem".equals(pageRem)) {
                                        if (pageAdd == null) {
                                            if ("reqAdd".equals(reqAdd)) {
                                                if ("sessAdd".equals(sessAdd)) {
                                                    if ("applAdd".equals(applAdd)) {
                                                        out.println("Test PASSED");
                                                    } else {
                                                        out.println("Test FAILED.  Application scoped " +
                                                            "variable added to wrapper not reflected in invoking context.");
                                                    }
                                                } else {
                                                    out.println("Test FAILED.  Session scoped " +
                                                        "variable added to wrapper not reflected in invoking context.");
                                                }
                                            } else {
                                                out.println("Test FAILED.  Request scoped " +
                                                    "variable added to wrapper not reflected in invoking context.");
                                            }
                                        } else {
                                            out.println("Test FAILED.  Page scoped variable added to wrapper should not" +
                                                " have been synchronized with the invoking context");
                                        }
                                    } else {
                                        out.println("Test FAILED.  The page scoped variable removed from the wrapper " +
                                            "should not have been removed from the invoking context.");
                                    }
                                } else {
                                    out.println("Test FAILED.  The application scoped variable that was removed from the " +
                                        "wrapper was not removed from the invoking context.");
                                }
                            } else {
                                out.println("Test FAILED.  The session scoped variable that was removed from the " +
                                    "wrapper was not removed from the invoking context.");
                            }
                        } else {
                            out.println("Test FAILED.  The request scoped variable that was removed from the " +
                                "wrapper was not removed from the invoking context.");
                        }
                    } else {
                        out.println("Test FAILED.  The application scoped variable that was overwritten in the wrapper " +
                            "was not synchronized with the invoking context.  Expected 'applicationScopeMod', received: " + applScope);
                    }
                } else {
                    out.println("Test FAILED.  The session scoped variable that was overwritten in the wrapper " +
                        "was not synchronized with the invoking context.  Expected 'sessionScopeMod', received: " + sessScope);
                }
            } else {
                out.println("Test FAILED.  The request scoped variable that was overwritten in the wrapper " +
                    "was not synchronized with the invoking context.  Expected 'requestScopeMod', received: " + reqScope);
            }
        } else {
            out.println("Test FAILED.  The page scoped variable originally defined in the invoking context " +
                "was modified by a change in the wrapper context.");
        }
    }
%>

<%--
    For each invocation to the tag, the JSP Context Wrapper must
    present a clean page scope containing no initial elements.
    All scopes other than the page scope must be identical to those
    in the Invoking JSP Context and must be modified accordingly when updates
    are made to those scopes in the JSP Context Wrapper.  Any modifications
    to the page scope, however, must not affect the Invoking JSP Context.
--%>

<%-- Init the PageContext scopes --%>
<%
    initTest(pageContext);
%>

<%-- invoke the Tag --%>
<tags:JspContextWrapperScopesTag />

<%-- post-invocation checks --%>
<%
    validate(pageContext);
%>

<%-- post test cleanup --%>
<%
    cleanup(pageContext);
%>

<%-- Invoke the tag again...no change in behavior should occur --%>
<%
    initTest(pageContext);
%>

<tags:JspContextWrapperScopesTag />

<%
    validate(pageContext);
%>

<%
    cleanup(pageContext);
%>
