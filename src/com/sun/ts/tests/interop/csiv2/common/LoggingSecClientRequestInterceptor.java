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

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.enterprise.iiop.security.*;
import com.sun.enterprise.util.*;
import com.sun.corba.ee.spi.legacy.connection.Connection;
import com.sun.corba.ee.spi.legacy.interceptor.RequestInfoExt;

import org.omg.CORBA.portable.ObjectImpl;

import com.sun.corba.ee.org.omg.CSIIOP.*;
import com.sun.corba.ee.spi.ior.IOR;
import com.sun.corba.ee.spi.orb.ORB;
import com.sun.corba.ee.spi.ior.iiop.IIOPProfile;
import com.sun.corba.ee.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.ee.spi.ior.iiop.IIOPAddress;
// ORBUtility moved from com.sun.corba.ee.impl.orbutil to com.sun.corba.ee.impl.misc
// in GF 4.0, but it appears this import is not needed.  Leave commented in case
// it is needed eventually.
//import com.sun.corba.ee.impl.orbutil.ORBUtility; 
import com.sun.corba.ee.impl.encoding.EncapsInputStream;
import com.sun.corba.ee.impl.encoding.CDRInputObject;

import java.net.Socket;
import java.util.*;

import javax.net.ssl.SSLSocket;

import org.omg.CORBA.*;
import org.omg.IOP.*;
import org.omg.PortableInterceptor.*;
import org.omg.IOP.CodecPackage.*;
import org.glassfish.enterprise.iiop.api.GlassFishORBHelper;

import com.sun.logging.LogDomains;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replacement for SecClientInterceptor that also maintains a log. This class
 * was designed to be used for the CSIv2 TS tests.
 */
