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
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.transaction.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.jms.*" %>
<%@ page import="com.sun.ts.lib.util.*" %>
<%@ page import="com.sun.ts.lib.porting.*" %>
<%@ page import="com.sun.ts.tests.jms.ee20.resourcedefs.annotations.MyBean" %>
<%@ page session="false" %>

<%! private static final long serialVersionUID = 1L; %>

<%-- JMS objects --%>
<%! private transient ConnectionFactory		cf = null;
    private transient ConnectionFactory 	dcf = null;
    private transient ConnectionFactory 	cfra = null;
    private transient QueueConnectionFactory 	qcf = null;
    private transient TopicConnectionFactory 	tcf = null;
    private transient TopicConnectionFactory 	dtcf = null;
    private transient Topic 			topic = null;
    private transient Topic 			topica = null;
    private transient Queue 			queue = null;
    private transient JMSContext		context = null;
    private transient JMSConsumer		consumerQ = null;
    private transient JMSProducer		producerQ = null;
    private transient JMSConsumer		consumerT = null;
    private transient JMSProducer		producerT = null;
    private boolean				queueTest = false;
%>

<%-- Harness req's --%>
<%! private Properties              		harnessProps = null; %>

<%-- properties read from ts.jte file --%>
<%! private long                            	timeout;
    private String                          	user;
    private String                          	password;
    private String                          	mode;
%>

<%-- Process the client request --%>
<%! private void doProcessRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
	boolean pass = true;
	Properties p = new Properties();
	res.setContentType("text/plain");
	PrintWriter out = res.getWriter();

	try {
	    String test = harnessProps.getProperty("TEST");
	    System.out.println("doProcessRequest: test to execute is: "+test);
	    if(test.equals("sendAndRecvQueueTestFromJspClient")) {
                if(sendAndRecvQueueTestFromJspClient())
	            p.setProperty("TESTRESULT", "pass");
		else
	            p.setProperty("TESTRESULT", "fail");
	    } else if(test.equals("sendAndRecvTopicTestFromJspClient")) {
                if(sendAndRecvTopicTestFromJspClient())
	            p.setProperty("TESTRESULT", "pass");
		else
	            p.setProperty("TESTRESULT", "fail");
	    } else if(test.equals("checkClientIDTestFromJspClient")) {
                if(checkClientIDTestFromJspClient())
	            p.setProperty("TESTRESULT", "pass");
		else
	            p.setProperty("TESTRESULT", "fail");
	    } else if(test.equals("verifyLookupWithRAElementSpecifiedFromJspClient")) {
                if(verifyLookupWithRAElementSpecifiedFromJspClient())
	            p.setProperty("TESTRESULT", "pass");
		else
	            p.setProperty("TESTRESULT", "fail");
	    } else {
	        p.setProperty("TESTRESULT", "fail");
	    }
	    doCleanup();
	    p.list(out);
	} catch (Exception e) {
	    TestUtil.logErr("doProcessRequest: Exception: " + e);
	    System.out.println("doProcessRequest: Exception: " + e);
	    p.setProperty("TESTRESULT", "fail");
	    p.list(out);
	}
	out.close();
    }
%>

<%-- Cleanup JMS objects after done using them --%>
<%! private void doCleanup() throws Exception {
        try {
            if(queueTest && consumerQ != null) {
                TestUtil.logMsg("Flush any messages left on Queue");
		Message rmsg = consumerQ.receive(timeout);
                while (rmsg != null) {
                    rmsg = consumerQ.receiveNoWait();
                    if (rmsg == null) {
                        rmsg = consumerQ.receiveNoWait();
                    }
		}
	        consumerQ.close();
             }
            if(consumerT != null)
	        consumerT.close();
	    TestUtil.logMsg("Close JMSContext Objects");
            if(context != null)
                context.close();
	} catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
	    throw new Exception("doCleanup failed!", e);
	}
    }
%>

