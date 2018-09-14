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
 * @(#)Schema.java	1.20 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

public class Schema {
  public static final int CUSTOMERREF = 1;

  public static final int ORDERREF = 2;

  public static final int ALIASREF = 3;

  public static final int PRODUCTREF = 4;

  public static final int NUMOFCUSTOMERS = 14;

  public static final int NUMOFORDERS = 16;

  public static final int NUMOFALIASES = 30;

  public static final int NUMOFPRODUCTS = 18;

  public static final int NUMOFADDRESSES = 28;

  public static final int NUMOFHOMEADDRESSES = 14;

  public static final int NUMOFWORKADDRESSES = 14;

  public static final int NUMOFPHONES = 28;

  public static final int NUMOFCREDITCARDS = 20;

  public static final int NUMOFLINEITEMS = 44;

  public static final int NUMOFSPOUSES = 6;

  public static final int NUMOFINFODATA = 6;

  public static final String CustomerBean = "java:comp/env/ejb/Customer";

  public static final String OrderBean = "java:comp/env/ejb/Order";

  public static final String AliasBean = "java:comp/env/ejb/Alias";

  public static final String ProductBean = "java:comp/env/ejb/Product";

  public static CustomerHome customerHome = null;

  public static OrderHome orderHome = null;

  public static AliasHome aliasHome = null;

  public static ProductHome productHome = null;

  public static PhoneDVC phoneDVC[] = new PhoneDVC[50];

  public static AddressDVC addressDVC[] = new AddressDVC[50];

  public static Country countryDVC[] = new Country[50];

  public static CreditCardDVC creditCardDVC[] = new CreditCardDVC[50];

  public static LineItemDVC lineItemDVC[] = new LineItemDVC[50];

  public static SpouseDVC spouseDVC[] = new SpouseDVC[6];

  public static InfoDVC infoDVC[] = new InfoDVC[6];

  private static Customer customerRef[] = new Customer[50];

  private static Order orderRef[] = new Order[50];

  private static Alias aliasRef[] = new Alias[50];

  private static Product productRef[] = new Product[50];

  public static Properties props = null;

  private static TSNamingContext nctx = null;

  public static void setup(Properties p) throws Exception {
    TestUtil.logTrace("Schema.setup");

    TestUtil.logMsg("Obtain Naming Context");
    nctx = new TSNamingContext();

    TestUtil.logMsg("Lookup CustomerBean: " + CustomerBean);
    customerHome = (CustomerHome) nctx.lookup(CustomerBean, CustomerHome.class);
    TestUtil.logMsg("Lookup OrderBean: " + OrderBean);
    orderHome = (OrderHome) nctx.lookup(OrderBean, OrderHome.class);
    TestUtil.logMsg("Lookup AliasBean: " + AliasBean);
    aliasHome = (AliasHome) nctx.lookup(AliasBean, AliasHome.class);
    TestUtil.logMsg("Lookup ProductBean: " + ProductBean);
    productHome = (ProductHome) nctx.lookup(ProductBean, ProductHome.class);
    try {
      TestUtil.logMsg("Check if Schema already exists in Persistent Storage");
      if (SchemaAlreadyExists())
        return;

      TestUtil.logMsg("Begin creating Schema in Persistent Storage");
      TestUtil.logMsg("Create Schema DVC Data");
      createSchemaDVCs(true);

      TestUtil.logMsg("Create Schema EJB Data");
      createSchemaEJBs(p);
      TestUtil.logMsg("Done creating Schema in Persistent Storage");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      RemoveSchemaEJBs();
      throw new Exception("Exception occurred: " + e);
    }
  }

  private static boolean SchemaAlreadyExists() throws Exception {
    boolean schemaExists = true;

    TestUtil.logTrace("Schema.SchemaAlreadyExists");

    Collection custCol = customerHome.findAllCustomers();
    Collection prodCol = productHome.findAllProducts();
    Collection orderCol = orderHome.findAllOrders();
    Collection aliasCol = aliasHome.findAllAliases();

    if (custCol.size() != NUMOFCUSTOMERS || prodCol.size() != NUMOFPRODUCTS
        || orderCol.size() != NUMOFORDERS || aliasCol.size() != NUMOFALIASES) {
      TestUtil.logMsg("Number of customers found = " + custCol.size());
      TestUtil.logMsg("Number of products found = " + prodCol.size());
      TestUtil.logMsg("Number of orders found = " + orderCol.size());
      TestUtil.logMsg("Number of aliases found = " + aliasCol.size());
      schemaExists = false;
    }

    if (schemaExists) {
      TestUtil.logMsg("Schema already exists in Persistent Storage");
      createSchemaDVCs(false);
      return true;
    } else {
      TestUtil.logMsg("Schema does not exist in Persistent Storage");
      RemoveSchemaEJBs();
      return false;
    }
  }

  private static void RemoveSchemaEJBs() {
    TestUtil.logTrace("Schema.RemoveSchemaEJBs");

    try {
      Collection col = customerHome.findAllCustomers();
      Iterator i = col.iterator();
      while (i.hasNext()) {
        Customer cref = (Customer) PortableRemoteObject.narrow(i.next(),
            Customer.class);
        try {
          cref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
      col = productHome.findAllProducts();
      i = col.iterator();
      while (i.hasNext()) {
        Product pref = (Product) PortableRemoteObject.narrow(i.next(),
            Product.class);
        try {
          pref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
      col = orderHome.findAllOrders();
      i = col.iterator();
      while (i.hasNext()) {
        Order oref = (Order) PortableRemoteObject.narrow(i.next(), Order.class);
        try {
          oref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
      col = aliasHome.findAllAliases();
      i = col.iterator();
      while (i.hasNext()) {
        Alias aref = (Alias) PortableRemoteObject.narrow(i.next(), Alias.class);
        try {
          aref.remove();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private static void createSchemaDVCs(boolean printMsgs) throws Exception {
    Vector v1 = null;
    Vector v2 = null;

    if (printMsgs)
      TestUtil.logTrace("Schema.createSchemaDVCs");

    if (printMsgs)
      TestUtil.logMsg("Create Phone Data [28 phone numbers]");
    phoneDVC[0] = new PhoneDVC("1", "617", "664-8122");
    phoneDVC[1] = new PhoneDVC("2", "781", "442-8122");
    phoneDVC[2] = new PhoneDVC("3", "508", "662-7117");
    phoneDVC[3] = new PhoneDVC("4", "781", "442-4488");
    phoneDVC[4] = new PhoneDVC("5", "992", "223-8888");
    phoneDVC[5] = new PhoneDVC("6", "781", "442-1134");
    phoneDVC[6] = new PhoneDVC("7", "442", "883-1597");
    phoneDVC[7] = new PhoneDVC("8", "781", "442-6699");
    phoneDVC[8] = new PhoneDVC("9", "603", "777-7890");
    phoneDVC[9] = new PhoneDVC("10", "781", "442-2323");
    phoneDVC[10] = new PhoneDVC("11", "603", "889-2355");
    phoneDVC[11] = new PhoneDVC("12", "781", "442-9876");
    phoneDVC[12] = new PhoneDVC("13", "222", "767-3124");
    phoneDVC[13] = new PhoneDVC("14", "781", "442-1111");
    phoneDVC[14] = new PhoneDVC("15", "222", "767-8898");
    phoneDVC[15] = new PhoneDVC("16", "781", "442-4444");
    phoneDVC[16] = new PhoneDVC("17", null, "564-9087");
    phoneDVC[17] = new PhoneDVC("18", "781", "442-5341");
    phoneDVC[18] = new PhoneDVC("19", null, null);
    phoneDVC[19] = new PhoneDVC("20", "781", "442-1585");
    phoneDVC[20] = new PhoneDVC("21", "207", "532-6354");
    phoneDVC[21] = new PhoneDVC("22", "781", "442-0845");
    phoneDVC[22] = new PhoneDVC("23", "913", null);
    phoneDVC[23] = new PhoneDVC("24", "781", "442-7465");
    phoneDVC[24] = new PhoneDVC("25", "678", "663-6091");
    phoneDVC[25] = new PhoneDVC("26", "781", "442-2139");
    phoneDVC[26] = new PhoneDVC("27", "890", "670-9138");
    phoneDVC[27] = new PhoneDVC("28", "781", "442-0230");

    if (printMsgs)
      TestUtil.logMsg("Create Country Data [14 countries]");
    countryDVC[0] = new Country("United States", "USA");
    countryDVC[1] = new Country("United States", "USA");
    countryDVC[2] = new Country("United States", "USA");
    countryDVC[3] = new Country("United States", "USA");
    countryDVC[4] = new Country("United States", "USA");
    countryDVC[5] = new Country("United States", "USA");
    countryDVC[6] = new Country("United States", "USA");
    countryDVC[7] = new Country("United States", "USA");
    countryDVC[8] = new Country("United States", "USA");
    countryDVC[9] = new Country("United States", "USA");
    countryDVC[10] = new Country("England", "GBR");
    countryDVC[11] = new Country("Ireland", "IRE");
    countryDVC[12] = new Country("China", "CHA");
    countryDVC[13] = new Country("Japan", "JPN");

    if (printMsgs)
      TestUtil.logMsg("Create Address Data [28 addresses]");
    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[0]);
    v2.add(phoneDVC[1]);
    addressDVC[0] = new AddressDVC("1", "1 Oak Road", "Bedford", "MA", "02155",
        v1);
    addressDVC[1] = new AddressDVC("2", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[2]);
    v2.add(phoneDVC[3]);
    addressDVC[2] = new AddressDVC("3", "10 Griffin Road", "Lexington", "MA",
        "02277", v1);
    addressDVC[3] = new AddressDVC("4", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[4]);
    v2.add(phoneDVC[5]);
    addressDVC[4] = new AddressDVC("5", "125 Moxy Lane", "Swansea", "MA",
        "11345", v1);
    addressDVC[5] = new AddressDVC("6", "1 Network Drive", "Burlington", "MA",
        "11345", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[6]);
    v2.add(phoneDVC[7]);
    addressDVC[6] = new AddressDVC("7", "2654 Brookline Avenue", "Brookline",
        "MA", "11678", v1);
    addressDVC[7] = new AddressDVC("8", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[8]);
    v2.add(phoneDVC[9]);
    addressDVC[8] = new AddressDVC("9", "100 Forrest Drive", "Hudson", "NH",
        "78654", v1);
    addressDVC[9] = new AddressDVC("10", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[10]);
    v2.add(phoneDVC[11]);
    addressDVC[10] = new AddressDVC("11", "200 Elliot Road", "Nashua", "NH",
        "65447", v1);
    addressDVC[11] = new AddressDVC("12", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[12]);
    v2.add(phoneDVC[13]);
    addressDVC[12] = new AddressDVC("13", "634 Goldstar Road", "Peabody", "MA",
        "88444", v1);
    addressDVC[13] = new AddressDVC("14", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[14]);
    v2.add(phoneDVC[15]);
    addressDVC[14] = new AddressDVC("15", "100 Forrest Drive", "Peabody", "MA",
        "88444", v1);
    addressDVC[15] = new AddressDVC("16", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[16]);
    v2.add(phoneDVC[17]);
    addressDVC[16] = new AddressDVC("17", "18 Rosewood Avenue", null, "MA",
        "87653", v1);
    addressDVC[17] = new AddressDVC("18", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[18]);
    v2.add(phoneDVC[19]);
    addressDVC[18] = new AddressDVC("19", null, "Belmont", "VT", "23083", v1);
    addressDVC[19] = new AddressDVC("20", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[20]);
    v2.add(phoneDVC[21]);
    addressDVC[20] = new AddressDVC("21", "3212 Boston Road", "Chelmsford",
        "MA", "01824", v1);
    addressDVC[21] = new AddressDVC("22", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[22]);
    v2.add(phoneDVC[23]);
    addressDVC[22] = new AddressDVC("23", "212 Edgewood Drive", "Claremont",
        "NH", "58976", v1);
    addressDVC[23] = new AddressDVC("24", "1 Network Drive", "Burlington", null,
        "00252", v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[24]);
    v2.add(phoneDVC[25]);
    addressDVC[24] = new AddressDVC("25", "47 Skyline Drive", "Attleboro", "MA",
        "76656", v1);
    addressDVC[25] = new AddressDVC("26", "1 Network Drive", "Burlington", "MA",
        null, v2);

    v1 = new Vector();
    v2 = new Vector();
    v1.add(phoneDVC[26]);
    v2.add(phoneDVC[27]);
    addressDVC[26] = new AddressDVC("27", "4 Rangeway Road", "Lawrence", "RI",
        "53026", v1);
    addressDVC[27] = new AddressDVC("28", "1 Network Drive", "Burlington", "MA",
        "00252", v2);

    if (printMsgs)
      TestUtil.logMsg("Create Credit Card Data [20 creditcards]");
    creditCardDVC[0] = new CreditCardDVC("1", "1234-2567-1222-9999", "VISA",
        "04/02", true, (double) 5579);
    creditCardDVC[1] = new CreditCardDVC("2", "3455-9876-1221-0060", "MCARD",
        "10/03", false, (double) 15000);
    creditCardDVC[2] = new CreditCardDVC("3", "1210-1449-2200-3254", "AXP",
        "11/02", true, (double) 3000);
    creditCardDVC[3] = new CreditCardDVC("4", "0002-1221-0078-0890", "VISA",
        "05/03", true, (double) 8000);
    creditCardDVC[4] = new CreditCardDVC("5", "1987-5555-8733-0011", "VISA",
        "05/03", true, (double) 2500);
    creditCardDVC[5] = new CreditCardDVC("6", "0000-0011-2200-3087", "MCARD",
        "11/02", true, (double) 23000);
    creditCardDVC[6] = new CreditCardDVC("7", "3341-7610-8880-9910", "AXP",
        "10/04", true, (double) 13000);
    creditCardDVC[7] = new CreditCardDVC("8", "2222-3333-4444-5555", "MCARD",
        "12/03", true, (double) 2000);
    creditCardDVC[8] = new CreditCardDVC("9", "8888-2222-0090-1348", "AXP",
        "01/02", true, (double) 4500);
    creditCardDVC[9] = new CreditCardDVC("10", "1762-5094-8769-3117", "VISA",
        "06/01", true, (double) 14000);
    creditCardDVC[10] = new CreditCardDVC("11", "1234-1234-1234-9999", "MCARD",
        "09/03", true, (double) 7000);
    creditCardDVC[11] = new CreditCardDVC("12", "9876-9876-1234-5678", "VISA",
        "04/04", false, (double) 1000);
    creditCardDVC[12] = new CreditCardDVC("13", "7777-8888-9999-0012", "MCARD",
        "01/02", true, (double) 3500);
    creditCardDVC[13] = new CreditCardDVC("14", "9099-8808-7718-4455", "AXP",
        "03/05", true, (double) 4400);
    creditCardDVC[14] = new CreditCardDVC("15", "7653-7901-2397-1768", "AXP",
        "02/04", true, (double) 5000);
    creditCardDVC[15] = new CreditCardDVC("16", "8760-8618-9263-3322", "VISA",
        "04/05", false, (double) 750);
    creditCardDVC[16] = new CreditCardDVC("17", "9870-2309-6754-3210", "MCARD",
        "03/03", true, (double) 500);
    creditCardDVC[17] = new CreditCardDVC("18", "8746-8754-9090-1234", "AXP",
        "08/04", false, (double) 1500);
    creditCardDVC[18] = new CreditCardDVC("19", "8736-0980-8765-4869", "MCARD",
        "09/02", true, (double) 5500);
    creditCardDVC[19] = new CreditCardDVC("20", "6745-0979-0970-2345", "VISA",
        "02/05", true, (double) 1400);

    if (printMsgs)
      TestUtil.logMsg("Create LineItem Data [44 lineitems]");
    lineItemDVC[0] = new LineItemDVC("1", 1);
    lineItemDVC[1] = new LineItemDVC("2", 1);
    lineItemDVC[2] = new LineItemDVC("3", 1);
    lineItemDVC[3] = new LineItemDVC("4", 1);
    lineItemDVC[4] = new LineItemDVC("5", 1);
    lineItemDVC[5] = new LineItemDVC("6", 1);
    lineItemDVC[6] = new LineItemDVC("7", 1);
    lineItemDVC[7] = new LineItemDVC("8", 1);
    lineItemDVC[8] = new LineItemDVC("9", 1);
    lineItemDVC[9] = new LineItemDVC("10", 1);
    lineItemDVC[10] = new LineItemDVC("11", 1);
    lineItemDVC[11] = new LineItemDVC("12", 1);
    lineItemDVC[12] = new LineItemDVC("13", 1);
    lineItemDVC[13] = new LineItemDVC("14", 1);
    lineItemDVC[14] = new LineItemDVC("15", 1);
    lineItemDVC[15] = new LineItemDVC("16", 1);
    lineItemDVC[16] = new LineItemDVC("17", 1);
    lineItemDVC[17] = new LineItemDVC("18", 1);
    lineItemDVC[18] = new LineItemDVC("19", 1);
    lineItemDVC[19] = new LineItemDVC("20", 1);
    lineItemDVC[20] = new LineItemDVC("21", 1);
    lineItemDVC[21] = new LineItemDVC("22", 1);
    lineItemDVC[22] = new LineItemDVC("23", 1);
    lineItemDVC[23] = new LineItemDVC("24", 1);
    lineItemDVC[24] = new LineItemDVC("25", 1);
    lineItemDVC[25] = new LineItemDVC("26", 1);
    lineItemDVC[26] = new LineItemDVC("27", 1);
    lineItemDVC[27] = new LineItemDVC("28", 1);
    lineItemDVC[28] = new LineItemDVC("29", 1);
    lineItemDVC[29] = new LineItemDVC("30", 5);
    lineItemDVC[30] = new LineItemDVC("31", 3);
    lineItemDVC[31] = new LineItemDVC("32", 8);
    lineItemDVC[32] = new LineItemDVC("33", 1);
    lineItemDVC[33] = new LineItemDVC("34", 1);
    lineItemDVC[34] = new LineItemDVC("35", 6);
    lineItemDVC[35] = new LineItemDVC("36", 1);
    lineItemDVC[36] = new LineItemDVC("37", 2);
    lineItemDVC[37] = new LineItemDVC("38", 3);
    lineItemDVC[38] = new LineItemDVC("39", 5);
    lineItemDVC[39] = new LineItemDVC("40", 3);
    lineItemDVC[40] = new LineItemDVC("41", 2);
    lineItemDVC[41] = new LineItemDVC("42", 1);
    lineItemDVC[42] = new LineItemDVC("43", 1);
    lineItemDVC[43] = new LineItemDVC("44", 3);

    if (printMsgs)
      TestUtil
          .logMsg("Create Spouse Info Data [6 entries of spouse info data]");
    infoDVC[0] = new InfoDVC("1", "634 Goldstar Road", "Peabody", "MA",
        "88444");
    infoDVC[1] = new InfoDVC("2", "3212 Boston Road", "Chelmsford", "MA",
        "01824");
    infoDVC[2] = new InfoDVC("3", "47 Skyline Drive", "Attleboro", "MA",
        "76656");
    infoDVC[3] = new InfoDVC("4", null, "Belmont", "VT", "23083");
    infoDVC[4] = new InfoDVC("5", "212 Edgewood Drive", "Claremont", "NH",
        "58976");
    infoDVC[5] = new InfoDVC("6", "11 Richmond Lane", "Chatham", "NJ", "65490");

    if (printMsgs)
      TestUtil.logMsg("Create Spouse Data [6 spouses]");
    spouseDVC[0] = new SpouseDVC("1", "Kathleen", "Jones", "Porter",
        "034-58-0988", infoDVC[0]);
    spouseDVC[1] = new SpouseDVC("2", "Judith", "Connors", "McCall",
        "074-22-6431", infoDVC[1]);
    spouseDVC[2] = new SpouseDVC("3", "Linda", "Kelly", "Morrison",
        "501-22-5940", infoDVC[2]);
    spouseDVC[3] = new SpouseDVC("4", "Thomas", null, "Mullen", "210-23-3456",
        infoDVC[3]);
    spouseDVC[4] = new SpouseDVC("5", "Mitchell", null, "Jackson",
        "476-44-3349", infoDVC[4]);
    spouseDVC[5] = new SpouseDVC("6", "Cynthia", "White", "Allen",
        "508-908-7765", infoDVC[5]);
  }

  private static void createSchemaEJBs(Properties p) throws Exception {
    Vector v1 = null;
    double totalPrice;

    props = p;

    TestUtil.logTrace("Schema.createSchemaEJBs");

    TestUtil.logMsg("Create Customer EJB's [14 customers]");
    customerRef[0] = customerHome.create("1", "Alan E. Frechette",
        addressDVC[0], addressDVC[1], countryDVC[0]);
    customerRef[0].initLogging(props);

    customerRef[1] = customerHome.create("2", "Arthur D. Frechette",
        addressDVC[2], addressDVC[3], countryDVC[1]);
    customerRef[1].initLogging(props);

    customerRef[2] = customerHome.create("3", "Shelly D. Mcgowan",
        addressDVC[4], addressDVC[5], countryDVC[2]);
    customerRef[2].initLogging(props);

    customerRef[3] = customerHome.create("4", "Robert E. Bissett",
        addressDVC[6], addressDVC[7], countryDVC[3]);
    customerRef[3].initLogging(props);

    customerRef[4] = customerHome.create("5", "Stephen S. D'Milla",
        addressDVC[8], addressDVC[9], countryDVC[4]);
    customerRef[4].initLogging(props);

    customerRef[5] = customerHome.create("6", "Karen R. Tegan", addressDVC[10],
        addressDVC[11], countryDVC[5]);
    customerRef[5].initLogging(props);

    customerRef[6] = customerHome.create("7", "Stephen J. Caruso",
        addressDVC[12], addressDVC[13], countryDVC[6]);
    customerRef[6].initLogging(props);

    customerRef[7] = customerHome.create("8", "Irene M. Caruso", addressDVC[14],
        addressDVC[15], countryDVC[7]);
    customerRef[7].initLogging(props);

    customerRef[8] = customerHome.create("9", "William P. Keaton",
        addressDVC[16], addressDVC[17], countryDVC[8]);
    customerRef[8].initLogging(props);

    customerRef[9] = customerHome.create("10", "Kate P. Hudson", addressDVC[18],
        addressDVC[19], countryDVC[9]);
    customerRef[9].initLogging(props);

    customerRef[10] = customerHome.create("11", "Jonathan K. Smith",
        addressDVC[20], addressDVC[21], countryDVC[10]);
    customerRef[10].initLogging(props);

    customerRef[11] = customerHome.create("12", null, addressDVC[22],
        addressDVC[23], countryDVC[11]);
    customerRef[11].initLogging(props);

    customerRef[12] = customerHome.create("13", "Douglas A. Donahue",
        addressDVC[24], addressDVC[25], countryDVC[12]);
    customerRef[12].initLogging(props);

    customerRef[13] = customerHome.create("14", "Kellie A. Sanborn",
        addressDVC[26], addressDVC[27], countryDVC[13]);
    customerRef[13].initLogging(props);

    TestUtil.logMsg("Create Spouse EJB Only [1 spouse]");
    customerHome.addSpouseEntry(spouseDVC[5]);

    TestUtil.logMsg("Create Product EJB's [18 products]");
    productRef[0] = productHome.create("1", "Java 2 Unleashed Programming",
        (double) 54.95, 100, (long) 987654321);
    productRef[0].initLogging(props);

    productRef[1] = productHome.create("2", "Java 2 Network Programming",
        (double) 37.95, 100, (long) 876543219);
    productRef[1].initLogging(props);

    productRef[2] = productHome.create("3", "CORBA Programming", (double) 44.95,
        55, (long) 765432198);
    productRef[2].initLogging(props);

    productRef[3] = productHome.create("4",
        "WEB Programming with JSP's & Servlet's", (double) 33.95, 25,
        (long) 654321987);
    productRef[3].initLogging(props);

    productRef[4] = productHome.create("5", "Dell Laptop PC", (double) 1095.95,
        50, (long) 543219876);
    productRef[4].initLogging(props);

    productRef[5] = productHome.create("6", "Compaq Laptop PC", (double) 995.95,
        33, (long) 432198765);
    productRef[5].initLogging(props);

    productRef[6] = productHome.create("7", "Toshiba Laptop PC",
        (double) 1210.95, 22, (long) 321987654);
    productRef[6].initLogging(props);

    productRef[7] = productHome.create("8", "Gateway Laptop PC",
        (double) 1100.95, 11, (long) 219876543);
    productRef[7].initLogging(props);

    productRef[8] = productHome.create("9", "Free Samples", (double) 0.00, 10,
        (long) 000000000);
    productRef[8].initLogging(props);

    productRef[9] = productHome.create("10",
        "Designing Enterprise Applications", (double) 39.95, 500,
        (long) 123456789);
    productRef[9].initLogging(props);

    productRef[10] = productHome.create("11", "Complete Guide to XML",
        (double) 38.85, 300, (long) 234567891);
    productRef[10].initLogging(props);

    productRef[11] = productHome.create("12", "Programming for Dummies",
        (double) 24.95, 45, (long) 345678912);
    productRef[11].initLogging(props);

    productRef[12] = productHome.create("13", "Introduction to Java",
        (double) 60.95, 95, (long) 456789123);
    productRef[12].initLogging(props);

    productRef[13] = productHome.create("14", "Ultra System", (double) 5095.95,
        250, (long) 567891234);
    productRef[13].initLogging(props);

    productRef[14] = productHome.create("15", "Very Best Tutorial",
        (double) 25.99, 0, (long) 678912345);
    productRef[14].initLogging(props);

    productRef[15] = productHome.create("16", "Home Grown Programming Examples",
        (double) 10.95, 25, (long) 789123456);
    productRef[15].initLogging(props);

    productRef[16] = productHome.create("17", "Programming in ANSI C",
        (double) 23.95, 10, (long) 891234567);
    productRef[16].initLogging(props);

    productRef[17] = productHome.create("18", "Trial Software", (double) 10.00,
        75, (long) 912345678);
    productRef[17].initLogging(props);

    TestUtil.logMsg("Create Order EJB's [16 orders]");
    orderRef[0] = orderHome.create("1", customerRef[0]);
    orderRef[0].initLogging(props);

    orderRef[1] = orderHome.create("2", customerRef[1]);
    orderRef[1].initLogging(props);

    orderRef[2] = orderHome.create("3", customerRef[2]);
    orderRef[2].initLogging(props);

    orderRef[3] = orderHome.create("4", customerRef[3]);
    orderRef[3].initLogging(props);

    orderRef[4] = orderHome.create("5", customerRef[4]);
    orderRef[4].initLogging(props);

    orderRef[5] = orderHome.create("6", customerRef[5]);
    orderRef[5].initLogging(props);

    orderRef[6] = orderHome.create("7", customerRef[6]);
    orderRef[6].initLogging(props);

    orderRef[7] = orderHome.create("8", customerRef[7]);
    orderRef[7].initLogging(props);

    orderRef[8] = orderHome.create("9", customerRef[3]);
    orderRef[8].initLogging(props);

    orderRef[9] = orderHome.create("10", customerRef[8]);
    orderRef[9].initLogging(props);

    orderRef[10] = orderHome.create("11", customerRef[9]);
    orderRef[10].initLogging(props);

    orderRef[11] = orderHome.create("12", customerRef[10]);
    orderRef[11].initLogging(props);

    orderRef[12] = orderHome.create("13", customerRef[11]);
    orderRef[12].initLogging(props);

    orderRef[13] = orderHome.create("14", customerRef[12]);
    orderRef[13].initLogging(props);

    orderRef[14] = orderHome.create("15", customerRef[13]);
    orderRef[14].initLogging(props);

    orderRef[15] = orderHome.create("16", customerRef[13]);
    orderRef[15].initLogging(props);

    TestUtil.logMsg("Create Alias EJB's [30 aliases]");
    aliasRef[0] = aliasHome.create("1", "aef");
    aliasRef[0].initLogging(props);

    aliasRef[1] = aliasHome.create("2", "al");
    aliasRef[1].initLogging(props);

    aliasRef[2] = aliasHome.create("3", "fish");
    aliasRef[2].initLogging(props);

    aliasRef[3] = aliasHome.create("4", "twin");
    aliasRef[3].initLogging(props);

    aliasRef[4] = aliasHome.create("5", "adf");
    aliasRef[4].initLogging(props);

    aliasRef[5] = aliasHome.create("6", "art");
    aliasRef[5].initLogging(props);

    aliasRef[6] = aliasHome.create("7", "sdm");
    aliasRef[6].initLogging(props);

    aliasRef[7] = aliasHome.create("8", "sh_ll");
    aliasRef[7].initLogging(props);

    aliasRef[8] = aliasHome.create("9", "reb");
    aliasRef[8].initLogging(props);

    aliasRef[9] = aliasHome.create("10", "bobby");
    aliasRef[9].initLogging(props);

    aliasRef[10] = aliasHome.create("11", "bb");
    aliasRef[10].initLogging(props);

    aliasRef[11] = aliasHome.create("12", "ssd");
    aliasRef[11].initLogging(props);

    aliasRef[12] = aliasHome.create("13", "steved");
    aliasRef[12].initLogging(props);

    aliasRef[13] = aliasHome.create("14", "stevie");
    aliasRef[13].initLogging(props);

    aliasRef[14] = aliasHome.create("15", "");
    aliasRef[14].initLogging(props);

    aliasRef[15] = aliasHome.create("16", "");
    aliasRef[15].initLogging(props);

    aliasRef[16] = aliasHome.create("17", "sjc");
    aliasRef[16].initLogging(props);

    aliasRef[17] = aliasHome.create("18", "stevec");
    aliasRef[17].initLogging(props);

    aliasRef[18] = aliasHome.create("19", "imc");
    aliasRef[18].initLogging(props);

    aliasRef[19] = aliasHome.create("20", "iris");
    aliasRef[19].initLogging(props);

    aliasRef[20] = aliasHome.create("21", "bro");
    aliasRef[20].initLogging(props);

    aliasRef[21] = aliasHome.create("22", "sis");
    aliasRef[21].initLogging(props);

    aliasRef[22] = aliasHome.create("23", "kell");
    aliasRef[22].initLogging(props);

    aliasRef[23] = aliasHome.create("24", "bill");
    aliasRef[23].initLogging(props);

    aliasRef[24] = aliasHome.create("25", "suzy");
    aliasRef[24].initLogging(props);

    aliasRef[25] = aliasHome.create("26", "jon");
    aliasRef[25].initLogging(props);

    aliasRef[26] = aliasHome.create("27", "jk");
    aliasRef[26].initLogging(props);

    aliasRef[27] = aliasHome.create("28", "kellieann");
    aliasRef[27].initLogging(props);

    aliasRef[28] = aliasHome.create("29", "smitty");
    aliasRef[28].initLogging(props);

    aliasRef[29] = aliasHome.create("30", null);
    aliasRef[29].initLogging(props);

    TestUtil.logMsg("Setting additional relationships for orderRef[0]");
    lineItemDVC[0].setProduct(productRef[0]);
    lineItemDVC[1].setProduct(productRef[1]);
    lineItemDVC[2].setProduct(productRef[7]);
    lineItemDVC[28].setProduct(productRef[8]);
    lineItemDVC[0].setOrder(orderRef[0]);
    lineItemDVC[1].setOrder(orderRef[0]);
    lineItemDVC[2].setOrder(orderRef[0]);
    lineItemDVC[28].setOrder(orderRef[0]);
    orderRef[0].addLineItem(lineItemDVC[0]);
    orderRef[0].addLineItem(lineItemDVC[1]);
    orderRef[0].addLineItem(lineItemDVC[2]);
    orderRef[0].addSampleLineItem(lineItemDVC[28]);
    creditCardDVC[1].setOrder(orderRef[0]);
    creditCardDVC[1].setCustomer(customerRef[0]);
    totalPrice = productRef[0].getPrice() + productRef[1].getPrice()
        + productRef[7].getPrice() + productRef[8].getPrice();
    orderRef[0].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[1]");
    lineItemDVC[3].setProduct(productRef[0]);
    lineItemDVC[4].setProduct(productRef[1]);
    lineItemDVC[5].setProduct(productRef[2]);
    lineItemDVC[6].setProduct(productRef[3]);
    lineItemDVC[7].setProduct(productRef[4]);
    lineItemDVC[3].setOrder(orderRef[1]);
    lineItemDVC[4].setOrder(orderRef[1]);
    lineItemDVC[5].setOrder(orderRef[1]);
    lineItemDVC[6].setOrder(orderRef[1]);
    lineItemDVC[7].setOrder(orderRef[1]);
    orderRef[1].addLineItem(lineItemDVC[3]);
    orderRef[1].addLineItem(lineItemDVC[4]);
    orderRef[1].addLineItem(lineItemDVC[5]);
    orderRef[1].addLineItem(lineItemDVC[6]);
    orderRef[1].addLineItem(lineItemDVC[7]);
    creditCardDVC[3].setOrder(orderRef[1]);
    creditCardDVC[3].setCustomer(customerRef[1]);
    totalPrice = productRef[0].getPrice() + productRef[1].getPrice()
        + productRef[2].getPrice() + productRef[3].getPrice()
        + productRef[4].getPrice();
    orderRef[1].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[2]");
    lineItemDVC[8].setProduct(productRef[2]);
    lineItemDVC[9].setProduct(productRef[5]);
    lineItemDVC[8].setOrder(orderRef[2]);
    lineItemDVC[9].setOrder(orderRef[2]);
    orderRef[2].addLineItem(lineItemDVC[8]);
    orderRef[2].addLineItem(lineItemDVC[9]);
    creditCardDVC[4].setOrder(orderRef[2]);
    creditCardDVC[4].setCustomer(customerRef[2]);
    totalPrice = productRef[2].getPrice() + productRef[5].getPrice();
    orderRef[2].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[3]");
    lineItemDVC[10].setProduct(productRef[6]);
    lineItemDVC[10].setOrder(orderRef[3]);
    orderRef[3].addLineItem(lineItemDVC[10]);
    creditCardDVC[5].setOrder(orderRef[3]);
    creditCardDVC[5].setCustomer(customerRef[3]);
    totalPrice = productRef[6].getPrice();
    orderRef[3].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[4]");
    lineItemDVC[11].setProduct(productRef[0]);
    lineItemDVC[12].setProduct(productRef[1]);
    lineItemDVC[13].setProduct(productRef[2]);
    lineItemDVC[14].setProduct(productRef[3]);
    lineItemDVC[15].setProduct(productRef[4]);
    lineItemDVC[16].setProduct(productRef[5]);
    lineItemDVC[17].setProduct(productRef[6]);
    lineItemDVC[18].setProduct(productRef[7]);
    lineItemDVC[11].setOrder(orderRef[4]);
    lineItemDVC[12].setOrder(orderRef[4]);
    lineItemDVC[13].setOrder(orderRef[4]);
    lineItemDVC[14].setOrder(orderRef[4]);
    lineItemDVC[15].setOrder(orderRef[4]);
    lineItemDVC[16].setOrder(orderRef[4]);
    lineItemDVC[17].setOrder(orderRef[4]);
    lineItemDVC[18].setOrder(orderRef[4]);
    orderRef[4].addLineItem(lineItemDVC[11]);
    orderRef[4].addLineItem(lineItemDVC[12]);
    orderRef[4].addLineItem(lineItemDVC[13]);
    orderRef[4].addLineItem(lineItemDVC[14]);
    orderRef[4].addLineItem(lineItemDVC[15]);
    orderRef[4].addLineItem(lineItemDVC[16]);
    orderRef[4].addLineItem(lineItemDVC[17]);
    orderRef[4].addLineItem(lineItemDVC[18]);
    creditCardDVC[7].setOrder(orderRef[4]);
    creditCardDVC[7].setCustomer(customerRef[4]);
    totalPrice = productRef[0].getPrice() + productRef[1].getPrice()
        + productRef[2].getPrice() + productRef[3].getPrice()
        + productRef[4].getPrice() + productRef[5].getPrice()
        + productRef[6].getPrice() + productRef[7].getPrice();
    orderRef[4].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[5]");
    lineItemDVC[19].setProduct(productRef[3]);
    lineItemDVC[20].setProduct(productRef[6]);
    lineItemDVC[29].setProduct(productRef[8]);
    lineItemDVC[19].setOrder(orderRef[5]);
    lineItemDVC[20].setOrder(orderRef[5]);
    lineItemDVC[29].setOrder(orderRef[5]);
    orderRef[5].addLineItem(lineItemDVC[19]);
    orderRef[5].addLineItem(lineItemDVC[20]);
    orderRef[5].addSampleLineItem(lineItemDVC[29]);
    creditCardDVC[10].setOrder(orderRef[5]);
    creditCardDVC[10].setCustomer(customerRef[5]);
    totalPrice = productRef[3].getPrice() + productRef[6].getPrice()
        + productRef[8].getPrice();
    orderRef[5].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[6]");
    lineItemDVC[21].setProduct(productRef[2]);
    lineItemDVC[22].setProduct(productRef[3]);
    lineItemDVC[23].setProduct(productRef[7]);
    lineItemDVC[21].setOrder(orderRef[6]);
    lineItemDVC[22].setOrder(orderRef[6]);
    lineItemDVC[23].setOrder(orderRef[6]);
    orderRef[6].addLineItem(lineItemDVC[21]);
    orderRef[6].addLineItem(lineItemDVC[22]);
    orderRef[6].addLineItem(lineItemDVC[23]);
    creditCardDVC[11].setOrder(orderRef[6]);
    creditCardDVC[11].setCustomer(customerRef[6]);
    totalPrice = productRef[2].getPrice() + productRef[3].getPrice()
        + productRef[7].getPrice();
    orderRef[6].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[7]");
    lineItemDVC[24].setProduct(productRef[0]);
    lineItemDVC[25].setProduct(productRef[4]);
    lineItemDVC[24].setOrder(orderRef[7]);
    lineItemDVC[25].setOrder(orderRef[7]);
    orderRef[7].addLineItem(lineItemDVC[24]);
    orderRef[7].addLineItem(lineItemDVC[25]);
    creditCardDVC[13].setOrder(orderRef[7]);
    creditCardDVC[13].setCustomer(customerRef[7]);
    totalPrice = productRef[0].getPrice() + productRef[4].getPrice();
    orderRef[7].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[8]");
    lineItemDVC[26].setProduct(productRef[0]);
    lineItemDVC[27].setProduct(productRef[1]);
    lineItemDVC[26].setOrder(orderRef[8]);
    lineItemDVC[27].setOrder(orderRef[8]);
    orderRef[8].addLineItem(lineItemDVC[26]);
    orderRef[8].addLineItem(lineItemDVC[27]);
    creditCardDVC[6].setOrder(orderRef[8]);
    creditCardDVC[6].setCustomer(customerRef[3]);
    totalPrice = productRef[0].getPrice() + productRef[1].getPrice();
    orderRef[8].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[9]");
    lineItemDVC[30].setProduct(productRef[9]);
    lineItemDVC[31].setProduct(productRef[16]);
    lineItemDVC[30].setOrder(orderRef[9]);
    lineItemDVC[31].setOrder(orderRef[9]);
    orderRef[9].addLineItem(lineItemDVC[30]);
    orderRef[9].addLineItem(lineItemDVC[31]);
    creditCardDVC[14].setOrder(orderRef[9]);
    creditCardDVC[14].setCustomer(customerRef[8]);
    totalPrice = productRef[9].getPrice() + productRef[16].getPrice();
    orderRef[9].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[10]");
    lineItemDVC[32].setProduct(productRef[13]);
    lineItemDVC[32].setOrder(orderRef[10]);
    orderRef[10].addLineItem(lineItemDVC[32]);
    creditCardDVC[15].setOrder(orderRef[10]);
    creditCardDVC[15].setCustomer(customerRef[9]);
    totalPrice = productRef[13].getPrice();
    orderRef[10].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[11]");
    lineItemDVC[33].setProduct(productRef[10]);
    lineItemDVC[34].setProduct(productRef[12]);
    lineItemDVC[33].setOrder(orderRef[11]);
    lineItemDVC[34].setOrder(orderRef[11]);
    orderRef[11].addLineItem(lineItemDVC[33]);
    orderRef[11].addLineItem(lineItemDVC[34]);
    creditCardDVC[16].setOrder(orderRef[11]);
    creditCardDVC[16].setCustomer(customerRef[10]);
    totalPrice = productRef[10].getPrice() + productRef[12].getPrice();
    orderRef[11].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[12]");
    lineItemDVC[35].setProduct(productRef[17]);
    lineItemDVC[35].setOrder(orderRef[12]);
    orderRef[12].addLineItem(lineItemDVC[35]);
    creditCardDVC[17].setOrder(orderRef[12]);
    creditCardDVC[17].setCustomer(customerRef[11]);
    totalPrice = productRef[17].getPrice();
    orderRef[12].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[13]");
    lineItemDVC[36].setProduct(productRef[7]);
    lineItemDVC[37].setProduct(productRef[14]);
    lineItemDVC[38].setProduct(productRef[15]);
    lineItemDVC[36].setOrder(orderRef[13]);
    lineItemDVC[37].setOrder(orderRef[13]);
    lineItemDVC[38].setOrder(orderRef[13]);
    orderRef[13].addLineItem(lineItemDVC[36]);
    orderRef[13].addLineItem(lineItemDVC[37]);
    orderRef[13].addLineItem(lineItemDVC[38]);
    creditCardDVC[18].setOrder(orderRef[13]);
    creditCardDVC[18].setCustomer(customerRef[12]);
    totalPrice = productRef[7].getPrice() + productRef[14].getPrice()
        + productRef[15].getPrice();
    orderRef[13].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[14]");
    lineItemDVC[39].setProduct(productRef[1]);
    lineItemDVC[40].setProduct(productRef[2]);
    lineItemDVC[41].setProduct(productRef[12]);
    lineItemDVC[42].setProduct(productRef[15]);
    lineItemDVC[39].setOrder(orderRef[14]);
    lineItemDVC[40].setOrder(orderRef[14]);
    lineItemDVC[41].setOrder(orderRef[14]);
    lineItemDVC[42].setOrder(orderRef[14]);
    orderRef[14].addLineItem(lineItemDVC[39]);
    orderRef[14].addLineItem(lineItemDVC[40]);
    orderRef[14].addLineItem(lineItemDVC[41]);
    orderRef[14].addLineItem(lineItemDVC[42]);
    creditCardDVC[19].setOrder(orderRef[14]);
    creditCardDVC[19].setCustomer(customerRef[13]);
    totalPrice = productRef[1].getPrice() + productRef[2].getPrice()
        + productRef[12].getPrice() + productRef[15].getPrice();
    orderRef[14].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for orderRef[15]");
    lineItemDVC[43].setProduct(productRef[13]);
    lineItemDVC[43].setOrder(orderRef[15]);
    orderRef[15].addLineItem(lineItemDVC[43]);
    creditCardDVC[19].setOrder(orderRef[15]);
    creditCardDVC[19].setCustomer(customerRef[13]);
    totalPrice = productRef[13].getPrice();
    orderRef[15].setTotalPrice((double) totalPrice);

    TestUtil.logMsg("Setting additional relationships for customerRef[0]");
    customerRef[0].addAlias(aliasRef[0]);
    customerRef[0].addAlias(aliasRef[1]);
    customerRef[0].addAlias(aliasRef[2]);
    customerRef[0].addAlias(aliasRef[3]);
    customerRef[0].addOrder(orderRef[0]);
    customerRef[0].addCreditCard(creditCardDVC[0]);
    customerRef[0].addCreditCard(creditCardDVC[1]);
    customerRef[0].addCreditCard(creditCardDVC[2]);

    TestUtil.logMsg("Setting additional relationships for customerRef[1]");
    customerRef[1].addAlias(aliasRef[2]);
    customerRef[1].addAlias(aliasRef[3]);
    customerRef[1].addAlias(aliasRef[4]);
    customerRef[1].addAlias(aliasRef[5]);
    customerRef[1].addOrder(orderRef[1]);
    customerRef[1].addCreditCard(creditCardDVC[3]);

    TestUtil.logMsg("Setting additional relationships for customerRef[2]");
    customerRef[2].addAlias(aliasRef[6]);
    customerRef[2].addAlias(aliasRef[7]);
    customerRef[2].addOrder(orderRef[2]);
    customerRef[2].addCreditCard(creditCardDVC[4]);

    TestUtil.logMsg("Setting additional relationships for customerRef[3]");
    customerRef[3].addAlias(aliasRef[8]);
    customerRef[3].addAlias(aliasRef[9]);
    customerRef[3].addAlias(aliasRef[10]);
    customerRef[3].addOrder(orderRef[3]);
    customerRef[3].addCreditCard(creditCardDVC[5]);
    customerRef[3].addCreditCard(creditCardDVC[6]);

    TestUtil.logMsg("Setting additional relationships for customerRef[4]");
    customerRef[4].addAlias(aliasRef[11]);
    customerRef[4].addAlias(aliasRef[12]);
    customerRef[4].addAlias(aliasRef[13]);
    customerRef[4].addOrder(orderRef[4]);
    customerRef[4].addCreditCard(creditCardDVC[7]);
    customerRef[4].addCreditCard(creditCardDVC[8]);

    TestUtil.logMsg("Setting additional relationships for customerRef[5]");
    // Removed for IS EMPTY tests per ks
    // leaving commented out as too many tests depend on this logic
    // customerRef[5].addAlias(aliasRef[14]);
    // customerRef[5].addAlias(aliasRef[15]);
    customerRef[5].addOrder(orderRef[5]);
    customerRef[5].addCreditCard(creditCardDVC[9]);
    customerRef[5].addCreditCard(creditCardDVC[10]);

    TestUtil.logMsg("Setting additional relationships for customerRef[6]");
    customerRef[6].addAlias(aliasRef[13]);
    customerRef[6].addAlias(aliasRef[16]);
    customerRef[6].addAlias(aliasRef[17]);
    customerRef[6].addOrder(orderRef[6]);
    customerRef[6].addCreditCard(creditCardDVC[11]);
    customerRef[6].addSpouse(spouseDVC[0]);

    TestUtil.logMsg("Setting additional relationships for customerRef[7]");
    customerRef[7].addAlias(aliasRef[18]);
    customerRef[7].addAlias(aliasRef[19]);
    customerRef[7].addOrder(orderRef[7]);
    customerRef[7].addCreditCard(creditCardDVC[12]);
    customerRef[7].addCreditCard(creditCardDVC[13]);

    TestUtil.logMsg("Setting additional relationships for customerRef[8]");
    customerRef[8].addAlias(aliasRef[23]);
    customerRef[8].addOrder(orderRef[9]);
    customerRef[8].addCreditCard(creditCardDVC[14]);

    TestUtil.logMsg("Setting additional relationships for customerRef[9]");
    customerRef[9].addAlias(aliasRef[21]);
    customerRef[9].addAlias(aliasRef[29]);
    customerRef[9].addOrder(orderRef[10]);
    customerRef[9].addCreditCard(creditCardDVC[15]);
    customerRef[9].addSpouse(spouseDVC[3]);

    TestUtil.logMsg("Setting additional relationships for customerRef[10]");
    customerRef[10].addAlias(aliasRef[25]);
    customerRef[10].addAlias(aliasRef[26]);
    customerRef[10].addAlias(aliasRef[28]);
    customerRef[10].addOrder(orderRef[11]);
    customerRef[10].addCreditCard(creditCardDVC[16]);
    customerRef[10].addSpouse(spouseDVC[1]);

    TestUtil.logMsg("Setting additional relationships for customerRef[11]");
    customerRef[11].addAlias(aliasRef[24]);
    customerRef[11].addOrder(orderRef[12]);
    customerRef[11].addCreditCard(creditCardDVC[17]);
    customerRef[11].addSpouse(spouseDVC[4]);

    TestUtil.logMsg("Setting additional relationships for customerRef[12]");
    customerRef[12].addAlias(aliasRef[20]);
    customerRef[12].addOrder(orderRef[13]);
    customerRef[12].addCreditCard(creditCardDVC[18]);
    customerRef[12].addSpouse(spouseDVC[2]);

    TestUtil.logMsg("Setting additional relationships for customerRef[13]");
    customerRef[13].addAlias(aliasRef[22]);
    customerRef[13].addAlias(aliasRef[27]);
    customerRef[13].addOrder(orderRef[14]);
    customerRef[13].addOrder(orderRef[15]);
    customerRef[13].addCreditCard(creditCardDVC[19]);

  }
}
