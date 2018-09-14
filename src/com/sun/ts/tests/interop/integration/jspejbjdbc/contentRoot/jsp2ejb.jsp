<%--

    Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.

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

<!--
 @(#)jsp2ejb.jsp	1.10 06/02/19
-->

<%@ page language="java" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.sun.ts.tests.integration.session.jspejbjdbc.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
 
<%
    String TELLERNAME = "joe";
    int ACCOUNT = 1075;
    java.util.Properties harnessProps = null;
    java.util.Enumeration enum1 = request.getParameterNames();
    if (enum1.hasMoreElements())
        harnessProps = new java.util.Properties();
    while (enum1.hasMoreElements()) {
        String name = (String) enum1.nextElement();
        String value = request.getParameter(name);
        harnessProps.setProperty(name, value);
    }

    Properties transactionProps = new Properties();
    double balance;
    try {
            TSNamingContext nctx = new TSNamingContext();
            TellerHome  beanHome =
		(TellerHome)nctx.lookup("java:comp/env/ejb/TellerBean",
                                 	 TellerHome.class);
            Teller beanRef = beanHome.create(TELLERNAME, harnessProps);
	    balance = beanRef.balance(ACCOUNT);
	    transactionProps.setProperty("Balance", "" + balance);
	    balance = beanRef.deposit(ACCOUNT, 100.0);
	    transactionProps.setProperty("Deposit", "" + balance);
	    balance = beanRef.withdraw(ACCOUNT, 50.0);
	    transactionProps.setProperty("Withdraw", "" + balance);
    } catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }

    StringBuffer lep = new StringBuffer();
    java.util.Enumeration key = transactionProps.keys();
    String name; 
    while (key.hasMoreElements())
    {
     name = (String)key.nextElement();
     lep.append(name+"="+transactionProps.getProperty(name)+"\n");
    }
%>

<%= lep.toString() %>