<%-- Lookup JMS Connection Factory and Destination Objects --%>
<%! private void doLookupJMSObjects() throws Exception {
	try {
	    TestUtil.logMsg("Lookup JMS factories defined by @JMSConnectionFactoryDefinitions");
	    TestUtil.logMsg("Lookup JMS destinations defined by @JMSDestinationDefinitions");
	    TSNamingContext namingctx = new TSNamingContext();
	    TestUtil.logMsg("Lookup java:comp/DefaultJMSConnectionFactory");
            dcf = (ConnectionFactory) 
		namingctx.lookup("java:comp/DefaultJMSConnectionFactory");
	    TestUtil.logMsg("Lookup java:global/JSPMyTestConnectionFactory");
            cf = (ConnectionFactory) 
		namingctx.lookup("java:global/JSPMyTestConnectionFactory");
	    TestUtil.logMsg("Lookup java:app/JSPMyTestQueueConnectionFactory");
            qcf = (QueueConnectionFactory) 
		namingctx.lookup("java:app/JSPMyTestQueueConnectionFactory");
	    TestUtil.logMsg("Lookup java:module/JSPMyTestTopicConnectionFactory");
            tcf = (TopicConnectionFactory) 
		namingctx.lookup("java:module/JSPMyTestTopicConnectionFactory");
	    TestUtil.logMsg("Lookup java:comp/env/jms/JSPMyTestDurableTopicConnectionFactory");
            dtcf = (TopicConnectionFactory) 
		namingctx.lookup("java:comp/env/jms/JSPMyTestDurableTopicConnectionFactory");
	    TestUtil.logMsg("Lookup java:global/env/JSPMyTestQueue");
            queue = (Queue) namingctx.lookup("java:global/env/JSPMyTestQueue");
	    TestUtil.logMsg("Lookup java:app/env/JSPMyTestTopic");
            topic = (Topic) namingctx.lookup("java:app/env/JSPMyTestTopic");

	    TestUtil.logMsg("Create JMSContext, JMSProducer's and JMSConsumer's");
            context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
	    producerQ = context.createProducer();
	    consumerQ = context.createConsumer(queue);
	    producerT = context.createProducer();
	    consumerT = context.createConsumer(topic);
	} catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
	    throw e;
	}
    }

    private void doLookupJMSObjectsWithRAElementSpecified() throws Exception {
	try {
	    TestUtil.logMsg(
		"Lookup JMS factories defined in @JMSConnectionFactoryDefinitions with RA specified");
	    TestUtil.logMsg(
		"Lookup JMS destinations defined in @JMSDestinationDefinitions with RA specified");
	    TSNamingContext namingctx = new TSNamingContext();
	    TestUtil.logMsg("Lookup java:comp/env/jms/JSPMyTestConnectionFactoryWithRASpecified");
            cfra = (ConnectionFactory) 
		namingctx.lookup("java:comp/env/jms/JSPMyTestConnectionFactoryWithRASpecified");
	    TestUtil.logMsg("Lookup java:module/env/JSPMyTestTopicWithRASpecified");
            topica = (Topic) 
		namingctx.lookup("java:module/env/JSPMyTestTopicWithRASpecified");
	} catch (Exception e ) {
	    throw new Exception("doLookupJMSObjectsWithRAElementSpecified failed:", e );
	}
    }

    private boolean sendAndRecvQueueTestFromJspClient() {
	boolean pass = true;
	String message = "Where are you!";
        try {
	    queueTest = true;
            // send and receive TextMessage
            TestUtil.logMsg("Creating TextMessage via JMSContext.createTextMessage(String)");
            TextMessage expTextMessage = context.createTextMessage(message);
            TestUtil.logMsg("Set some values in TextMessage");
            expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvQueueTestFromJspClient");
	    TestUtil.logMsg("Sending TextMessage via JMSProducer.send(Destination, Message)");
            producerQ.send(queue, expTextMessage);
            TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
            TextMessage actTextMessage = (TextMessage) consumerQ.receive(timeout);
            if (actTextMessage == null) {
                TestUtil.logErr("Did not receive TextMessage");
		pass = false;
            }
            TestUtil.logMsg("Check the value in TextMessage");
	    if(actTextMessage.getText().equals(expTextMessage.getText())) {
		TestUtil.logMsg("TextMessage is correct");
	    } else {
		TestUtil.logErr("TextMessage is incorrect expected " + expTextMessage.getText() 
			+ ", received " + actTextMessage.getText());
		pass = false;
	    }
        } catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
            pass = false;
        } 

	return pass;
    }

    private boolean sendAndRecvTopicTestFromJspClient() {
	boolean pass = true;
	String message = "Where are you!";
        try {
	    queueTest = false;
            // send and receive TextMessage
            TestUtil.logMsg("Creating TextMessage via JMSContext.createTextMessage(String)");
            TextMessage expTextMessage = context.createTextMessage(message);
            TestUtil.logMsg("Set some values in TextMessage");
            expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTopicTestFromJspClient");
	    TestUtil.logMsg("Sending TextMessage via JMSProducer.send(Destination, Message)");
            producerT.send(topic, expTextMessage);
            TestUtil.logMsg("Receive TextMessage via JMSconsumer.receive(long)");
            TextMessage actTextMessage = (TextMessage) consumerT.receive(timeout);
            if (actTextMessage == null) {
                TestUtil.logErr("Did not receive TextMessage");
		pass = false;
            }
            TestUtil.logMsg("Check the value in TextMessage");
	    if(actTextMessage.getText().equals(expTextMessage.getText())) {
		TestUtil.logMsg("TextMessage is correct");
	    } else {
		TestUtil.logErr("TextMessage is incorrect expected " + expTextMessage.getText() 
			+ ", received " + actTextMessage.getText());
		pass = false;
	    }
        } catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
            pass = false;
        } 
	return pass;
    }

    private boolean checkClientIDTestFromJspClient() {
	boolean pass = true;
	JMSContext context = null;
        try {
	    queueTest = false;
	    TestUtil.logMsg("Create JMSContext from durable topic connection factory");
	    TestUtil.logMsg("Check the client id which is configured as MyClientID in the " +
			"JMSConnectionFactoryDefinition annotation");
            context = dtcf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
	    String clientid = context.getClientID();
	    if(clientid == null) {
		TestUtil.logErr("Client ID value is null (expected MyClientID)");
		pass = false;
	    } else if(clientid.equals("MyClientID")) {
		TestUtil.logMsg("Client ID value is correct (MyClientID)");
	    } else {
		TestUtil.logErr("Client ID value is incorrect (expected MyClientID, got "+clientid+")");
		pass = false;
	    }
        } catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
	    pass = false;
        } finally {
	    try {
		if(context != null)
		    context.close();
	    } catch(Exception e) {}
	}
	return pass;
    }

    public boolean verifyLookupWithRAElementSpecifiedFromJspClient() {
	boolean pass = true;
        try {
	    doLookupJMSObjectsWithRAElementSpecified();
        } catch (Exception e) {
	    TestUtil.logErr("Caught exception: " + e);
            pass = false;
        } 
	return pass;
    }
