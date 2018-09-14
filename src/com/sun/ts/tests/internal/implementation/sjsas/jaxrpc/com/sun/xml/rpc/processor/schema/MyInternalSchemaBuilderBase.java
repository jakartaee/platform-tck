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

package com.sun.xml.rpc.processor.schema;

import com.sun.xml.rpc.processor.schema.InternalSchemaBuilderBase;
import com.sun.xml.rpc.wsdl.document.schema.SchemaDocument;
import com.sun.xml.rpc.wsdl.document.schema.SchemaElement;
import java.util.Properties;
import java.util.Set;

public class MyInternalSchemaBuilderBase extends InternalSchemaBuilderBase {
  public MyInternalSchemaBuilderBase(SchemaDocument sd, Properties p) {
    super(sd, p);
  }

  public WildcardComponent buildAnyWildcard(SchemaElement se,
      ComplexTypeDefinitionComponent c, InternalSchema i) {
    return super.buildAnyWildcard(se, c, i);
  }

  public void processRestrictionSimpleTypeDefinition(SchemaElement se,
      ComplexTypeDefinitionComponent c, InternalSchema i) {
    super.processRestrictionSimpleTypeDefinition(se, c, i);
  }

  public Set parseSymbolSet(String s, Set set) {
    return super.parseSymbolSet(s, set);
  }

  public void failUnimplemented(String s) {
    super.failUnimplemented(s);
  }

  public void failValidation(String s1, String s2) {
    super.failValidation(s1, s2);
  }

  public void failValidation(String s1, String s2, String s3) {
    super.failValidation(s1, s2, s3);
  }

}
