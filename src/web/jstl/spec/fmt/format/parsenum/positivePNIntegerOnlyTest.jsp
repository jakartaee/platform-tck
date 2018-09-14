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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="tck" uri="http://java.sun.com/jstltck/jstltck-util" %>
<tck:test testName="positivePNIntegerOnlyTest">
    <fmt:setLocale value="en_US"/>

    <!-- if integerOnly is true, only the integer portion of the value
             is parsed. By default the entire value is parsed if integerOnly
             is not specified. -->
    Number: <fmt:parseNumber value="1,234.56"/>
    Number: <fmt:parseNumber value="1,234.56" integerOnly='<%= false %>'/>
    Number int only: <fmt:parseNumber value="1,234.56" integerOnly="true"/>
    Currency int only: <fmt:parseNumber value="$1,234.56" integerOnly="true" type="currency"/>
    Currency: <fmt:parseNumber value="$1,234.56" integerOnly="false" type="currency"/>
    <%--
      *** Commented out due to J2SE BugID: 4663985 ***
    Percent int only: <fmt:parseNumber value="150%" integerOnly="true" type="percent"/>
    Percent: <fmt:parseNumber value="150%" integerOnly="false" type="percent"/>
    --%>
</tck:test>
