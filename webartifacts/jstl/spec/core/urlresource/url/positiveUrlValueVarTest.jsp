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

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positiveUrlValueVarTest">

     <!-- validate that the url action's url attribute can accept
             both RT and dynamic values. Validate that the returned
             value is the same as that returned by response.encodeURL() -->
     <c:url var="rtVal" value='<%= "/rewrite" %>'/>
     <c:url var="rtsVal" value="/rewrite"/>
     <%
        String encodeVal = response.encodeURL("/jstl_core_url_web/rewrite");
        String rtValue = (String) pageContext.getAttribute("rtVal");
        String rtsValue = (String) pageContext.getAttribute("rtsVal");
        if (rtValue.equals(encodeVal) && rtsValue.equals(encodeVal)) {
            out.println("The &lt;c:url&gt; action returned expected value.<br>");
        } else {
            out.println("The &lt;c:url&gt; action returned value different from that " +
                        "returned by response.encodeUrl()!<br>");
            out.println("From url action: " + rtValue);
            out.println("From encodeURL: " + encodeVal);
        }
     %>
</tck:test>
