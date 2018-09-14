/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.singleton.dependson.common;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class HistoryBean {
  private Map<String, List<Integer>> instanceIdsMap = new HashMap<String, List<Integer>>();

  private Map<String, Long> creationTimeMillisMap = new HashMap<String, Long>();

  public List<Integer> getInstanceIdsByBeanName(String beanName) {
    return instanceIdsMap.get(beanName);
  }

  public void addInstanceIdForBeanName(String beanName, int i,
      Object beanInstance) {
    List<Integer> ids = instanceIdsMap.get(beanName);
    if (ids == null) {
      ids = new ArrayList<Integer>();
      instanceIdsMap.put(beanName, ids);
    }
    ids.add(i);
    Helper.getLogger()
        .info(String.format(
            "Adding instanceId %d for beanName %s by beanInstance %s", i,
            beanName, beanInstance));
  }

  public long getCreationTimeMillisByBeanName(String beanName) {
    return creationTimeMillisMap.get(beanName);
  }

  public void addCreationTimeMillisForBeanName(String beanName, long t,
      Object beanInstance) {
    if (creationTimeMillisMap.containsKey(beanName)) {
      throw new IllegalStateException("creationTimeMillis for beanName "
          + beanName + " already exists, which was added by another instance.");
    }
    creationTimeMillisMap.put(beanName, t);
    Helper.getLogger()
        .info(String.format(
            "Adding creationTimeMillis %d for beanName %s by beanInstance %s",
            t, beanName, beanInstance));
  }
}
