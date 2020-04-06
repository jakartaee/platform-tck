/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002, 2020 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.wsdlImport.file.nested3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import jakarta.ejb.SessionBean;
import jakarta.ejb.SessionContext;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class StockQuotePortTypeImpl implements SessionBean {

  private static final float stockPrices[] = { -1.0f, 24.25f, 45.5f };

  private static final String STOCKSYMBOL1 = "GTE";

  private static final String STOCKSYMBOL2 = "GE";

  public void ejbCreate() {
  }

  public void ejbActivate() {
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public TradePrice getLastTradePrice(GetLastTradePrice glt)
      throws RemoteException {
    TradePrice tp = new TradePrice();
    String tickerSymbol = glt.getTickerSymbol();
    if (tickerSymbol.equals(STOCKSYMBOL1))
      tp.setPrice(stockPrices[1]);
    else if (tickerSymbol.equals(STOCKSYMBOL2))
      tp.setPrice(stockPrices[2]);
    else
      tp.setPrice(stockPrices[0]);
    return tp;
  }
}