%>

<%
    MyBean mb = new MyBean();
    // Get the harness properties from the HTTP request object
    harnessProps = new Properties();
    Enumeration enumlist = request.getParameterNames();
    while (enumlist.hasMoreElements()) {
        String name = (String) enumlist.nextElement();
        String value = request.getParameter(name);
        harnessProps.setProperty(name, value);
    }

    // Parse the harness properties
    try {
        TestUtil.init(harnessProps);
        // get props
        timeout = Long.parseLong(harnessProps.getProperty("jms_timeout"));
        user = harnessProps.getProperty("user");
        password = harnessProps.getProperty("password");
        mode = harnessProps.getProperty("platform.mode");

        // check props for errors
        if (timeout < 1) {
            throw new Exception("'jms_timeout' (milliseconds) in ts.jte must be > 0");
        }
        if (user == null ) {
            throw new Exception("'user' in ts.jte must not be null ");
        }
        if (password == null ) {
            throw new Exception("'password' in ts.jte must not be null ");
        }
        if (mode == null) {
            throw new Exception("'platform.mode' in ts.jte must not be null");
        }
        doLookupJMSObjects();
    } catch (Exception e) {
        System.out.println("doPost: Exception: " + e);
        e.printStackTrace();
        throw new ServletException("unable to initialize remote logging in JSP");
    }
    doProcessRequest(request, response);
    harnessProps = null;
%>
