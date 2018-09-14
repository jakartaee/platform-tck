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
 * @(#)ExceptionCheckTag.java	1.2 03/19/02
 */

package com.sun.ts.tests.jstl.common.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * ExceptionCheckTag.java Simple tag to catch Exceptions thrown from nested
 * actions.
 */

public class ExceptionCheckTag extends TagSupport implements TryCatchFinally {

  private static final int BUFFER_SIZE = 255;

  /**
   * A stringified FQCN
   */
  private String _exception = null;

  /**
   * A FWCN for the root cause exception.
   */
  private String _rootException = null;

  /**
   * The var to export test status to
   */
  private String _varName = null;

  /**
   * Flag to check for the existence of a root cause exception.
   */
  private boolean _checkRootCause = false;

  /**
   * String to search for in the exception message.
   */
  private String _exceptionText = null;

  /**
   * Flag indiciating of proper exception was caught or not
   */
  private boolean _exceptionCaught = false;

  /**
   * Holder for any unexpected exceptions caught while testing
   */
  private Exception _unexpectedException = null;

  /**
   * Creates a new instance of ExceptionCheckTag
   */
  public ExceptionCheckTag() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets the type of exception to check for.
   * 
   * @param exception
   *          Exception class.
   */
  public void setException(String exception) {
    this._exception = exception;
  }

  /**
   * Sets the type of root exception to check for.
   * 
   * @param rootException
   *          Exception class.
   */
  public void setRootException(String rootException) {
    this._rootException = rootException;
  }

  /**
   * Sets the name of the variable to export.
   * 
   * @param Name
   *          of the variable to export.
   */
  public void setVar(String var) {
    this._varName = var;
  }

  /**
   * If true, the Exception will check to see if it has a root cause.
   * 
   * @param checkRootCause
   */
  public void setCheckRootCause(String checkRootCause) {
    this._checkRootCause = Boolean.valueOf(checkRootCause).booleanValue();
  }

  /**
   * Sets the text that should be expected in the exception message.
   *
   * @param exceptionText
   *          text to search for
   */
  public void setExceptionText(String exceptionText) {
    this._exceptionText = exceptionText.toLowerCase();
  }

  /**
   * <code>doStartTag</code> has been overridden to return EVAL_BODY_INCLUDE
   *
   * @return <code>EVAL_BODY_INCLUDE</code>
   * @exception JspException
   *              if an error occurs
   */
  public int doStartTag() throws JspException {
    return EVAL_BODY_INCLUDE;
  }

  /**
   * <pre>
   * Called if any nested action throws an Exception.
   * If the Throwable is an instance of the Exception
   * type this action is looking for, then a success
   * message will be exported into the page scope
   * associated with the variable name passed to the
   * action.
   * If the Throwable is not an instance, then following
   * the same logic from above, a message will be exported
   * stating failure.
   * If the exception type passed to the action cannot
   * be found, then a message will be exported indicating 
   * such.
   * </pre>
   *
   * @param java.lang.Throwable
   *          Exception caught from a nested action.
   */
  public void doCatch(java.lang.Throwable t) {
    StringBuffer sb = new StringBuffer(BUFFER_SIZE);
    try {
      Class clazz = Class.forName(_exception);

      if (clazz.isInstance(t)) {
        sb.append("The expected Exception <strong>");
        sb.append(_exception);
        sb.append("</strong> was thrown!");

        if (t instanceof JspException && _checkRootCause) {
          Throwable rt = ((JspException) t).getRootCause();
          if (rt != null) {
            if (_rootException == null) {
              sb.append("<br>\nThe root cause of Exception defined");
            } else {
              try {
                Class root = Class.forName(_rootException);
                if (root.isInstance(rt)) {
                  sb.append("<br>\nThe root cause Exception <strong>");
                  sb.append(_rootException);
                  sb.append("</strong> was of the expected type.");
                } else {
                  sb.append("<br>\nThe root cause Exception <strong>");
                  sb.append(_rootException);
                  sb.append("</strong> was not of the expected type.");
                  sb.append("Exception type received:<strong>");
                  sb.append(rt.getClass());
                  sb.append("</strong>.<br>");
                }
              } catch (Exception e) {
                sb.append("<strong>Error:</strong> The specified ");
                sb.append("Root Exception class <strong>");
                sb.append(_rootException);
                sb.append("</strong> does not exist!");
              }
            }
          } else {
            sb.append("<strong>Error:</strong>");
            sb.append("The expected Exception <strong>");
            sb.append(_exception);
            sb.append("</strong> was thrown but the ");
            sb.append("root cause was not populated");
          }
        }

        if (_exceptionText != null) {
          boolean found = false;
          String toString = t.toString();
          if (toString != null) {
            toString = toString.toLowerCase();
          } else {
            toString = "";
          }

          String message = t.getMessage();
          if (message != null) {
            message = message.toLowerCase();
          } else {
            message = "";
          }

          if (toString.indexOf(_exceptionText) > -1) {
            found = true;
          } else if (message.indexOf(_exceptionText) > -1) {
            found = true;
          }

          if (found) {
            sb.append("<br>\nThe expected Exception text");
            sb.append(" was found in the Exception message!");
          } else {
            sb.append("<br>\n<strong>Error:</strong>");
            sb.append("The expected Exception text <strong>");
            sb.append(_exceptionText);
            sb.append("</strong> was not found in the Exception!");
            sb.append("<br>\nThe exception text was: ");
            sb.append("toString(): ");
            sb.append(toString);
            sb.append(" getMessage(): ");
            sb.append(message);
          }
        }
      } else {
        sb.append("<strong>Error:</strong> ");
        sb.append("The expected Exception <strong>");
        sb.append(_exception);
        sb.append("</strong> was not thrown!<br>\n");
        sb.append("The actual Exception thrown was: <strong>");
        sb.append(t.getClass());
        sb.append("</strong>");
      }
    } catch (Exception e) {
      sb.append("<strong>Error:</strong> The specified ");
      sb.append("Exception class <strong>");
      sb.append(_exception);
      sb.append("</strong> does not exist!");
      e.printStackTrace();
    }
    pageContext.setAttribute(_varName, sb.toString());
  }

  /**
   * Invoked in all cases after doEndTag() for any class implementing Tag,
   * IterationTag or BodyTag.
   */
  public void doFinally() {
    if (pageContext.getAttribute(_varName, PageContext.PAGE_SCOPE) == null) {
      pageContext.setAttribute(_varName,
          "<strong>Error: </strong>No Exception "
              + "thrown!<br>\n The expected Exception " + "was: <strong>"
              + _exception + "</strong>");
    }
  }

  /**
   * <code>release</code> is called by the tag handler to release state. This
   * method is invoked by the JSP page implementation object.
   */
  public void release() {
    _exception = null;
    _unexpectedException = null;
    _varName = null;
    _exceptionCaught = false;
    _checkRootCause = false;
    _exceptionText = null;
    _rootException = null;
  }
}
