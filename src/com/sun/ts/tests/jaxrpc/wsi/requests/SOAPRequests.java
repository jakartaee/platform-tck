/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jaxrpc.wsi.requests;

public interface SOAPRequests {
  public static final String HELLOWORLD = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String HELLOWORLD_WITH_HANDLER = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Header><cts:test env:actor='http://conformance-checker.org' xmlns:cts='http://cts.org'>response</cts:test></env:Header><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String HELLOWORLD_WITH_CONFORMANCE = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'> <env:Header> <wsi:Claim conformsTo='http://ws-i.org/profiles/basic1.0/' xmlns:wsi='http://ws-i.org/schemas/conformanceClaim/' /></env:Header><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String HELLOWORLD_WITH_MULTIPLE_CONFORMANCE = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'> <env:Header> <wsi:Claim conformsTo='http://ws-i.org/profiles/basic1.0/' xmlns:wsi='http://ws-i.org/schemas/conformanceClaim/' /><wsi:Claim conformsTo='http://dummy/conformanceClaim' xmlns:wsi='http://ws-i.org/schemas/conformanceClaim/' /></env:Header><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String ALWAYS_THROWS_EXCEPTION = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://faulttestservice.org/wsdl' xmlns:ns1='http://faulttestservice.org/types'><env:Body><ns0:alwaysThrowsException/></env:Body></env:Envelope>";

  public static final String ALWAYS_THROWS_SERVER_EXCEPTION = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://faulttestservice.org/wsdl' xmlns:ns1='http://faulttestservice.org/types'><env:Body><ns0:alwaysThrowsServerException/></env:Body></env:Envelope>";

  public static final String BAD_SOAP_ENVELOPE = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://not.soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String MUST_UNDERSTAND_HEADER = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Header><foo:Bar xmlns:foo='http://foo.org/bar/' env:actor='http://schemas.xmlsoap.org/soap/actor/next' env:mustUnderstand='1'>BAZ</foo:Bar></env:Header><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String NON_EXISTANT_OPERATION = "<?xml version='1.0' encoding='UTF-8'?> <!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --> <env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:nonExistantOperation/></env:Body></env:Envelope>";

  public static final String BAD_SOAP_ENVELOPE_WITH_HEADER = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://not.soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Header><foo:Bar xmlns:foo='http://foo.org/bar/' env:actor='http://foo.org/actor' env:mustUnderstand='1'>BAZ</foo:Bar></env:Header><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String BAD_SOAP_ENVELOPE_NON_EXISTANT_OPERATION = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://not.soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:nonExistantOperation/></env:Body></env:Envelope>";

  public static final String MUST_UNDERSTAND_HEADER_NON_EXISTANT_OPERATION = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Header><foo:Bar xmlns:foo='http://foo.org/bar/' env:actor='http://schemas.xmlsoap.org/soap/actor/next' env:mustUnderstand='1'>BAZ</foo:Bar></env:Header><env:Body><ns0:nonExistantOperation/></env:Body></env:Envelope>";

  public static final String SOAP_ACTION_HELLO_WORLD = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://soapactionservice.org/wsdl/' xmlns:ns1='http://soapactionservice.org/types/'><env:Body><ns0:helloWorld/></env:Body></env:Envelope>";

  public static final String SOAP_ACTION_ECHO_STRING = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://soapactionservice.org/wsdl/' xmlns:ns1='http://soapactionservice.org/types/'><env:Header><cts:test env:actor='http://conformance-checker.org' xmlns:cts='http://cts.org'>response</cts:test></env:Header><env:Body><ns0:echoString><str><ans1:p1 xmlns:ans1=\"http://soapactionservice.org/xsd\">echo</ans1:p1></str></ns0:echoString></env:Body></env:Envelope>";

  public static final String ARRAY_OPERATION = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:arrayOperation/></env:Body></env:Envelope>";

  public static final String ONE_WAY_OPERATION = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://simpletestservice.org/wsdl' xmlns:ns1='http://simpletestservice.org/types'><env:Body><ns0:oneWayOperation/></env:Body></env:Envelope>";

  public static final String ECHO_STRING = "<?xml version='1.0' encoding='UTF-8'?><!-- Copyright (c) 2003 Oracle Corporation.  All rights reserved. --><env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:enc='http://schemas.xmlsoap.org/soap/encoding/' xmlns:ns0='http://soapactionservice.org/wsdl/' xmlns:ns1='http://soapactionservice.org/types/'><env:Header><cts:test env:actor='http://conformance-checker.org' xmlns:cts='http://cts.org'>response</cts:test></env:Header><env:Body><ns0:echoString><str><ans1:p1 xmlns:ans1=\"http://soapactionservice.org/xsd\">echo</ans1:p1></str></ns0:echoString></env:Body></env:Envelope>";

  public static final String R0007_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr0007testservice.org/W2JRLR0007TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoString>\n" + "\t\t\t<str>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr0007testservice.org/xsd\">R0007-1</ans1:p1>\n"
      + "\t\t\t</str>\n" + "\t\t</ns0:echoString>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R0007_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr0007testservice.org/W2JRLR0007TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr0007testservice.org/xsd\">{0}</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R1011_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr1011testservice.org/W2JRLR1011TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoString>\n" + "\t\t\t<str>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr1011testservice.org/xsd\">R1011-1</ans1:p1>\n"
      + "\t\t\t</str>\n" + "\t\t</ns0:echoString>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R1011_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr1011testservice.org/W2JRLR1011TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr1011testservice.org/xsd\">{0}</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R1012_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr1012testservice.org/W2JRLR1012TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoString>\n" + "\t\t\t<str>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr1012testservice.org/xsd\">R1012-1</ans1:p1>\n"
      + "\t\t\t</str>\n" + "\t\t</ns0:echoString>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R1012_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr1012testservice.org/W2JRLR1012TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr1012testservice.org/xsd\">{0}</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R2301_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://rpclitservice.org/wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:getBean/>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R2729_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr2729testservice.org/W2JRLR2729TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoString>\n" + "\t\t\t<str>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr2729testservice.org/xsd\">R2729</ans1:p1>\n"
      + "\t\t\t</str>\n" + "\t\t</ns0:echoString>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R2744_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr2744testservice.org/W2JRLR2744TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr2744testservice.org/xsd\">{0}</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R2745_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr2745testservice.org/W2JRLR2745TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr2745testservice.org/xsd\">{0}</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R4001_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr4001testservice.org/W2JRLR4001TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoString>\n" + "\t\t\t<str>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr4001testservice.org/xsd\">R4001-1</ans1:p1>\n"
      + "\t\t\t</str>\n" + "\t\t</ns0:echoString>\n" + "\t</env:Body>\n"
      + "</env:Envelope>\n";

  public static final String R4001_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\"\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:ns0=\"http://w2jrlr4001testservice.org/W2JRLR4001TestService.wsdl\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n" + "\t\t\t<result>\n"
      + "\t\t\t\t<ans1:p1 xmlns:ans1=\"http://w2jrlr4001testservice.org/xsd\">R4001-2</ans1:p1>\n"
      + "\t\t\t</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";

  public static final String R1109_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"
      + "<env:Envelope\n"
      + "\txmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"\n"
      + "\txmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n"
      + "\txmlns:ns0=\"http://soapactionservice.org/wsdl/\">\n"
      + "\t<env:Body>\n" + "\t\t<ns0:echoStringResponse>\n"
      + "\t\t\t<result>{0}</result>\n" + "\t\t</ns0:echoStringResponse>\n"
      + "\t</env:Body>\n" + "</env:Envelope>\n";
}
