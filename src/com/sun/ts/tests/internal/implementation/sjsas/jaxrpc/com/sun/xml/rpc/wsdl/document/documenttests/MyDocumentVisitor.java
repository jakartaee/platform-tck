/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.document.documenttests;

import com.sun.xml.rpc.wsdl.document.*;

public class MyDocumentVisitor extends WSDLDocumentVisitorBase
    implements WSDLDocumentVisitor {

  public void preVisit(Definitions definitions) throws Exception {
    System.out.println("preVisit: " + definitions);
  }

  public void postVisit(Definitions definitions) throws Exception {
    System.out.println("postVisit: " + definitions);
  }

  public void preVisit(Message message) throws Exception {
    System.out.println("preVisit: " + message);
  }

  public void postVisit(Message message) throws Exception {
    System.out.println("postVisit: " + message);
  }

  public void preVisit(Types types) throws Exception {
    System.out.println("preVisit: " + types);
  }

  public void postVisit(Types types) throws Exception {
    System.out.println("postVisit: " + types);
  }

  public void preVisit(PortType portType) throws Exception {
    System.out.println("preVisit: " + portType);
  }

  public void postVisit(PortType portType) throws Exception {
    System.out.println("postVisit: " + portType);
  }

  public void preVisit(Service service) throws Exception {
    System.out.println("preVisit: " + service);
  }

  public void postVisit(Service service) throws Exception {
    System.out.println("postVisit: " + service);
  }

  public void preVisit(Port port) throws Exception {
    System.out.println("preVisit: " + port);
  }

  public void postVisit(Port port) throws Exception {
    System.out.println("postVisit: " + port);
  }

  public void preVisit(Input input) throws Exception {
    System.out.println("preVisit: " + input);
  }

  public void postVisit(Input input) throws Exception {
    System.out.println("postVisit: " + input);
  }

  public void preVisit(Output output) throws Exception {
    System.out.println("preVisit: " + output);
  }

  public void postVisit(Output output) throws Exception {
    System.out.println("postVisit: " + output);
  }

  public void preVisit(BindingInput input) throws Exception {
    System.out.println("preVisit: " + input);
  }

  public void postVisit(BindingInput input) throws Exception {
    System.out.println("postVisit: " + input);
  }

  public void preVisit(BindingOutput output) throws Exception {
    System.out.println("preVisit: " + output);
  }

  public void postVisit(BindingOutput output) throws Exception {
    System.out.println("postVisit: " + output);
  }

  public void preVisit(BindingOperation operation) throws Exception {
    System.out.println("preVisit: " + operation);
  }

  public void postVisit(BindingOperation operation) throws Exception {
    System.out.println("postVisit: " + operation);
  }

  public void visit(Documentation documentation) throws Exception {
    System.out.println("visit: " + documentation);
  }

  public void visit(MessagePart messagePart) throws Exception {
    System.out.println("visit: " + messagePart);
  }
}
