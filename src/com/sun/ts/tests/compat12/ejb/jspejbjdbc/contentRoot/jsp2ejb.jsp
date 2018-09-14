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

<%@ page language="java" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.sun.ts.tests.compat12.ejb.jspejbjdbc.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
 
<%
    int ACCOUNT	= 1075;
    double BALANCE = 10490.75;
    int ACCOUNTS[] = { 1000, 1075, 40, 30564, 387 };
    double BALANCES[] = { 50000.0, 10490.75, 200.50, 25000.0, 1000000.0 };
    java.util.Properties harnessProps = null;
    java.util.Enumeration enum1 = request.getParameterNames();
    if (enum1.hasMoreElements())
        harnessProps = new java.util.Properties();
    while (enum1.hasMoreElements()) {
        String name = (String) enum1.nextElement();
        String value = request.getParameter(name);
        harnessProps.setProperty(name, value);
    }

    Properties p = new Properties();
    Account beanRef = null;
    double balance;
    try {
            TSNamingContext nctx = new TSNamingContext();
            AccountHome  beanHome =
		(AccountHome)nctx.lookup("java:comp/env/ejb/AccountBean",
                                 	 AccountHome.class);
	    TestUtil.logMsg("Create 5 Account EJB's");
	    for(int i=0; i<ACCOUNTS.length; i++) {
	        TestUtil.logMsg("Creating account="+ACCOUNTS[i]+
			", balance="+BALANCES[i]);
		if(i == 0)
		    beanRef = (Account) beanHome.create(ACCOUNTS[i],
			BALANCES[i], true, harnessProps);
		else
		    beanRef = (Account) beanHome.create(ACCOUNTS[i],
			BALANCES[i], false, harnessProps);
	    }
	    TestUtil.logMsg("Find Account EJB of account="+ACCOUNT);
	    beanRef = (Account) beanHome.findTheBean(
				new Integer(ACCOUNT), harnessProps);
	    TestUtil.logMsg("Operating on account: " + ACCOUNT);
	    TestUtil.logMsg("Perform transactions on account: " + ACCOUNT);
	    balance = beanRef.balance();
	    p.setProperty("Balance", "" + balance);
	    TestUtil.logMsg("Balance: " + balance);
	    balance = beanRef.deposit(100.0);
	    p.setProperty("Deposit", "" + balance);
	    TestUtil.logMsg("Deposit: " + balance);
	    balance = beanRef.withdraw(50.0);
	    p.setProperty("Withdraw", "" + balance);
	    TestUtil.logMsg("Withdraw: " + balance);

            TestUtil.logMsg("Clean Up Entity Data");
            for(int i=0; i<ACCOUNTS.length; i++) {
                TestUtil.logMsg("Removing Account EJB's");
                beanRef = (Account) beanHome.findByPrimaryKey(new Integer(ACCOUNTS[i]));
                        if( beanRef != null) {
                        beanRef.remove();
                        }
            }

    } catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }

    StringBuffer lep = new StringBuffer();
    java.util.Enumeration key = p.keys();
    String name; 
    while (key.hasMoreElements())
    {
     name = (String)key.nextElement();
     lep.append(name+"="+p.getProperty(name)+"\n");
    }
%>

<%= lep.toString() %>
