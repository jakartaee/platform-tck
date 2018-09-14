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
<tck:test testName="positiveFNMinFracDigitsTest">
    <fmt:setLocale value="en_US"/>

    <!-- minFractionDigits specifies the minimum number of fractional digits in
             the formatted value -->
    <fmt:formatNumber value="1234.1" minFractionDigits='<%= 6 %>'/>
    <fmt:formatNumber value=".53" type="percent" minFractionDigits="6"/>
    <fmt:formatNumber value="1234.1" type="currency" minFractionDigits="6"/>
</tck:test>
