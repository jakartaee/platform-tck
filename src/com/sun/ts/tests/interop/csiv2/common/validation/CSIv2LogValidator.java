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

package com.sun.ts.tests.interop.csiv2.common.validation;

import java.util.*;
import java.util.logging.Level;

import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.lib.util.*;

/**
 * Validates a CSIv2Log, using different validation strategies.
 *
 * @author Mark Roth
 */
public class CSIv2LogValidator {
  /** The CSIv2 Log to validate, already parsed. */
  private AssertionEntry assertionEntry;

  /** The strategy to use to validate IORs */
  private IORValidationStrategy iorValidationStrategy;

  /** The strategy to use for validating invocation requests */
  private RequestValidationStrategy requestValidationStrategy;

  /** The strategy to use for validating invocation responses */
  private ResponseValidationStrategy responseValidationStrategy;

  /** The flag to decide if we will do remote interface validation */
  private boolean validateRemote = true;

  /** The expected reply for home and remote */
  private String expectedHome = ReplyEntry.CREATE_EXCEPTION;

  private String expectedRemote = ReplyEntry.NO_EXCEPTION;

  /** Constant to select the EJBHome interface for validation */
  private static final String EJB_HOME = "EJBHome";

  /** Constant to select the EJBRemote interface for validation */
  private static final String EJB_REMOTE = "EJBRemote";

  /** Constant to select the request for validation */
  private static final String REQUEST = "Request";

  /** Constant to select the response for validation */
  private static final String RESPONSE = "Response";

  private static boolean isInterceptorLoggingRequired = true;

  /** Publicly accessible log, for all validation. */
  private OutputLog log = new OutputLog();

  private static final String linePrefix = "----------[  ";

  private static final String lineSuffix = "  ]----------\n";

  /**
   * Creates a new CSIv2Log validator.
   *
   * @param assertionName
   *          The name of the assertion, so that the correct subset of the log
   *          can be found.
   * @param csiv2LogEntry
   *          The CSIv2 log to be validated
   * @param iorValidationStrategy
   *          The strategy to use for IOR validation, or null if no IOR
   *          validation is to be done.
   * @param requestValidationStrategy
   *          The strategy to use for invocation request validation, or null if
   *          no invocation request validation is to be done.
   * @param responseValidationStrategy
   *          The strategy to use for invocation response validation, or null if
   *          no invocation response validation is to be done.
   * @exception ValidationException
   *              Thrown if the assertion could not be found in the given log.
   */
  public CSIv2LogValidator(String assertionName, CSIv2LogEntry csiv2LogEntry,
      IORValidationStrategy iorValidationStrategy,
      RequestValidationStrategy requestValidationStrategy,
      ResponseValidationStrategy responseValidationStrategy)
      throws ValidationException {

    if (isInterceptorLoggingRequired) {
      this.assertionEntry = findAssertionEntry(csiv2LogEntry, assertionName);
    }
    this.iorValidationStrategy = iorValidationStrategy;
    this.requestValidationStrategy = requestValidationStrategy;
    this.responseValidationStrategy = responseValidationStrategy;
  }

  /**
   * Creates a new CSIv2Log validator for negative cases.
   *
   * @param assertionName
   *          The name of the assertion, so that the correct subset of the log
   *          can be found.
   * @param csiv2LogEntry
   *          The CSIv2 log to be validated
   * @param iorValidationStrategy
   *          The strategy to use for IOR validation, or null if no IOR
   *          validation is to be done.
   * @param requestValidationStrategy
   *          The strategy to use for invocation request validation, or null if
   *          no invocation request validation is to be done.
   * @param responseValidationStrategy
   *          The strategy to use for invocation response validation, or null if
   *          no invocation response validation is to be done.
   * @param expectedHome
   *          The expected result from calling home methods.
   * @param expectedRemote
   *          The expected result from calling remote methods.
   * @exception ValidationException
   *              Thrown if the assertion could not be found in the given log.
   */
  public CSIv2LogValidator(String assertionName, CSIv2LogEntry csiv2LogEntry,
      IORValidationStrategy iorValidationStrategy,
      RequestValidationStrategy requestValidationStrategy,
      ResponseValidationStrategy responseValidationStrategy,
      String expectedHome, String expectedRemote) throws ValidationException {

    this(assertionName, csiv2LogEntry, iorValidationStrategy,
        requestValidationStrategy, responseValidationStrategy);

    this.expectedHome = expectedHome;
    this.expectedRemote = expectedRemote;
    // we won't check remote interface for negative cases
    this.validateRemote = false;

  }

