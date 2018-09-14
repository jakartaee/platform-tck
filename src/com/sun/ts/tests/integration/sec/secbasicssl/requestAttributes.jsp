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
 @(#)requestAttributes.jsp	1.6 06/02/19
-->

<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="java.security.cert.Certificate" %>
<%@ page import="java.security.cert.X509Certificate" %>

<html>
<body>
<% 
    boolean fail = false;
    String FAILSTRING = "FAILED"; // must be the same for all jsps.
    String PASSSTRING = "PASSED"; // must be the same for all jsps.

    /* testName: request_attributes
    *
    *  @assertion_ids:
    *        Assertion request_attributes
    *
    *       If a request has been transmitted over a secure protocol, such as
    *       HTTPS, this information must be exposed via the isSecure method
    *       of the ServletRequest interface. The web container must expose
    *       the following  attributes to the servlet programmer.
    *           1) The cipher suite
    *           2) the bit size of the algorithm
    *
    *       If there is an SSL certificate associated with the request, it must
    *       be exposed by the servlet container to the servlet programmer as an
    *       array of objects of type java.security.cert.X509Certificate
    *
    *       See Also:
    *            * Servlet 2.3 Specification  section 4.7
    *
    *  @test_strategy:
    *       1. Look for the following request attributes
    *             a) cipher-suite
    *             b) key-size
    *             c) SSL certificate
    *          If any of the above attributes is not set/incorrect,
    *          report test failure.
    *
    *      Note: SSL certificate attribute will be set only if there
    *            is a client certificate involved in SSL connection.
    *            This test doesn't use client certificate
    * 
    *
    */
    String testName = "test_request_attributes";

    String cipherSuiteAttrib = "javax.servlet.request.cipher_suite";
    String keySizeAttrib     = "javax.servlet.request.key_size";
    String certificateAttrib = "javax.servlet.request.X509Certificate";

    String cipherSuite = null;
    Integer    keySize = new Integer(0);
    X509Certificate[] certificates=null;
    try {
        fail = false;

	out.println("Remote User      = " + request.getRemoteUser());
	out.println("Caller principal = " + request.getUserPrincipal());

        if(request.getUserPrincipal() != null)
	   out.println("Caller principal Name = " + request.getUserPrincipal().getName());

        cipherSuite = (String)request.getAttribute(cipherSuiteAttrib);

        //verify cipher-suite attribute
        if( cipherSuite == null ) {
            out.println( testName + ": " + FAILSTRING +
                " - cipher-suite attribute not set" );
            fail = true;
        }else
                out.println( testName + ": cipher-suite : " + cipherSuite );

        keySize =(Integer)request.getAttribute(keySizeAttrib);

        //verify key-size attribute
        if(keySize == null){
                out.println( testName + ": " + FAILSTRING +
                        " - key-size attribute not set" );
                fail = true;
        } else
                out.println( testName + ": key-size : " + keySize.toString() );

        certificates =(X509Certificate[])request.getAttribute(certificateAttrib);

        //verify SSL certificate attribute
        //
        // Note : There is no SSL certificate associated with this request
        //        so the certificates attribute should be null
        //        (i.e certificates = null )

        if(certificates != null){
            out.println( testName + ": " + FAILSTRING +
            " - incorrect SSL certificate attribute" );
            fail = true;
        }

        //else {
        //  for( int i = 0; i < certificates.length; i++ ) {
        //      logRef.log( testName + ": certificate [" +i+"]"
        //          + certificates[i].toString() );
        //  }
        //}

        if( !fail ) {
            out.println( testName + ": " + PASSSTRING );
        }
    }
    catch( Exception e ) {
        out.println( testName + ": " + FAILSTRING +
            " - Exception: " + e.getMessage() );
    }

%>
</body>
</html>
