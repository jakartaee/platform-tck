/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.util;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.Endpoint;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class PublishEndpoint {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final String BINDINGID_STRING = "http://schemas.xmlsoap.org/wsdl/soap/http";

  private static TSURL ctsurl = new TSURL();

  private static String url = null;

  private static String hostname = HOSTNAME;

  private static int portnum = 0;

  private static Properties properties = null;

  private static String contextroot = null;

  private static String datfile = null;

  private static String tshome = null;

  private static File tsHome = null;

  private static String tsjte = null;

  private static String webappdir = null;

  private static File webAppDir = null;

  private static Vector<Endpoint> le = new Vector<Endpoint>();

  private static ClassLoader cl = Thread.currentThread()
      .getContextClassLoader();

  public static void main(String[] args) {
    int i = 0;
    while (i < args.length) {
      String arg = args[i++];
      if (i == args.length - 1 && !arg.startsWith("-")) {
        System.out.println(
            "ERROR: argument found that does not have a option '" + arg + "'");
        getUsage();
        System.exit(1);
      } else if (arg.equals("-h")) {
        getUsage();
        System.exit(0);
      } else if (arg.equals("-tshome")) {
        tshome = args[i++];
        tsHome = new File(tshome);
        if (tsHome.isDirectory()) {
          Properties props = new Properties();
          tsjte = tshome + File.separator + "bin" + File.separator + "ts.jte";
          try {
            FileInputStream fis = new FileInputStream(new File(tsjte));
            props.load(fis);
            hostname = props.getProperty("webServerHost");
            portnum = Integer.parseInt(props.getProperty("webServerPort"));
            try {
              TestUtil.init(props);
            } catch (Exception e) {
              ;
            }
          } catch (Exception e) {
            e.printStackTrace();
          }

          datfile = tshome + File.separator + "bin" + File.separator
              + "jaxws-url-props.dat";
          try {
            FileInputStream fis = new FileInputStream(new File(datfile));
            properties = new Properties();
            properties.load(fis);
          } catch (Exception e) {
            e.printStackTrace();
          }

        } else {
          System.out.println("ERROR: -tshome did not specify a directory");
          System.exit(1);
        }
        continue;
      } else if (arg.equals("-host")) {
        hostname = args[i++];
        continue;
      } else if (arg.equals("-port")) {
        portnum = Integer.parseInt(args[i++]);
        continue;
      } else if (arg.equals("-datfile")) {
        datfile = args[i++];
        try {
          FileInputStream fis = new FileInputStream(new File(datfile));
          properties = new Properties();
          properties.load(fis);
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
        continue;
      } else if (arg.equals("-webappdir")) {
        webappdir = args[i++];
        try {
          webAppDir = new File(webappdir);
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
        if (!webAppDir.isDirectory()) {
          System.out.println("ERROR: -webappdir dir not specify a directory");
          System.exit(1);
        }
        continue;
      } else if (arg.equals("-tsjte")) {
        tsjte = args[i++];
        try {
          FileInputStream fis = new FileInputStream(new File(tsjte));
          Properties props = new Properties();
          props.load(fis);
          TestUtil.init(props);
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
        continue;
      } else {
        System.out.println("ERROR: illegal argument '" + args[i++] + "'");
        getUsage();
        System.exit(1);
      }
    }

    if (datfile == null) {
      System.out.println("ERROR: -datfile was not specified");
      getUsage();
      System.exit(1);
    }
    if (webappdir == null) {
      System.out.println("ERROR: -webappdir was not specified");
      getUsage();
      System.exit(1);
    }
    if (tsjte == null) {
      System.out.println("ERROR: -tsjte was not specified");
      getUsage();
      System.exit(1);
    }

    if (portnum == 0) {
      int javaseServerPort = JAXWS_Util.getFreePort();
      if (javaseServerPort <= 0) {
        TestUtil.logErr("Free port not found.");
      } else {
        portnum = javaseServerPort;
      }
    }

    String[] dirListing = webAppDir.list(JarFilter.getInstance());
    for (int k = 0; k < dirListing.length; k++) {
      // System.out.println("Working on archive -> "+dirListing[k]);

      contextroot = dirListing[k].substring(0, dirListing[k].indexOf("."));

      String datfileKey = properties.getProperty(contextroot);
      if ((datfileKey == null) || (datfileKey.equals(""))) {
        System.out.println("ERROR: Could not find the key for the '"
            + contextroot + "' in the dat file");
        System.out.println("so this webapp will not be published");
        continue;
      }
      boolean found = true;
      int j = 1;
      while (found) {
        String implProp = datfileKey + ".impl." + j;
        String impl = properties.getProperty(implProp);
        if (((impl == null) || (impl.equals(""))) && (j == 1)) {
          System.out.println(
              "ERROR: Could not find the following implementation prop '"
                  + implProp + "'");
          System.out
              .println("in the dat file, so this webapp will not be published");
          break;
        } else if (impl == null) {
          found = false;
        } else {
          String endpointurl = properties
              .getProperty(datfileKey + ".endpoint." + j);
          // System.out.println("impl="+impl);
          // System.out.println("PROTOCOL="+PROTOCOL);
          // System.out.println("hostname="+hostname);
          // System.out.println("portnum="+portnum);
          // System.out.println("endpointurl="+endpointurl);

          url = ctsurl.getURLString(PROTOCOL, hostname, portnum, endpointurl);
          Endpoint endpoint = null;
          try {
            Class c = Class.forName(impl);
            Object o = c.newInstance();
            endpoint = Endpoint.create(o);

            List<Source> listMetaData = new ArrayList<Source>();
            boolean foundMetaData = true;
            int jj = 1;
            while (foundMetaData) {
              String metadataProp = datfileKey + ".metadata." + jj;
              String metadata = properties.getProperty(metadataProp);
              if (metadata == null) {
                foundMetaData = false;
              } else {
                URL url = cl.getResource(metadata);
                if (url != null) {
                  listMetaData.add(
                      new StreamSource(url.openStream(), url.toExternalForm()));
                }
              }
              jj++;
            }
            if (listMetaData != null) {
              if (listMetaData.size() > 0) {
                endpoint.setMetadata(listMetaData);
              }
            }
            endpoint.publish(url);
            System.out.println("Published endpoint to url " + url);

            le.add(endpoint);
          } catch (Exception e) {
            System.out
                .println("Exception occurred trying to publish url:" + url);
            e.printStackTrace();
          }
        }
        j++;

      }
    }

    int len = le.size();
    if (len > 0) {

      try {
        File terminate = new File(webappdir + File.separator + "terminate");
        boolean bFound = true;
        while (bFound) {
          Thread.sleep(10000);
          if (terminate.exists()) {
            bFound = false;
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("Unpublishing:" + len + " endpoints");
      for (int j = 0; j < len; j++) {
        Endpoint e = le.get(j);
        e.stop();
      }
    } else {
      System.out.println("No endpoints were published, so terminating...");
    }
    System.out.println("exiting PublishEndpoint");
    System.exit(0);
  }

  private static void getUsage() {
    System.out.println("PublishEndpoint [-tshome fullpathname] ||");
    System.out.println(
        "                [[-host hostname] | [-port portnum] | [-datfile fullpathtofilename] | [-tsjte fullpathtofilename]]");
    System.out.println("                -webappdir fullpathtoarchives ");

  }

  private static class JarFilter implements FilenameFilter {
    private static JarFilter instance = new JarFilter();

    private JarFilter() {
    }

    public static JarFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      return name.endsWith(".jar");
    }
  }

}
