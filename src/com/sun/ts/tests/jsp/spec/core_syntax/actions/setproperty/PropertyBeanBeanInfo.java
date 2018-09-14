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

/*
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;

import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.SimpleBeanInfo;
import java.util.Arrays;

public class PropertyBeanBeanInfo extends SimpleBeanInfo {
  private PropertyDescriptor[] pd = null;

  public PropertyBeanBeanInfo() {

  }

  /**
   *
   * @return an array of PropertyDescriptors describing the PropertyBean's
   *         exposed properties.
   */
  public PropertyDescriptor[] getPropertyDescriptors() {
    if (pd == null) {
      try {
        pd = new PropertyDescriptor[] {
            new PropertyDescriptor("PString", PropertyBean.class),
            new PropertyDescriptor("PBoolean", PropertyBean.class),
            new PropertyDescriptor("PInteger", PropertyBean.class) };

        pd[0].setPropertyEditorClass(PStringPropertyEditor.class);
        pd[1].setPropertyEditorClass(PBooleanPropertyEditor.class);
        pd[2].setPropertyEditorClass(PIntegerPropertyEditor.class);
      } catch (IntrospectionException ie) {
        pd = super.getPropertyDescriptors();
      }
    }
    return Arrays.copyOf(pd, pd.length);
  }

}// PropertyBeanBeanInfo