  /**
   * Static utility method that returns true if the given log passes validation.
   * Called directly from Client.java.
   * <p>
   * In general, forward-direction test cases will pass in null for the ior and
   * response validation parameters. Reverse-direction test cases will pass in
   * null for the request validation parameter.
   *
   * @param assertionName
   *          The name of the assertion to search for within the log. A log can
   *          potentially contain more than one assertion.
   * @param logContents
   *          A String containing the log to verify
   * @param iorValidator
   *          The validation strategy used to validate the relevant IOR in the
   *          log, or null to pass the IOR regardless of its presence, or
   *          accuracy.
   * @param requestValidator
   *          The validation strategy used to validate the relevant invocation
   *          request (e.g. EstablishContext).
   * @param responseValidator
   *          The validation strategy used to validate the relevant invocation
   *          response (e.g. CompleteEstablishContext).
   */
  public static boolean validate(String assertionName, String logContents,
      IORValidationStrategy iorValidator,
      RequestValidationStrategy requestValidator,
      ResponseValidationStrategy responseValidator) {
    boolean pass = false;

    isInterceptorLoggingRequired = getSysPropInterceptorLoggingRequired();
    if (isInterceptorLoggingRequired) {

      try {
        CSIv2LogEntry csiv2LogEntry = new Parser().parse(logContents);
        pass = new CSIv2LogValidator(assertionName, csiv2LogEntry, iorValidator,
            requestValidator, responseValidator).validate();
      } catch (ParseException e) {
        TestUtil.logErr("CSIv2 Log Parser Exception: ", e);
      } catch (ValidationException e) {
        TestUtil.logErr("CSIv2 Log Validation Exception: ", e);
      }
    } else {
      interceptorLoggingDisabledMsg();
      pass = true;
    }

    return pass;
  }

  /**
   * Static utility method for negative cases. It returns true if the given log
   * passes validation. Called directly from Client.java.
   * <p>
   * In general, forward-direction test cases will pass in null for the ior and
   * response validation parameters. Reverse-direction test cases will pass in
   * null for the request validation parameter.
   *
   * @param assertionName
   *          The name of the assertion to search for within the log. A log can
   *          potentially contain more than one assertion.
   * @param logContents
   *          A String containing the log to verify
   * @param iorValidator
   *          The validation strategy used to validate the relevant IOR in the
   *          log, or null to pass the IOR regardless of its presence, or
   *          accuracy.
   * @param requestValidator
   *          The validation strategy used to validate the relevant invocation
   *          request (e.g. EstablishContext).
   * @param responseValidator
   *          The validation strategy used to validate the relevant invocation
   *          response (e.g. CompleteEstablishContext).
   * @param expectedHome
   *          The expected result from calling home methods.
   * @param expectedRemote
   *          The expected result from calling remote methods.
   */
  public static boolean validate(String assertionName, String logContents,
      IORValidationStrategy iorValidator,
      RequestValidationStrategy requestValidator,
      ResponseValidationStrategy responseValidator, String expectedHome,
      String expectedRemote) {
    boolean pass = false;

    isInterceptorLoggingRequired = getSysPropInterceptorLoggingRequired();
    if (isInterceptorLoggingRequired) {

      try {
        CSIv2LogEntry csiv2LogEntry = new Parser().parse(logContents);
        pass = new CSIv2LogValidator(assertionName, csiv2LogEntry, iorValidator,
            requestValidator, responseValidator, expectedHome, expectedRemote)
                .validate();
      } catch (ParseException e) {
        TestUtil.logErr("CSIv2 Log Parser Exception: ", e);
      } catch (ValidationException e) {
        TestUtil.logErr("CSIv2 Log Validation Exception: ", e);
      }
    } else {
      interceptorLoggingDisabledMsg();
      pass = true;
    }

    return pass;
  }

