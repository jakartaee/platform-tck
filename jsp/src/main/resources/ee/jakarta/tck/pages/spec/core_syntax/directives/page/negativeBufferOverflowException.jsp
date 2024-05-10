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
<title>negativeBufferOverflowException</title>
<body>
<% /**	Name: negativeBufferOverflowException
		Description: Set autoflush to false. Write more
                     date out than the 8KB buffer can handle.
		Result: An exception should be thrown.
**/ %>		
<%@ page autoFlush="false" %>
<% try {
      for (int d = 0; d < 60000; d++ ) {
          out.print(d+"    ");  
      }
   } catch ( Throwable t ) {
       out.clear();
       out.println( "Buffer overflow occurred.  Exception successfully caught." );
       out.println( "Test status: PASS" );
   }
%>
</body>
</html>