public class LoggingSecClientRequestInterceptor
    extends SecClientRequestInterceptor {

  private static Logger _logger = null;

  static {
    _logger = LogDomains.getLogger(LoggingSecClientRequestInterceptor.class,
        "com.sun.ts.tests.interop.csiv2.common.LoggingSecClientRequestInterceptor");
  }

  /** Here until this constant is more accessilble. */
  private static final int SECURITY_ATTRIBUTE_SERVICE_ID = 15;

  /** For unmarshalling service contexts */
  private Codec codec;

  private GlassFishORBHelper gHelper;

  // Following padding spaces are necessary to format csiv2 log output
  // Padding space used for formatting IOR and its sub elements
  //
  private static String ior_pad1 = "             "; // 14 spaces(2*(8-1)

  private static String ior_pad2 = "               "; // 16 spaces(2*(9-1)

  private static String ior_pad3 = "                 "; // 18 spaces(2*(10-1)

  private static String ior_pad4 = "                   "; // 20 spaces(2*(11-1)

  private static String ior_pad5 = "                     "; // 22
                                                            // spaces(2*(12-1)

  public LoggingSecClientRequestInterceptor(String name, Codec codec) {
    super(name, codec);
    this.codec = codec;
    _logger.log(Level.FINE, " LoggingSecClientRequestInterceptor constructed.");

    gHelper = getORBHelper();
  }

  public String name() {
    return super.name();
  }

  public void destroy() {
    _logger.log(Level.FINE,
        "CI : LoggingSecClientRequestInterceptor destroyed.");
    super.destroy();
  }

  public void send_request(ClientRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "CI : send_request( " + ri.operation() + ").");

    // Hemanth's fix for ignoring the processing for local calls
    ObjectImpl target = (ObjectImpl) ri.effective_target();
    if (target._is_local()) { // target is local
      return;
    }

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.send_request(ri);
    } else {
      // Increment the watch counter for this method:
      CSIv2Log.incrementWatch(ri.operation());

      NO_PERMISSION np = null;
      StartingPointState startState = collectRelevantStartingState(ri);
      // Do the actual security stuff:
      try {
        super.send_request(ri);
      }

      // In PI, if a starting point throws an exception, the
      // ending point for that interceptor is not invoked. The
      // client and server side interceptor starting points
      // have to be modified to catch the NO_PERMISSION exception.
      // If caught, they need to log the appropriate information
      // and close the log element using </server-interceptor>.

      catch (NO_PERMISSION e) {
        com.sun.ts.lib.util.TestUtil.printStackTrace(e);
        np = e;
      } finally {
        CSIv2Log log = CSIv2Log.getLog();
        logStartClientInterceptor(log, startState, ri);
        if (np != null) {
          log.logEndClientInterceptor();
          throw np;
        }
      }
    }

  }

  public void send_poll(ClientRequestInfo ri) {
    _logger.log(Level.FINE, "CI : send_poll( " + ri.operation() + ").");
    // Our RI currently never causes send_poll to be called.
    super.send_poll(ri);
  }

  public void receive_reply(ClientRequestInfo ri) {
    _logger.log(Level.FINE, "CI : receive_reply( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.receive_reply(ri);
    } else {
      // Decrement the watch counter for this method.
      CSIv2Log.decrementWatch(ri.operation());

      try {
        super.receive_reply(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLog();
        logEndClientInterceptor(log, ri);
      }
    }

  }

  public void receive_exception(ClientRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "CI : receive_exception( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.receive_exception(ri);
    } else {
      // Decrement the watch counter for this method.
      CSIv2Log.decrementWatch(ri.operation());

      try {
        super.receive_exception(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLog();
        logEndClientInterceptor(log, ri);
      }
    }

  }

  public void receive_other(ClientRequestInfo ri) throws ForwardRequest {
    _logger.log(Level.FINE, "CI : receive_other( " + ri.operation() + ").");

    if (!LoggingSecInterceptor.isActive(ri)) {
      super.receive_other(ri);
    } else {
      // Decrement the watch counter for this method.
      CSIv2Log.decrementWatch(ri.operation());

      try {
        super.receive_other(ri);
      } finally {
        CSIv2Log log = CSIv2Log.getLog();
        logEndClientInterceptor(log, ri);
      }
    }

  }

  /**
   * Log all relevant starting interception information
   */
  private void logStartClientInterceptor(CSIv2Log log,
      StartingPointState startState, ClientRequestInfo ri) {
    // Log reply service context:
    log.logStartClientInterceptor();

    // Log operation name:
    log.logOperationName(ri.operation());

    // Now, analyze and log the service context:
    LoggingSecInterceptor.logRequestServiceContext(
        SECURITY_ATTRIBUTE_SERVICE_ID, log, ri, codec);

    // Analyze and log whether ssl is being used:
    log.logSSLUsed(startState.socket instanceof SSLSocket);

    // Analyze and log the target's IOR
    ORB porb = (ORB) gHelper.getORB();
    IOR ior = porb.getIOR(ri.effective_target(), false);
    logIOR(log, porb, ior);
  }

  /**
   * Log all relevant ending interception information
   */
  private void logEndClientInterceptor(CSIv2Log log, ClientRequestInfo ri) {
    // Analyze and log the target's IOR
    ORB porb = (ORB) gHelper.getORB();
    IOR ior = porb.getIOR(ri.effective_target(), false);
    logIOR(log, porb, ior);

    // Log whether this was a location forward reply:
    log.logLocationForward(ri.reply_status() == LOCATION_FORWARD.value);

    // Log the reply service context:
    LoggingSecInterceptor.logReplyServiceContext(SECURITY_ATTRIBUTE_SERVICE_ID,
        log, ri, codec);

    // Log reply service context:
    log.logEndClientInterceptor();
  }

  /**
   * Log detailed summary of IOR. Note that not all fields are logged. Some
   * fields are omitted because they are difficult to log and not relevant to
   * the fields we want to test.
   */
  private void logIOR(CSIv2Log log, ORB orb, IOR ior) {
    CSIV2TaggedComponentInfo csiv2TCInfo = new CSIV2TaggedComponentInfo(orb);
    CompoundSecMech[] mechanisms = csiv2TCInfo.getSecurityMechanisms(ior);
    boolean stateful = isCSIv2IORStateful(orb, ior);

    String iorDetails = "\n";

    IIOPProfile iiopProfile = ior.getProfile();

    IIOPProfileTemplate profileTemplate = (IIOPProfileTemplate) iiopProfile
        .getTaggedProfileTemplate();

    IIOPAddress iiopAddress = profileTemplate.getPrimaryAddress();
    iorDetails += ior_pad1 + "<port>" + iiopAddress.getPort() + "</port>\n";

    iorDetails += ior_pad1 + "<stateful>" + stateful + "</stateful>\n";

    // Log each compound sec mech:
    for (int i = 0; i < mechanisms.length; i++) {
      CompoundSecMech mech = mechanisms[i];

      iorDetails += ior_pad1 + "<compound-sec-mech>\n";

      // log target requires
      iorDetails += ior_pad2 + "<target-requires>" + mech.target_requires
          + "</target-requires>\n";

      // log transport mechanism
      iorDetails += ior_pad2 + "<ior-transport-mech>\n"
          + transportMechXML(mech, orb) + ior_pad2 + "</ior-transport-mech>\n";

      // log as-context
      iorDetails += ior_pad2 + "<ior-as-context>\n"
          + asContextMechXML(mech.as_context_mech) + ior_pad2
          + "</ior-as-context>\n";

      // log sas-context
      iorDetails += ior_pad2 + "<ior-sas-context>\n"
          + sasContextMechXML(mech.sas_context_mech) + ior_pad2
          + "</ior-sas-context>\n";

      iorDetails += ior_pad1 + "</compound-sec-mech>";
    }
    log.logIOR(iorDetails);
  }

  /**
   * Returns true if the "stateful" flag is set to true in the
   * CompoundSecMechList element.
   */
  private boolean isCSIv2IORStateful(ORB orb, IOR ior) {
    IIOPProfile prof = ior.getProfile();

    IIOPProfileTemplate ptemp = (IIOPProfileTemplate) prof
        .getTaggedProfileTemplate();

    Iterator itr = ptemp.iteratorById(TAG_CSI_SEC_MECH_LIST.value);
    if (!itr.hasNext()) {
      _logger.log(Level.FINE,
          "CTS : Error: TAG_CSI_SEC_MECH_LIST tagged component not found");
    }
    java.lang.Object o = itr.next();
    if (itr.hasNext()) {
      _logger.log(Level.FINE,
          "CTS : Error: More than one TAG_CSI_SEC_MECH_LIST tagged "
              + "component found ");
    }
    com.sun.corba.ee.spi.ior.TaggedComponent tcomp = (com.sun.corba.ee.spi.ior.TaggedComponent) o;
    TaggedComponent comp = tcomp.getIOPComponent(orb);
    byte[] b = comp.component_data;
    CDRInputObject in = (CDRInputObject) new EncapsInputStream(orb, b,
        b.length);

    in.consumeEndian();
    CompoundSecMechList l = CompoundSecMechListHelper.read(in);
    return l.stateful;
  }

  /**
   * Returns an XML String representation of the given tranport mechanism
   */
  private String transportMechXML(CompoundSecMech mech, ORB orb) {
    TaggedComponent transportMech = mech.transport_mech;
    String result;
    int tag = transportMech.tag;

    switch (tag) {
    case TAG_TLS_SEC_TRANS.value:
      TLS_SEC_TRANS tls_sec_trans;
      byte[] data = transportMech.component_data;
      CDRInputObject in = (CDRInputObject) new EncapsInputStream(orb, data,
          data.length);

      in.consumeEndian();

      tls_sec_trans = TLS_SEC_TRANSHelper.read(in);

      result = ior_pad3 + "<tls-trans>\n" + ior_pad4 + "<target-supports>"
          + tls_sec_trans.target_supports + "</target-supports>\n" + ior_pad4
          + "<target-requires>" + tls_sec_trans.target_requires
          + "</target-requires>\n";

      TransportAddress[] addrs = tls_sec_trans.addresses;
      for (int i = 0; i < addrs.length; i++) {
        TransportAddress addr = addrs[i];
        result += ior_pad4 + "<trans-addr>\n" + ior_pad5 + "<host-name>"
            + addr.host_name + "</host-name>\n" + ior_pad5 + "<port>"
            + addr.port + "</port>\n" + ior_pad4 + "</trans-addr>\n";
      }

      result += ior_pad3 + "</tls-trans>\n";
      break;

    case TAG_NULL_TAG.value:
      result = ior_pad3 + "<null-trans/>\n";
      break;
    default:
      result = ior_pad3 + "<other-trans details=\"tag=" + tag + "\"/>\n";
      break;
    }

    return result;
  }

  /**
   * Returns an XML String representation of the given AS context mechanism
   */
  private String asContextMechXML(AS_ContextSec asContextMech) {
    String result;

    result = ior_pad3 + "<target-supports>" + asContextMech.target_supports
        + "</target-supports>\n";

    result += ior_pad3 + "<target-requires>" + asContextMech.target_requires
        + "</target-requires>\n";

    result += ior_pad3 + "<client-authentication-mech>"
        + CSIv2Log.binHex(asContextMech.client_authentication_mech)
        + "</client-authentication-mech>\n";

    byte[] name = asContextMech.target_name;
    result += ior_pad3 + "<target-name>" + CSIv2Log.binHex(name)
        + "</target-name>\n";

    return result;
  }

  /**
   * Returns an XML String representation of the given SAS context mechanism
   */
  private String sasContextMechXML(SAS_ContextSec sasContextMech) {
    String result;

    result = ior_pad3 + "<target-supports>" + sasContextMech.target_supports
        + "</target-supports>\n";

    result += ior_pad3 + "<target-requires>" + sasContextMech.target_requires
        + "</target-requires>\n";

    byte[] naming_mechs[] = sasContextMech.supported_naming_mechanisms;
    for (int i = 0; i < naming_mechs.length; i++) {
      byte[] naming_mech = naming_mechs[i];
      result += ior_pad3 + "<supported-naming-mechanism>"
          + CSIv2Log.binHex(naming_mech) + "</supported-naming-mechanism>\n";
    }

    result += ior_pad3 + "<supported-identity-types>"
        + sasContextMech.supported_identity_types
        + "</supported-identity-types>\n";

    return result;
  }

  /**
   * Collect all relevant starting point state before calling super. This is
   * required because some information may be lost during the call to the
   * security interceptor. In particular, we are concerned about capturing the
   * socket information before it is lost, if the interceptor happens to call
   * another service locally.
   */
  private StartingPointState collectRelevantStartingState(
      ClientRequestInfo ri) {
    StartingPointState result = new StartingPointState();

    // Use the proprietary connection interceptor extension to determine
    // the connection object for this invocation:
    Connection c = ((RequestInfoExt) ri).connection();
    if (c != null) {
      // Retrieve the socket:
      result.socket = c.getSocket();
    }

    return result;
  }

  /**
   * This provides a convenient structure in which to organize any state that is
   * relevant, before we invoke the super interceptor. This structure is
   * appropriate for starting interception points.
   */
  private static class StartingPointState {
    /** The socket on which this invocation was made */
    public Socket socket;
  }

}
