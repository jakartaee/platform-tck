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

package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override;

import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF;
import com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF;
import javax.ejb.EJB;

/**
 * Adding this superclass to verify that injections on superclass can also be
 * overridden with descriptors.
 */
abstract public class EjbLink2BeanBase extends EjbLinkBeanBase {

  // this ejb-ref is overridden in two_ejb.xml
  @EJB(name = "ejb/EjbLink1Bean", beanName = "no-such-bean-name", description = "should be overridden by ejb-jar.xml")
  private EjbLinkIF bean1;

  // this ejb-ref is overridden in two_ejb.xml
  // beanName is overridden in two_ejb.xml to be two_ejb.jar#EjbLink3Bean. The
  // value here EjbLink3Bean
  // is ambiguous since there are two beans with this name inside this EAR.
  @EJB(name = "ejb/EjbLink3Bean", beanName = "EjbLink3Bean", description = "should be overridden by ejb-jar.xml")
  private EjbLinkIF bean3;

  // this ejb-ref is overridden in two_ejb.xml
  @EJB(name = "ejb/EjbLink1BeanLocal", beanName = "no-such-bean-name", description = "should be overridden by ejb-jar.xml")
  private EjbLinkLocalIF localBean1;

}
