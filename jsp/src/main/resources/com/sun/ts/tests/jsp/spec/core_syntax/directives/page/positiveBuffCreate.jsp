<%--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

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

<html>
<title>positiveBuffCreate</title>
<body>
<% /**	Name: positiveBuffCreate
		Description: Create a buffer  size of say 12kb. Keep autoflush
			  set to false(default).Write characters to the out object. Invoke
			  the flush() method on out to flush the output to the client.
		Result:Should return the output that you sent to the client.
**/ %>		
<!-- with buff size 0f 12kb and autoflush false and do flush -->
<%@ page buffer="12kb" autoFlush="false" %>
<% for(int d=0;d<1000;d++) out.print(d+"    "); out.flush(); %>
</body>
</html>
