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
<title>Precompilation Test</title>
<body>
<%  /**  Name:Precompilation 
         Description: Checks if the request is actually delivered
                      to this page or not if the request
                      parameter jsp_precompile is either not set
	                  or set to "true".
              
         Result: Will send the message "Got The request"  
                 if the jsp_precompile="false"
                 
                
**/ %>

Got the request....The test parameter is
<%! String test; %>

<%
  test=request.getParameter("test"); 
  if(test==null) //just to remove the dependency on Java 
  test="null" ;
%>
<%=test %>
</body>
</html>
