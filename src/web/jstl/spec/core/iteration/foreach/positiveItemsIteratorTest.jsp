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
<%@ page import="java.util.HashSet,java.util.Iterator,java.util.ArrayList,java.util.Arrays" %>
<tck:test testName="positiveItemsIteratorTest">
    <%
        ArrayList lTest = new ArrayList();
        ArrayList lControl = new ArrayList();

        HashSet hSet = new HashSet();
        hSet.add("val1");
        hSet.add("val2");
        hSet.add("var3");
        hSet.add("var4");
        pageContext.setAttribute("hashSet", hSet.iterator());
        for (Iterator rIter = hSet.iterator(); rIter.hasNext(); ) {
            lControl.add(rIter.next());
        }

    %>
    <!-- RT: check for support if iteration via a passed Iterator -->
    <c:forEach var="rVar" items='<%= hSet.iterator() %>'>
        <%
              lTest.add(pageContext.findAttribute("rVar"));
        %>
    </c:forEach>

    <%
        String[] test =
            (String[]) lTest.toArray(new String[lTest.size()]);
        String[] control =
            (String[]) lControl.toArray(new String[lControl.size()]);

        Arrays.sort(test);
        Arrays.sort(control);

        if (!Arrays.equals(test, control)) {
            out.println("Test FAILED.  Iteration produced unexpected results.");
            out.println("Expected values:");
            for (int i = 0; i < control.length; i++) {
                out.println('\t' + control[i]);
            }
            out.println("Values received:");
            for (int i = 0; i < test.length; i++) {
                out.println('\t' + test[i]);
            }
        }

    %>

</tck:test>