  private static void interceptorLoggingDisabledMsg() {

    TestUtil.logMsg("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    TestUtil.logMsg("$$ : Interceptor Logging disabled !!!             $$");
    TestUtil.logMsg("$$ : This mode verifies ejb invocations only !!!  $$");
    TestUtil.logMsg("$$ : CSIv2 Protocol elements not validated !!!    $$");
    TestUtil.logMsg("$$ :                                              $$");
    TestUtil.logMsg("$$ : Enable Interceptor logging using             $$");
    TestUtil.logMsg("$$ :  -Dinterceptor.logging.required=true         $$");
    TestUtil.logMsg("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
  }

  /**
   * Runs all validation strategies, and returns the logical AND of each.
   *
   * @return true if validation passes, false if not
   * @exception ValidationException
   *              Thrown if a problem occurred while attempting to validate.
   */
  public boolean validate() throws ValidationException {
    boolean iorsValid = false;
    boolean replyValid = false;
    boolean invocationRequestValid = false;
    boolean invocationResponseValid = false;

    if (isInterceptorLoggingRequired) {

      log.logInfo(linePrefix + "1) Validate Replies" + lineSuffix);
      // Validate EJBHome and EJBRemote Reply:
      if (expectedHome != null && expectedRemote != null) {
        replyValid = validateReply(EJB_HOME, expectedHome)
            & (!validateRemote || validateReply(EJB_REMOTE, expectedRemote));
      } else {
        replyValid = true;
        log.logInfo("Not expecting reply. Skipping Reply validation.");
      }

      log.logInfo(linePrefix + "2) Validate IORs" + lineSuffix);
      // Validate EJBHome and EJBRemote IORs:
      if (iorValidationStrategy != null) {
        iorsValid = validateIOR(EJB_HOME)
            & (!validateRemote || validateIOR(EJB_REMOTE));
      } else {
        iorsValid = true;
        log.logInfo("Forward Test. Skipping IOR validation.");
      }

      log.logInfo(linePrefix + "3) Validate Invocation Requests" + lineSuffix);
      // Validate EJBHome and EJBRemote Invocation Request:
      if (requestValidationStrategy != null) {
        invocationRequestValid = validateInvocation(EJB_HOME, REQUEST)
            & (!validateRemote || validateInvocation(EJB_REMOTE, REQUEST));
      } else {
        invocationRequestValid = true;
        log.logInfo("Reverse Test. Skipping invocation request validation.");
      }

      log.logInfo(linePrefix + "4) Validate Invocation responses" + lineSuffix);
      // Validate EJBHome and EJBRemote Invocations Response:
      if (responseValidationStrategy != null) {
        invocationResponseValid = validateInvocation(EJB_HOME, RESPONSE)
            & (!validateRemote || validateInvocation(EJB_REMOTE, RESPONSE));
      } else {
        invocationResponseValid = true;
        log.logInfo("Forward Test. Skipping invocation response validation.");
      }
    } else {
      interceptorLoggingDisabledMsg();
      iorsValid = true;
      replyValid = true;
      invocationRequestValid = true;
      invocationResponseValid = true;
    }

    return replyValid && iorsValid && invocationRequestValid
        && invocationResponseValid;
  }

  /**
   * Validates the given IOR.
   *
   * @param whichIOR
   *          The IOR to validate (EJBHome or EJBRemote).
   * @exception ValidationException
   *              Thrown if validation fails.
   */
  private boolean validateIOR(String whichIOR) throws ValidationException {
    boolean iorValid = false;
    if (isInterceptorLoggingRequired) {

      IOREntry iorEntry = findLastIOREntry(whichIOR);

      if (iorEntry == null) {
        throw new ValidationException(
            "Could not find " + whichIOR + " IOR Entry.");
      }

      log.logInfo("Validating " + whichIOR + " IOR...");
      iorValid = iorValidationStrategy.validate(iorEntry);
      log.logInfo(whichIOR + " IOR " + (iorValid ? "Valid.\n" : "Invalid.\n"));
    } else {
      interceptorLoggingDisabledMsg();
      iorValid = true;
    }

    return iorValid;
  }

  /**
   * Validates the given reply.
   *
   * @param whichReply
   *          The reply to validate (EJBHome or EJBRemote).
   * @param expectedReply
   *          The expected reply (EJBHome or EJBRemote).
   * @exception ValidationException
   *              Thrown if validation fails.
   */
  private boolean validateReply(String whichReply, String expectedReply)
      throws ValidationException {
    boolean replyValid = false;
    if (isInterceptorLoggingRequired) {
      ReplyEntry replyEntry = findLastReplyEntry(whichReply);

      if (replyEntry == null) {
        throw new ValidationException(
            "Could not find " + whichReply + " Reply Entry.");
      }

      log.logInfo("Validating " + whichReply + " reply...");
      replyValid = replyEntry.getReplyType().equals(expectedReply);
      if (replyValid)
        log.logInfo("Got expected " + expectedReply + " reply.");
      else
        log.logInfo("Got unexpected " + replyEntry.getReplyType()
            + ", expected " + expectedReply + " reply.");
      log.logInfo(
          whichReply + " reply " + (replyValid ? "Valid." : "Invalid."));
    } else {
      interceptorLoggingDisabledMsg();
      replyValid = true;
    }

    return replyValid;
  }

  /**
   * Validates the given invocation request.
   *
   * @param whichIOR
   *          The invocation to validate (EJBHome or EJBRemote).
   * @param requestOrResponse
   *          Are we validating the request, or the response?
   * @exception ValidationException
   *              Thrown if validation fails.
   */
  private boolean validateInvocation(String whichInvocation,
      String requestOrResponse) throws ValidationException {
    String invocationName = whichInvocation + " Invocation "
        + requestOrResponse;
    boolean invocationValid = false;
    if (isInterceptorLoggingRequired) {
      ClientEntry clientEntry = findLastInvocationEntry(whichInvocation);

      if (clientEntry == null) {
        throw new ValidationException(
            "Could not find " + invocationName + " Entry.");
      } else {
        log.logInfo("Validating " + invocationName + "...");
        if (requestOrResponse.equals(REQUEST)) {
          invocationValid = requestValidationStrategy.validate(clientEntry);
        } else {
          invocationValid = responseValidationStrategy.validate(clientEntry);
        }
        log.logInfo(
            invocationName + " " + (invocationValid ? "Valid." : "Invalid."));
      }
    } else {
      interceptorLoggingDisabledMsg();
      invocationValid = true;
    }

    return invocationValid;
  }

  /**
   * Searches for the assertion node within the CSIv2LogEntry.
   *
   * @param csiv2LogEntry
   *          the CSIv2Log to search
   * @param assertionName
   *          the name of the assertion to find
   * @return The corresponding AssertionEntry
   * @exception ValidationException
   *              Thrown if the assertion could not be found.
   */
  private AssertionEntry findAssertionEntry(CSIv2LogEntry csiv2LogEntry,
      String assertionName) throws ValidationException {
    AssertionEntry result = null;
    Vector assertions = csiv2LogEntry.getAssertions();

    for (int i = 0; i < assertions.size(); i++) {
      AssertionEntry entry = (AssertionEntry) assertions.elementAt(i);
      if (entry.getName().equals(assertionName)) {
        result = entry;
      }
    }

    if (result == null) {
      throw new ValidationException(
          "Could not find assertion node " + assertionName + " in CSIv2 log.");
    }

    return result;
  }

  /**
   * Searches for the IOREntry to be validated for the EJBHome or EJBRemote
   * interface.
   * <p>
   * This is done by finding the LAST client-interceptor node, and following it
   * until location-forward is false for that node.
   *
   * @param whichIOR
   *          Which IOR to retrieve( EJBHome or EJBRemote )
   * @return The IOREntry to be validated.
   * @exception ValidationException
   *              Thrown if the IOREntry could not be found, or an error was
   *              encountered while trying to find it.
   */
  private IOREntry findLastIOREntry(String whichIOR)
      throws ValidationException {
    // Traverse the tree depth-first for all client-interceptor nodes.
    // Keep track of the last one found.
    InvocationEntry invocation = assertionEntry.getInvocation();
    return findLastIOREntryInInvocation(null, whichIOR, invocation);
  }

  /**
   * Searches for the ReplyEntry to be validated for the EJBHome or EJBRemote
   * interface.
   *
   * @param whichReply
   *          Which reply to retrieve( EJBHome or EJBRemote )
   * @return The ReplyEntry to be validated.
   */
  private ReplyEntry findLastReplyEntry(String whichReply) {
    // Traverse the tree depth-first for all reply nodes.
    // Keep track of the last one found.
    InvocationEntry invocation = assertionEntry.getInvocation();
    return findLastReplyEntryInInvocation(null, whichReply, invocation);
  }

  /**
   * Utility method for findLastIOREntry
   */
  private IOREntry findLastIOREntryInInvocation(IOREntry lastFound,
      String whichIOR, InvocationEntry invocation) {
    IOREntry result = lastFound;

    ClientEntry clientHome = null;
    ClientEntry clientRemote = null;

    Vector ejbHomes = invocation.getEjbHomes();
    if (ejbHomes.size() > 0) {
      EJBHomeEntry lastEJBHome = (EJBHomeEntry) ejbHomes
          .elementAt(ejbHomes.size() - 1);
      clientHome = lastEJBHome.getClient();
    }

    Vector ejbRemotes = invocation.getEjbRemotes();
    if (ejbRemotes.size() > 0) {
      EJBRemoteEntry lastEJBRemote = (EJBRemoteEntry) ejbRemotes
          .elementAt(ejbRemotes.size() - 1);
      clientRemote = lastEJBRemote.getClient();
    }

    if (whichIOR.equals(EJB_HOME) && (clientHome != null)) {
      ClientInterceptorEntry clientInterceptor = clientHome
          .getClientInterceptor();
      if (clientInterceptor != null) {
        result = clientInterceptor.getIor();
      }
    } else if (whichIOR.equals(EJB_REMOTE) && (clientRemote != null)) {
      ClientInterceptorEntry clientInterceptor = clientRemote
          .getClientInterceptor();
      if (clientInterceptor != null) {
        result = clientInterceptor.getIor();
      }
    }

    // Follow the client remote branch - there will be nothing
    // interesting to follow on the client home branch.
    if (clientRemote != null) {
      ClientInterceptorEntry clientInterceptor = clientRemote
          .getClientInterceptor();
      ServerInterceptorEntry serverInterceptor = clientRemote
          .getServerInterceptor();
      ServerEntry server = clientRemote.getServer();

      if (clientInterceptor != null) {
        result = findLastIOREntryInClientInterceptor(result, whichIOR,
            clientInterceptor);
      } else if (serverInterceptor != null) {
        ServerEntry server2 = serverInterceptor.getServer();
        if (server2 != null) {
          result = findLastIOREntryInServer(result, whichIOR, server2);
        }
      } else if (server != null) {
        result = findLastIOREntryInServer(result, whichIOR, server);
      }
    }

    return result;
  }

  /**
   * Utility method for findLastReplyEntry
   */
  private ReplyEntry findLastReplyEntryInInvocation(ReplyEntry lastFound,
      String whichReply, InvocationEntry invocation) {
    ReplyEntry result = lastFound;

    ClientEntry clientHome = null;
    ClientEntry clientRemote = null;

    Vector ejbHomes = invocation.getEjbHomes();
    if (ejbHomes.size() > 0) {
      EJBHomeEntry lastEJBHome = (EJBHomeEntry) ejbHomes
          .elementAt(ejbHomes.size() - 1);
      clientHome = lastEJBHome.getClient();
    }

    Vector ejbRemotes = invocation.getEjbRemotes();
    if (ejbRemotes.size() > 0) {
      EJBRemoteEntry lastEJBRemote = (EJBRemoteEntry) ejbRemotes
          .elementAt(ejbRemotes.size() - 1);
      clientRemote = lastEJBRemote.getClient();
    }

    if (whichReply.equals(EJB_HOME) && (clientHome != null)) {
      result = clientHome.getReply();
    } else if (whichReply.equals(EJB_REMOTE) && (clientRemote != null)) {
      result = clientRemote.getReply();
    }

    return result;
  }

  /**
   * Utility method for findLastIOREntry
   */
  private IOREntry findLastIOREntryInClientInterceptor(IOREntry lastFound,
      String whichIOR, ClientInterceptorEntry clientInterceptor) {
    IOREntry result = lastFound;

    // Recursively dive into all server-interceptor and server nodes.
    ServerInterceptorEntry serverInterceptor = clientInterceptor
        .getServerInterceptor();
    ServerEntry server = clientInterceptor.getServer();

    if (serverInterceptor != null) {
      ServerEntry server2 = serverInterceptor.getServer();
      if (server2 != null) {
        result = findLastIOREntryInServer(result, whichIOR, server2);
      }
    } else if (server != null) {
      result = findLastIOREntryInServer(result, whichIOR, server);
    }

    // Traverse until we no longer hit a location forward node.
    ClientInterceptorEntry clientInterceptor2 = clientInterceptor
        .getClientInterceptor();
    if (clientInterceptor2 != null) {
      result = findLastIOREntryInClientInterceptor(result, whichIOR,
          clientInterceptor2);
    }

    return result;
  }

  /**
   * Utility method for findLastIOREntry
   */
  private IOREntry findLastIOREntryInServer(IOREntry lastFound, String whichIOR,
      ServerEntry server) {
    IOREntry result = lastFound;

    InvocationEntry invocation = server.getInvocation();
    if (invocation != null) {
      result = findLastIOREntryInInvocation(lastFound, whichIOR, invocation);
    }

    return result;
  }

  /**
   * Searches for the ClientEntry to be validated for the EJBHome or EJBRemote
   * interface.
   * <p>
   * This is done by traversing the tree depth-first, and finding the last
   * invocation node encountered in the entire tree. Then, depending on the
   * value for whichInvocation, the ejb-home or ejb-remote node is selected, and
   * it's client element is returned.
   *
   * @param whichInvocation
   *          Which invocation to retrieve( EJBHome or EJBRemote )
   * @return The ClientEntry to be validated.
   * @exception ValidationException
   *              Thrown if the clientEntry could not be found, or an error was
   *              encountered while trying to find it.
   */
  private ClientEntry findLastInvocationEntry(String whichInvocation)
      throws ValidationException {
    ClientEntry result;

    // Traverse the tree depth-first for all client nodes.
    // Keep track of the last one found.
    InvocationEntry invocation = assertionEntry.getInvocation();
    return findLastClientEntryInInvocation(null, whichInvocation, invocation);
  }

  // TODO: Factor below code to above code.

  /**
   * Utility method for findLastInvocationEntry
   */
  private ClientEntry findLastClientEntryInInvocation(ClientEntry lastFound,
      String whichInvocation, InvocationEntry invocation) {
    ClientEntry result = lastFound;

    ClientEntry clientHome = null;
    ClientEntry clientRemote = null;

    Vector ejbHomes = invocation.getEjbHomes();
    if (ejbHomes.size() > 0) {
      EJBHomeEntry lastEJBHome = (EJBHomeEntry) ejbHomes
          .elementAt(ejbHomes.size() - 1);
      clientHome = lastEJBHome.getClient();
    }

    Vector ejbRemotes = invocation.getEjbRemotes();
    if (ejbRemotes.size() > 0) {
      EJBRemoteEntry lastEJBRemote = (EJBRemoteEntry) ejbRemotes
          .elementAt(ejbRemotes.size() - 1);
      clientRemote = lastEJBRemote.getClient();
    }

    if (whichInvocation.equals(EJB_HOME) && (clientHome != null)) {
      result = clientHome;
    } else if (whichInvocation.equals(EJB_REMOTE) && (clientRemote != null)) {
      result = clientRemote;
    }

    // Follow the client remote branch - there will be nothing
    // interesting to follow on the client home branch.
    if (clientRemote != null) {
      ClientInterceptorEntry clientInterceptor = clientRemote
          .getClientInterceptor();
      ServerInterceptorEntry serverInterceptor = clientRemote
          .getServerInterceptor();
      ServerEntry server = clientRemote.getServer();

      if (clientInterceptor != null) {
        result = findLastClientEntryInClientInterceptor(result, whichInvocation,
            clientInterceptor);
      } else if (serverInterceptor != null) {
        ServerEntry server2 = serverInterceptor.getServer();
        if (server2 != null) {
          result = findLastClientEntryInServer(result, whichInvocation,
              server2);
        }
      } else if (server != null) {
        result = findLastClientEntryInServer(result, whichInvocation, server);
      }
    }

    return result;
  }

  private ClientEntry findLastClientEntryInClientInterceptor(
      ClientEntry lastFound, String whichInvocation,
      ClientInterceptorEntry clientInterceptor) {
    ClientEntry result = lastFound;

    // Recursively dive into all server-interceptor and server nodes.
    ServerInterceptorEntry serverInterceptor = clientInterceptor
        .getServerInterceptor();
    ServerEntry server = clientInterceptor.getServer();

    if (serverInterceptor != null) {
      ServerEntry server2 = serverInterceptor.getServer();
      if (server2 != null) {
        result = findLastClientEntryInServer(result, whichInvocation, server2);
      }
    } else if (server != null) {
      result = findLastClientEntryInServer(result, whichInvocation, server);
    }

    // Traverse until we no longer hit a location forward node.
    ClientInterceptorEntry clientInterceptor2 = clientInterceptor
        .getClientInterceptor();
    if (clientInterceptor2 != null) {
      result = findLastClientEntryInClientInterceptor(result, whichInvocation,
          clientInterceptor2);
    }

    return result;
  }

  private ClientEntry findLastClientEntryInServer(ClientEntry lastFound,
      String whichInvocation, ServerEntry server) {
    ClientEntry result = lastFound;

    InvocationEntry invocation = server.getInvocation();
    if (invocation != null) {
      result = findLastClientEntryInInvocation(lastFound, whichInvocation,
          invocation);
    }

    return result;
  }

  private static boolean getSysPropInterceptorLoggingRequired() {
    String sysPropText = System.getProperty("interceptor.logging.required");

    if (sysPropText != null) {
      isInterceptorLoggingRequired = Boolean.parseBoolean(sysPropText);
    }

    return isInterceptorLoggingRequired;
  }
}
