/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.core.linkjaxbadapter;

import javax.ws.rs.core.Link;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Model {

  public Model(Link link) {
    super();
    this.link = link;
  }

  public Model() {
    link = RuntimeDelegate.getInstance().createLinkBuilder()
        .uri("default://constructor.model/was/used").build();
  }

  @XmlElement
  @XmlJavaTypeAdapter(JaxbAdapterEx.class)
  Link link;

  public Link getLink() {
    return link;
  }

  public void setLink(Link lnk) {
    link = lnk;
  }
}
