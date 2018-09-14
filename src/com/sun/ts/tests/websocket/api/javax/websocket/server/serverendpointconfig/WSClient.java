/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.javax.websocket.server.serverendpointconfig;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.TCKExtension;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

public class WSClient extends ServiceEETest {

  public static void main(String[] args) {
    WSClient theTests = new WSClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
   */
  public void setup(String[] args, Properties p) throws Fault {
  }

  /* Run test */
  /*
   * @testName: constructortest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:70; WebSocket:JAVADOC:71; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:197;
   *
   *
   * @test_Strategy: Test constructor
   */
  public void constructortest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer").build();

      List tmp = sec.getDecoders();
      if (tmp != null) {
        TestUtil.logTrace("getDecoders() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getDecoders() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getEncoders();
      if (tmp != null) {
        TestUtil.logTrace("getEncoders() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getEncoders() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getExtensions();
      if (tmp != null) {
        TestUtil.logTrace("getExtensions() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getSubprotocols();
      if (tmp != null) {
        TestUtil.logTrace("getSubprotocols() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed");
    }
  }

  /*
   * @testName: subprotocolsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:197; WebSocket:JAVADOC:204;
   *
   * @test_Strategy:
   */
  public void subprotocolsTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");
    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .subprotocols(expected_subprotocols).build();

      List<String> tmp = sec.getSubprotocols();
      if (tmp != null) {
        log.append("getSubprotocols() return non-null List|");
        if (!tmp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmp.size() + "|");
          int size_actual = tmp.size();
          if (size_actual == expected_subprotocols.size()) {
            for (String subpro : tmp) {
              if (expected_subprotocols.contains(subpro)) {
                log.append("sub protocol " + subpro + " found is expected|");
              } else {
                passed = false;
                log.append(
                    "sub protocol " + subpro + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append("getSubprotocols() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size_actual);
          }
        } else {
          log.append("getSubprotocols() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getSubprotocols() return null list");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());
    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: extensionsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:195; WebSocket:JAVADOC:203;
   *
   * @test_Strategy:
   */
  public void extensionsTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer").extensions(extensions)
          .build();
      List<Extension> tmp = sec.getExtensions();
      if (tmp != null) {
        log.append("getExtensions() return non-null List|");
        if (!tmp.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmp.size() + "|");
          int size_actual = tmp.size();
          if (size_actual == extensions.size()) {
            for (Extension ext : tmp) {
              if (extensions.contains(ext)) {
                log.append("extension " + ext + " found is expected|");
              } else {
                passed = false;
                log.append("extension " + ext + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append(
                "getExtensions() returned not exactly the same amount of extensions.");
            log.append("Expecting two, returned " + size_actual);
          }
        } else {
          log.append("getExtensions() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getExtensions() return null list|");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());
    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:195; WebSocket:JAVADOC:197; WebSocket:JAVADOC:203;
   * WebSocket:JAVADOC:204;
   *
   *
   * @test_Strategy:
   */
  public void constructorTest1() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .subprotocols(expected_subprotocols).extensions(extensions).build();

      List<Extension> tmpe = sec.getExtensions();
      List<String> tmpp = sec.getSubprotocols();

      if (tmpe != null) {
        log.append("getExtensions() return non-null List|");
        if (!tmpe.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmpe.size() + "|");
          int size_actual = tmpe.size();
          if (size_actual == extensions.size()) {
            for (Extension ext : tmpe) {
              if (extensions.contains(ext)) {
                log.append("extension " + ext + " found is expected|");
              } else {
                passed = false;
                log.append("extension " + ext + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append(
                "getExtensions() returned not exactly the same amount of extensions.");
            log.append("Expecting two, returned " + size_actual);
          }
        } else {
          log.append("getExtensions() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getExtensions() return null list|");
      }

      if (tmpp != null) {
        log.append("getSubprotocols() return non-null List|");
        if (!tmpp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmpp.size() + "|");
          int size_actual = tmpp.size();
          if (size_actual == expected_subprotocols.size()) {
            for (String subpro : tmpp) {
              if (expected_subprotocols.contains(subpro)) {
                log.append("sub protocol " + subpro + " found is expected|");
              } else {
                passed = false;
                log.append(
                    "sub protocol " + subpro + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append("getSubprotocols() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size_actual);
          }
        } else {
          log.append("getSubprotocols() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getSubprotocols() return null list");
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: encodersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:71; WebSocket:JAVADOC:202;
   *
   * @test_Strategy:
   */
  public void encodersTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<Class<? extends Encoder>> expected_encoders = new ArrayList<Class<? extends Encoder>>();
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.ErrorEncoder.class);
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanEncoder.class);

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .encoders(expected_encoders).build();

      List<Class<? extends Encoder>> tmp = sec.getEncoders();
      if (tmp != null) {
        log.append("getEncoders() return non-null List");
        if (!tmp.isEmpty()) {
          int size = tmp.size();
          log.append(
              "getEncoders() return non-empty List  with size " + size + "|");
          if (size == 2) {
            for (Class<? extends Encoder> encoder : tmp) {
              log.append("Encoder " + encoder + " found ");
              if (expected_encoders.contains(encoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getEncoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getEncoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getEncoders() return null list");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: decodersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:70; WebSocket:JAVADOC:201;
   *
   * @test_Strategy:
   */
  public void decodersTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .decoders(expected_decoders).build();

      List<Class<? extends Decoder>> tmp = sec.getDecoders();
      if (tmp != null) {
        log.append("getDecoders() return non-null List|");
        if (!tmp.isEmpty()) {
          int size = tmp.size();
          log.append("getDecoders() return non-empty List  with size "
              + tmp.size() + "|");
          if (size == 2) {
            for (Class<? extends Decoder> decoder : tmp) {
              log.append("Decoder " + decoder + " found ");
              if (expected_decoders.contains(decoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getDecoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getDecoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getDecoders() return null list|");
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: constructorTest2
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:195; WebSocket:JAVADOC:197; WebSocket:JAVADOC:203;
   * WebSocket:JAVADOC:204; WebSocket:JAVADOC:70; WebSocket:JAVADOC:201;
   *
   * @test_Strategy:
   */
  public void constructorTest2() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .subprotocols(expected_subprotocols).extensions(extensions)
          .decoders(expected_decoders).build();

      List<Extension> tmpe = sec.getExtensions();
      List<String> tmpp = sec.getSubprotocols();
      List<Class<? extends Decoder>> tmpd = sec.getDecoders();

      if (tmpe != null) {
        log.append("getExtensions() return non-null List|");
        if (!tmpe.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmpe.size() + "|");
          int size_actual = tmpe.size();
          if (size_actual == extensions.size()) {
            for (Extension ext : tmpe) {
              if (extensions.contains(ext)) {
                log.append("extension " + ext + " found is expected|");
              } else {
                passed = false;
                log.append("extension " + ext + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append(
                "getExtensions() returned not exactly the same amount of extensions.");
            log.append("Expecting two, returned " + size_actual);
          }
        } else {
          log.append("getExtensions() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getExtensions() return null list|");
      }

      if (tmpp != null) {
        log.append("getSubprotocols() return non-null List|");
        if (!tmpp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmpp.size() + "|");
          int size_actual = tmpp.size();
          if (size_actual == expected_subprotocols.size()) {
            for (String subpro : tmpp) {
              if (expected_subprotocols.contains(subpro)) {
                log.append("sub protocol " + subpro + " found is expected|");
              } else {
                passed = false;
                log.append(
                    "sub protocol " + subpro + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append("getSubprotocols() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size_actual);
          }
        } else {
          log.append("getSubprotocols() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getSubprotocols() return null list");
      }

      if (tmpd != null) {
        log.append("getDecoders() return non-null List|");
        if (!tmpd.isEmpty()) {
          int size = tmpd.size();
          log.append("getDecoders() return non-empty List  with size "
              + tmpd.size() + "|");
          if (size == 2) {
            for (Class<? extends Decoder> decoder : tmpd) {
              log.append("Decoder " + decoder + " found ");
              if (expected_decoders.contains(decoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getDecoders() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size);
          }
        } else {
          log.append("getDecoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getDecoders() return null list|");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: constructorTest3
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:195; WebSocket:JAVADOC:197; WebSocket:JAVADOC:203;
   * WebSocket:JAVADOC:204; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:201; WebSocket:JAVADOC:202;
   *
   * @test_Strategy:
   */
  public void constructorTest3() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    List<Class<? extends Encoder>> expected_encoders = new ArrayList<Class<? extends Encoder>>();
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.ErrorEncoder.class);
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanEncoder.class);

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .subprotocols(expected_subprotocols).extensions(extensions)
          .decoders(expected_decoders).encoders(expected_encoders).build();

      List<Extension> tmpe = sec.getExtensions();
      List<String> tmpp = sec.getSubprotocols();
      List<Class<? extends Decoder>> tmpd = sec.getDecoders();

      if (tmpe != null) {
        log.append("getExtensions() return non-null List|");
        if (!tmpe.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmpe.size() + "|");
          int size_actual = tmpe.size();
          if (size_actual == extensions.size()) {
            for (Extension ext : tmpe) {
              if (extensions.contains(ext)) {
                log.append("extension " + ext + " found is expected|");
              } else {
                passed = false;
                log.append("extension " + ext + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append(
                "getExtensions() returned not exactly the same amount of extensions.");
            log.append("Expecting two, returned " + size_actual);
          }
        } else {
          log.append("getExtensions() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getExtensions() return null list|");
      }

      if (tmpp != null) {
        log.append("getSubprotocols() return non-null List|");
        if (!tmpp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmpp.size() + "|");
          int size_actual = tmpp.size();
          if (size_actual == expected_subprotocols.size()) {
            for (String subpro : tmpp) {
              if (expected_subprotocols.contains(subpro)) {
                log.append("sub protocol " + subpro + " found is expected|");
              } else {
                passed = false;
                log.append(
                    "sub protocol " + subpro + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append("getSubprotocols() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size_actual);
          }
        } else {
          log.append("getSubprotocols() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getSubprotocols() return null list");
      }

      if (tmpd != null) {
        log.append("getDecoders() return non-null List|");
        if (!tmpd.isEmpty()) {
          int size = tmpd.size();
          log.append("getDecoders() return non-empty List  with size "
              + tmpd.size() + "|");
          if (size == 2) {
            for (Class<? extends Decoder> decoder : tmpd) {
              log.append("Decoder " + decoder + " found ");
              if (expected_decoders.contains(decoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getDecoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getDecoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getDecoders() return null list|");
      }

      List<Class<? extends Encoder>> tmpen = sec.getEncoders();
      if (tmpen != null) {
        log.append("getEncoders() return non-null List");
        if (!tmpen.isEmpty()) {
          int size = tmpen.size();
          log.append(
              "getEncoders() return non-empty List  with size " + size + "|");
          if (size == 2) {
            for (Class<? extends Encoder> encoder : tmpen) {
              log.append("Encoder " + encoder + " found ");
              if (expected_encoders.contains(encoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getEncoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getEncoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getEncoders() return null list");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: configuratorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:199; WebSocket:JAVADOC:193; WebSocket:JAVADOC:206;
   * WebSocket:JAVADOC:207; WebSocket:JAVADOC:69;
   *
   * @test_Strategy:
   */
  public void configuratorTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    ServerEndpointConfig.Configurator config = new TCKConfigurator();

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer").configurator(config)
          .build();

      ServerEndpointConfig.Configurator tmpc = sec.getConfigurator();

      if (!tmpc.getClass().equals(
          com.sun.ts.tests.websocket.api.javax.websocket.server.serverendpointconfig.TCKConfigurator.class)) {
        log.append("getConfigurator() returned a different Configurator: "
            + tmpc.getClass().getName());
        passed = false;
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: constructorTest4
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:199; WebSocket:JAVADOC:193; WebSocket:JAVADOC:206;
   * WebSocket:JAVADOC:207; WebSocket:JAVADOC:69; WebSocket:JAVADOC:70;
   * WebSocket:JAVADOC:71; WebSocket:JAVADOC:195; WebSocket:JAVADOC:197;
   * 
   * @test_Strategy:
   */
  public void constructorTest4() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    ServerEndpointConfig.Configurator config = new TCKConfigurator();

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer").configurator(config)
          .build();
      List tmp = sec.getDecoders();
      if (tmp != null) {
        TestUtil.logTrace("getDecoders() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getDecoders() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getEncoders();
      if (tmp != null) {
        TestUtil.logTrace("getEncoders() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getEncoders() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getExtensions();
      if (tmp != null) {
        TestUtil.logTrace("getExtensions() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

      tmp = sec.getSubprotocols();
      if (tmp != null) {
        TestUtil.logTrace("getSubprotocols() return non-null List");
        if (!tmp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmp.size() + "|");
          passed = false;
        }
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: getEndpointClassTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:194;
   *
   * @test_Strategy:
   */
  public void getEndpointClassTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer").build();

      Class tmp = sec.getEndpointClass();

      if (!tmp.equals(
          com.sun.ts.tests.websocket.api.javax.websocket.server.serverendpointconfig.TCKEndpoint.class)) {
        log.append(
            "getEndpointClass() returned a different Class: " + tmp.getName());
        passed = false;
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: getPathTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:196;
   *
   * @test_Strategy:
   */
  public void getPathTest() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();
    String path = "/TCKTestServer";

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, path).build();

      String tmp = sec.getPath();

      if (!path.equals(tmp)) {
        log.append("getPath() returned a different path: " + tmp);
        passed = false;
      }

    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  /*
   * @testName: constructorTest5
   * 
   * @assertion_ids: WebSocket:JAVADOC:200; WebSocket:JAVADOC:198;
   * WebSocket:JAVADOC:199; WebSocket:JAVADOC:193; WebSocket:JAVADOC:206;
   * WebSocket:JAVADOC:207; WebSocket:JAVADOC:69; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:197; WebSocket:JAVADOC:203; WebSocket:JAVADOC:204;
   * WebSocket:JAVADOC:70; WebSocket:JAVADOC:71; WebSocket:JAVADOC:201;
   * WebSocket:JAVADOC:202;
   *
   * @test_Strategy:
   */
  public void constructorTest5() throws Fault {
    boolean passed = true;
    StringBuffer log = new StringBuffer();

    List<String> expected_subprotocols = Arrays.asList("JSON", "XML", "XMPP",
        "Hessian", "Quake", "PUB/SUB", "Query");

    List<Class<? extends Encoder>> expected_encoders = new ArrayList<Class<? extends Encoder>>();
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.ErrorEncoder.class);
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanEncoder.class);

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    ServerEndpointConfig.Configurator config = new TCKConfigurator();

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      ServerEndpointConfig sec = ServerEndpointConfig.Builder
          .create(TCKEndpoint.class, "/TCKTestServer")
          .subprotocols(expected_subprotocols).extensions(extensions)
          .decoders(expected_decoders).encoders(expected_encoders)
          .configurator(config).build();

      List<Extension> tmpe = sec.getExtensions();
      List<String> tmpp = sec.getSubprotocols();
      List<Class<? extends Decoder>> tmpd = sec.getDecoders();

      if (tmpe != null) {
        log.append("getExtensions() return non-null List|");
        if (!tmpe.isEmpty()) {
          log.append("getExtensions() return non-empty List  with size "
              + tmpe.size() + "|");
          int size_actual = tmpe.size();
          if (size_actual == extensions.size()) {
            for (Extension ext : tmpe) {
              if (extensions.contains(ext)) {
                log.append("extension " + ext + " found is expected|");
              } else {
                passed = false;
                log.append("extension " + ext + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append(
                "getExtensions() returned not exactly the same amount of extensions.");
            log.append("Expecting two, returned " + size_actual);
          }
        } else {
          log.append("getExtensions() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getExtensions() return null list|");
      }

      if (tmpp != null) {
        log.append("getSubprotocols() return non-null List|");
        if (!tmpp.isEmpty()) {
          log.append("getSubprotocols() return non-empty List with size "
              + tmpp.size() + "|");
          int size_actual = tmpp.size();
          if (size_actual == expected_subprotocols.size()) {
            for (String subpro : tmpp) {
              if (expected_subprotocols.contains(subpro)) {
                log.append("sub protocol " + subpro + " found is expected|");
              } else {
                passed = false;
                log.append(
                    "sub protocol " + subpro + " found is not expected|");
              }
            }
          } else {
            passed = false;
            log.append("getSubprotocols() returned not exactly the same size.");
            log.append("Expecting seven, returned " + size_actual);
          }
        } else {
          log.append("getSubprotocols() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getSubprotocols() return null list");
      }

      if (tmpd != null) {
        log.append("getDecoders() return non-null List|");
        if (!tmpd.isEmpty()) {
          int size = tmpd.size();
          log.append("getDecoders() return non-empty List  with size "
              + tmpd.size() + "|");
          if (size == 2) {
            for (Class<? extends Decoder> decoder : tmpd) {
              log.append("Decoder " + decoder + " found ");
              if (expected_decoders.contains(decoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getDecoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getDecoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getDecoders() return null list|");
      }

      List<Class<? extends Encoder>> tmpen = sec.getEncoders();
      if (tmpen != null) {
        log.append("getEncoders() return non-null List");
        if (!tmpen.isEmpty()) {
          int size = tmpen.size();
          log.append(
              "getEncoders() return non-empty List  with size " + size + "|");
          if (size == 2) {
            for (Class<? extends Encoder> encoder : tmpen) {
              log.append("Encoder " + encoder + " found ");
              if (expected_encoders.contains(encoder)) {
                log.append("is expected.|");
              } else {
                passed = false;
                log.append("is not expected.|");
              }
            }
          } else {
            passed = false;
            log.append("getEncoders() returned not exactly the same size.");
            log.append("Expecting two, returned " + size);
          }
        } else {
          log.append("getEncoders() return empty List|");
          passed = false;
        }
      } else {
        passed = false;
        log.append("getEncoders() return null list");
      }
    } catch (Exception e) {
      log.append(e.getMessage());
      passed = false;
    }

    System.out.println(log.toString());

    if (passed == false) {
      throw new Fault("Test failed: " + log.toString());
    }
  }

  public void cleanup() {
  }
}

class TCKConfigurator extends ServerEndpointConfig.Configurator {

  public static final TCKEndpoint testEndpoint = new TCKEndpoint();

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass)
      throws InstantiationException {
    if (endpointClass.equals(TCKEndpoint.class)) {
      return (T) testEndpoint;
    }
    throw new InstantiationException();
  }
}
