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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;

import java.lang.annotation.Annotation;
import javax.xml.ws.WebFault;
import javax.xml.ws.*;
import javax.jws.*;
import javax.jws.soap.*;
import java.lang.reflect.Field;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import java.lang.reflect.Method;

public class AnnotationUtils {

  /**
   * Private to prevent instantiation.
   */
  private AnnotationUtils() {
    super();
  }

  public static Method[] getMethods(Class c) {
    return c.getDeclaredMethods();
  }

  public static Method getMethod(Class c, String methodName) {
    Method[] methods = c.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      if (methodName.equals(method.getName())) {
        return method;
      }
    }
    return null;
  }

  public static boolean verifyXmlElement(XmlElement element, String name,
      String namespace, boolean required, boolean nillable) {
    boolean pass = true;

    TestUtil.logMsg("Verify XmlElement Params: " + element);

    if (element.name().equals(name)) {
      TestUtil.logMsg("name match: " + name);
    } else {
      TestUtil.logErr("name non-match: " + name);
      pass = false;
    }
    if (element.namespace().equals(namespace)) {
      TestUtil.logMsg("namespace match: " + namespace);
    } else {
      TestUtil.logErr("namespace non-match: " + namespace);
      pass = false;
    }
    if (element.required() == required) {
      TestUtil.logMsg("required match: " + required);
    } else {
      TestUtil.logErr("required non-match: " + required);
      pass = false;
    }
    if (element.nillable() == nillable) {
      TestUtil.logMsg("nillable match: " + nillable);
    } else {
      TestUtil.logErr("nillable non-match: " + nillable);
      pass = false;
    }
    return pass;
  }

  public static boolean verifyXmlType(XmlType type, String name,
      String namespace) {
    boolean pass = true;

    TestUtil.logMsg("Verify XmlType Params: " + type);

    if (type.name().equals(name)) {
      TestUtil.logMsg("name match: " + name);
    } else {
      TestUtil.logErr("name non-match: " + name);
      pass = false;
    }
    if (type.namespace().equals(namespace)) {
      TestUtil.logMsg("namespace match: " + namespace);
    } else {
      TestUtil.logErr("namespace non-match: " + namespace);
      pass = false;
    }
    return pass;
  }

  public static Annotation getAnnotationOnClass(Class theClass,
      Class theAnnotationClass) {
    try {
      Annotation a = theClass.getAnnotation(theAnnotationClass);
      if (a != null) {
        System.out.println("Annotation on class (" + theClass
            + ") is present for annotation (" + a + ")");
        return a;
      } else {
        System.out.println("Annotation on class (" + theClass
            + ") is not present for annotation "
            + theAnnotationClass.getName());
        return a;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Annotation getAnnotationOnField(Class theClass, String theField,
      Class theAnnotationClass) {
    try {
      Field f = theClass.getDeclaredField(theField);
      Annotation a = f.getAnnotation(theAnnotationClass);
      if (a != null) {
        System.out.println("Annotation on field (" + theField
            + ") is present for annotation (" + a + ")");
        return a;
      } else {
        System.out.println("Annotation on field (" + theField
            + ") is not present for annotation "
            + theAnnotationClass.getName());
        return a;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean isAnnotationOnFieldPresent(Class theClass,
      String theField, Class theAnnotationClass) {
    try {
      Field f = theClass.getDeclaredField(theField);
      Annotation a = f.getAnnotation(theAnnotationClass);
      if (a != null) {
        System.out.println("Annotation on field (" + theField
            + ") is present for annotation (" + a + ")");
        return true;
      } else {
        System.out.println("Annotation on field (" + theField
            + ") is not present for annotation "
            + theAnnotationClass.getName());
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static String getWsdlLocation(Class c) {
    String wsdlLocation = null;
    if (c.isAnnotationPresent(javax.jws.WebService.class)) {
      WebService ws = (javax.jws.WebService) c
          .getAnnotation(javax.jws.WebService.class);
      wsdlLocation = ws.wsdlLocation();
    }
    return wsdlLocation;
  }

  public static boolean verifyWebServiceAnnotation(Class c, String name,
      String targetNamespace, String serviceName, String wsdlLocation,
      String endpointInterface) {
    boolean result = true;
    if (c.isAnnotationPresent(javax.jws.WebService.class)) {
      WebService ws = (javax.jws.WebService) c
          .getAnnotation(javax.jws.WebService.class);
      if (ws != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(ws.toString());
        if (ws.name() != null) {
          if (!ws.name().equals("")) {
            if (!name.equals(ws.name())) {
              result = false;
              TestUtil.logErr("Error with name attribute:");
              TestUtil.logErr("Expected=" + name);
              TestUtil.logErr("Actual=" + ws.name());
            }
          } else {
            TestUtil.logTrace("name attribute was empty");
          }
        }
        if (ws.targetNamespace() != null) {
          if (!ws.targetNamespace().equals("")) {
            if (!targetNamespace.equals(ws.targetNamespace())) {
              result = false;
              TestUtil.logErr("Error with targetNamespace attribute:");
              TestUtil.logErr("Expected=" + targetNamespace);
              TestUtil.logErr("Actual=" + ws.targetNamespace());
            }
          } else {
            TestUtil.logTrace("targetNamespace attribute was empty");
          }
        }
        if (ws.serviceName() != null) {
          if (!ws.serviceName().equals("")) {
            if (!serviceName.equals(ws.serviceName())) {
              result = false;
              TestUtil.logErr("Error with serviceName attribute:");
              TestUtil.logErr("Expected=" + serviceName);
              TestUtil.logErr("Actual=" + ws.serviceName());
            }
          } else {
            TestUtil.logTrace("serviceName attribute was empty");
          }
        }
        if (ws.wsdlLocation() != null) {
          if (!ws.wsdlLocation().equals("")) {
            if (ws.wsdlLocation().indexOf(wsdlLocation) == -1) {
              result = false;
              TestUtil.logErr("Error with wsdlLocation attribute:");
              TestUtil.logErr("Expected attribute to contain=" + wsdlLocation);
              TestUtil.logErr("Actual=" + ws.wsdlLocation());
            }
          } else {
            TestUtil.logTrace("wsdlLocation attribute was empty");
          }
        }
        if (ws.endpointInterface() != null) {
          if (!ws.endpointInterface().equals("")) {
            if (!endpointInterface.equals(ws.endpointInterface())) {
              result = false;
              TestUtil.logErr("Error with endpointInterface attribute:");
              TestUtil.logErr("Expected=" + endpointInterface);
              TestUtil.logErr("Actual=" + ws.endpointInterface());
            }
          } else {
            TestUtil.logTrace("endpointInterface attribute was empty");
          }
        }
      } else {
        TestUtil.logErr("WebService annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logErr("WebService annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifySOAPBindingAnnotationPerMethod(Class c,
      String methodName, String style, String use, String parameterStyle) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    } else
      TestUtil.logMsg("Method name found for -> " + methodName);
    if (method.isAnnotationPresent(javax.jws.soap.SOAPBinding.class)) {
      SOAPBinding sb = (javax.jws.soap.SOAPBinding) method
          .getAnnotation(javax.jws.soap.SOAPBinding.class);
      if (sb != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(sb.toString());
        if (sb.style().name() != null) {
          if (!style.equals(sb.style().name())) {
            result = false;
            TestUtil.logErr("Error with style attribute:");
            TestUtil.logErr("Expected=" + style);
            TestUtil.logErr("Actual=" + sb.style().name());
          }
        }
        if (sb.use().name() != null) {
          if (!use.equals(sb.use().name())) {
            result = false;
            TestUtil.logErr("Error with use attribute:");
            TestUtil.logErr("Expected=" + use);
            TestUtil.logErr("Actual=" + sb.use().name());
          }
        }
        if (sb.parameterStyle().name() != null) {
          if (!parameterStyle.equals(sb.parameterStyle().name())) {
            result = false;
            TestUtil.logErr("Error with parameterStyle attribute:");
            TestUtil.logErr("Expected=" + parameterStyle);
            TestUtil.logErr("Actual=" + sb.parameterStyle().name());
          }
        }
      } else {
        TestUtil
            .logErr("SOAPBinding annotation for the method returned was null");
        result = false;
      }
    } else {
      TestUtil.logMsg("SOAPBinding annotation for the method not found");
      TestUtil.logMsg("Verify SOAPBinding annotation at the class level ...");
      result = verifySOAPBindingAnnotation(c, style, use, parameterStyle);
      if (!result) {
        TestUtil.logMsg(
            "The operation must have the defaults DOCUMENT/LITERAL/WRAPPED ...");
        if (style.equals("DOCUMENT") && use.equals("LITERAL")
            && parameterStyle.equals("WRAPPED")) {
          result = true;
          TestUtil.logMsg(
              "Operation name -> " + methodName + " is the default case");
        }
      }
    }
    return result;
  }

  public static boolean verifySOAPBindingAnnotation(Class c, String style,
      String use, String parameterStyle) {
    boolean result = true;
    if (c.isAnnotationPresent(javax.jws.soap.SOAPBinding.class)) {
      SOAPBinding sb = (javax.jws.soap.SOAPBinding) c
          .getAnnotation(javax.jws.soap.SOAPBinding.class);
      if (sb != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(sb.toString());
        if (sb.style().name() != null) {
          if (!style.equals(sb.style().name())) {
            result = false;
            TestUtil.logErr("Error with style attribute:");
            TestUtil.logErr("Expected=" + style);
            TestUtil.logErr("Actual=" + sb.style().name());
          }
        }
        if (sb.use().name() != null) {
          if (!use.equals(sb.use().name())) {
            result = false;
            TestUtil.logErr("Error with use attribute:");
            TestUtil.logErr("Expected=" + use);
            TestUtil.logErr("Actual=" + sb.use().name());
          }
        }
        if (sb.parameterStyle().name() != null) {
          if (!parameterStyle.equals(sb.parameterStyle().name())) {
            result = false;
            TestUtil.logErr("Error with parameterStyle attribute:");
            TestUtil.logErr("Expected=" + parameterStyle);
            TestUtil.logErr("Actual=" + sb.parameterStyle().name());
          }
        }
      } else {
        TestUtil.logErr("SOAPBinding annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logMsg("SOAPBinding annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifySOAPBindingAnnotation(Class c, String methodName,
      String style, String use, String parameterStyle) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }

    if (method.isAnnotationPresent(javax.jws.soap.SOAPBinding.class)) {
      SOAPBinding sb = method.getAnnotation(javax.jws.soap.SOAPBinding.class);
      if (sb != null) {
        TestUtil.logMsg("Annotation:");
        TestUtil.logMsg(sb.toString());

        if (sb.style().name() != null) {
          if (!style.equals(sb.style().name())) {
            result = false;
            TestUtil.logErr("Error with style attribute:");
            TestUtil.logErr("Expected=" + style);
            TestUtil.logErr("Actual=" + sb.style().name());
          }
        }
        if (sb.use().name() != null) {
          if (!use.equals(sb.use().name())) {
            result = false;
            TestUtil.logErr("Error with use attribute:");
            TestUtil.logErr("Expected=" + use);
            TestUtil.logErr("Actual=" + sb.use().name());
          }
        }
        if (sb.parameterStyle().name() != null) {
          if (!parameterStyle.equals(sb.parameterStyle().name())) {
            result = false;
            TestUtil.logErr("Error with parameterStyle attribute:");
            TestUtil.logErr("Expected=" + parameterStyle);
            TestUtil.logErr("Actual=" + sb.parameterStyle().name());
          }
        }
      } else {
        result = false;
        TestUtil
            .logErr("The SOAPBinding annotation returned was null for method"
                + methodName);
      }
    } else {
      result = false;
      TestUtil.logErr("SOAPBinding annotation not found");
    }
    return result;
  }

  public static boolean verifyOnewayAnnotation(Class c, String methodName) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      if (!method.isAnnotationPresent(javax.jws.Oneway.class)) {
        result = false;
        TestUtil.logErr("The Oneway annotation was not present");
      }
    }
    return result;
  }

  public static boolean verifyWebResultAnnotation(Class c, String methodName,
      String name, String targetNamespace) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      if (method.isAnnotationPresent(javax.jws.WebResult.class)) {
        WebResult wr = method.getAnnotation(javax.jws.WebResult.class);
        if (wr != null) {
          TestUtil.logTrace("Annotation:");
          TestUtil.logTrace(wr.toString());
          if (wr.name() != null) {
            if (!name.equals(wr.name())) {
              result = false;
              TestUtil.logErr("Error with name attribute:");
              TestUtil.logErr("Expected=" + name);
              TestUtil.logErr("Actual=" + wr.name());
            }
          }
          if (wr.targetNamespace() != null) {
            if (!wr.targetNamespace().equals("")) {
              if (!targetNamespace.equals(wr.targetNamespace())) {
                result = false;
                TestUtil.logErr("Error with targetNamespace attribute:");
                TestUtil.logErr("Expected=" + targetNamespace);
                TestUtil.logErr("Actual=" + wr.targetNamespace());
              }
            } else {
              TestUtil.logTrace("targetNamespace attribute was empty");
            }
          }
        } else {
          result = false;
          TestUtil.logErr("WebResult annotation returned was null");
        }
      } else {
        result = false;
        TestUtil.logErr("WebResult annotation not found");
      }
    }
    return result;
  }

  public static boolean verifyWebParamAnnotation(Class c, int paramIndex,
      String methodName, String name, String targetNamespace, String mode,
      boolean header) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      Annotation[][] aArray = method.getParameterAnnotations();
      for (int j = 0; j < aArray[paramIndex].length; j++) {
        Annotation annotation = aArray[paramIndex][j];
        if (annotation instanceof javax.jws.WebParam) {
          WebParam wp = (WebParam) annotation;
          TestUtil.logTrace("Annotation:");
          TestUtil.logTrace(wp.toString());
          if (wp.name() != null) {
            if (!wp.name().equals("")) {
              if (!name.equals(wp.name())) {
                result = false;
                TestUtil.logErr("Error with name attribute:");
                TestUtil.logErr("Expected=" + name);
                TestUtil.logErr("Actual=" + wp.name());
              }
            } else {
              TestUtil.logTrace("name attribute was empty");
            }
          }
          if (wp.targetNamespace() != null) {
            if (!wp.targetNamespace().equals("")) {
              if (!targetNamespace.equals(wp.targetNamespace())) {
                result = false;
                TestUtil.logErr("Error with targetNamespace attribute:");
                TestUtil.logErr("Expected=" + targetNamespace);
                TestUtil.logErr("Actual=" + wp.targetNamespace());
              }
            } else {
              TestUtil.logTrace("targetNamespace attribute was empty");
            }
          }
          if (wp.mode() != null) {
            if (!mode.equals(wp.mode().name())) {
              result = false;
              TestUtil.logErr("Error with mode attribute:");
              TestUtil.logErr("Expected=" + mode);
              TestUtil.logErr("Actual=" + wp.mode());
            }
          }
          if (header != wp.header()) {
            result = false;
            TestUtil.logErr("Error with header attribute:");
            TestUtil.logErr("Expected=" + header);
            TestUtil.logErr("Actual=" + wp.header());
          }
        } else {
          result = false;
          TestUtil.logErr("WebParam annotation not found");
        }
      }
    }
    return result;
  }

  public static boolean verifyWebMethodAnnotation(Class c, String methodName,
      String operationName, String action) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      if (method.isAnnotationPresent(javax.jws.WebMethod.class)) {
        WebMethod wm = method.getAnnotation(javax.jws.WebMethod.class);
        if (wm != null) {
          TestUtil.logTrace("Annotation:");
          TestUtil.logTrace(wm.toString());
          if (wm.operationName() != null) {
            if (!wm.operationName().equals("")) {
              if (!operationName.equals(wm.operationName())) {
                result = false;
                TestUtil.logErr("Error with operationName attribute:");
                TestUtil.logErr("Expected=" + operationName);
                TestUtil.logErr("Actual=" + wm.operationName());
              }
            } else {
              TestUtil.logTrace("operationName attribute was empty");
            }
          }
          if (wm.action() != null) {
            if (!wm.action().equals("")) {
              if (!action.equals(wm.action())) {
                result = false;
                TestUtil.logErr("Error with action attribute:");
                TestUtil.logErr("Expected=" + action);
                TestUtil.logErr("Actual=" + wm.action());
              }
            } else {
              TestUtil.logTrace("action attribute was empty");
            }
          }
        } else {
          result = false;
          TestUtil.logErr("WebMethod annotation returned was null");
        }
      } else {
        result = false;
        TestUtil.logErr("WebMethod annotation not found");
      }
    }
    return result;
  }

  public static boolean verifyWebFaultAnnotation(Class c, String name,
      String targetNamespace, String faultBean) {
    boolean result = true;
    if (c.isAnnotationPresent(javax.xml.ws.WebFault.class)) {
      WebFault wf = (javax.xml.ws.WebFault) c
          .getAnnotation(javax.xml.ws.WebFault.class);
      if (wf != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(wf.toString());
        if (wf.name() != null) {
          if (!wf.name().equals("")) {
            if (!name.equals(wf.name())) {
              result = false;
              TestUtil.logErr("Error with name attribute:");
              TestUtil.logErr("Expected=" + name);
              TestUtil.logErr("Actual=" + wf.name());
            }
          } else {
            TestUtil.logTrace("name attribute was empty");
          }
        }
        if (wf.targetNamespace() != null) {
          if (!wf.targetNamespace().equals("")) {
            if (!targetNamespace.equals(wf.targetNamespace())) {
              result = false;
              TestUtil.logErr("Error with targetNamespace attribute:");
              TestUtil.logErr("Expected=" + targetNamespace);
              TestUtil.logErr("Actual=" + wf.targetNamespace());
            }
          } else {
            TestUtil.logTrace("targetNamespace attribute was empty");
          }
        }
        if (wf.faultBean() != null) {
          if (!wf.faultBean().equals("")) {
            if (!faultBean.equals(wf.faultBean())) {
              result = false;
              TestUtil.logErr("Error with faultBean attribute:");
              TestUtil.logErr("Expected=" + faultBean);
              TestUtil.logErr("Actual=" + wf.faultBean());
            }
          } else {
            TestUtil.logTrace("faultBean attribute was empty");
          }
        }
      } else {
        result = false;
        TestUtil.logErr("WebFault annotation returned was null");
      }
    } else {
      result = false;
      TestUtil.logErr("WebFault annotation for the class not found");
    }
    return result;
  }

  public static boolean verifyRequestWrapperAnnotation(Class c,
      String methodName, String localName, String targetNamespace,
      String className) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (method.isAnnotationPresent(javax.xml.ws.RequestWrapper.class)) {
      javax.xml.ws.RequestWrapper rw = (javax.xml.ws.RequestWrapper) method
          .getAnnotation(javax.xml.ws.RequestWrapper.class);
      if (rw != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(rw.toString());
        if (rw.localName() != null) {
          if (!rw.localName().equals("")) {
            if (!localName.equals(rw.localName())) {
              result = false;
              TestUtil.logErr("Error with localName attribute:");
              TestUtil.logErr("Expected=" + localName);
              TestUtil.logErr("Actual=" + rw.localName());
            }
          } else {
            TestUtil.logTrace("localName attribute was empty");
          }
        }
        if (rw.targetNamespace() != null) {
          if (!rw.targetNamespace().equals("")) {
            if (!targetNamespace.equals(rw.targetNamespace())) {
              result = false;
              TestUtil.logErr("Error with targetNamespace attribute:");
              TestUtil.logErr("Expected=" + targetNamespace);
              TestUtil.logErr("Actual=" + rw.targetNamespace());
            }
          } else {
            TestUtil.logTrace("targetNamespace attribute was empty");
          }
        }
        if (rw.className() != null) {
          if (!rw.className().equals("")) {
            if (!className.equals(rw.className())) {
              result = false;
              TestUtil.logErr("Error with className attribute:");
              TestUtil.logErr("Expected=" + className);
              TestUtil.logErr("Actual=" + rw.className());
            }
          } else {
            TestUtil.logTrace("className attribute was empty");
          }
        }
      } else {
        TestUtil.logErr("RequestWrapper annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logErr("RequestWrapper annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifyResponseWrapperAnnotation(Class c,
      String methodName, String localName, String targetNamespace,
      String className) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (method.isAnnotationPresent(javax.xml.ws.ResponseWrapper.class)) {
      javax.xml.ws.ResponseWrapper rw = (javax.xml.ws.ResponseWrapper) method
          .getAnnotation(javax.xml.ws.ResponseWrapper.class);
      if (rw != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(rw.toString());
        if (rw.localName() != null) {
          if (!rw.localName().equals("")) {
            if (!localName.equals(rw.localName())) {
              result = false;
              TestUtil.logErr("Error with localName attribute:");
              TestUtil.logErr("Expected=" + localName);
              TestUtil.logErr("Actual=" + rw.localName());
            }
          } else {
            TestUtil.logTrace("localName attribute was empty");
          }
        }
        if (rw.targetNamespace() != null) {
          if (!rw.targetNamespace().equals("")) {
            if (!targetNamespace.equals(rw.targetNamespace())) {
              result = false;
              TestUtil.logErr("Error with targetNamespace attribute:");
              TestUtil.logErr("Expected=" + targetNamespace);
              TestUtil.logErr("Actual=" + rw.targetNamespace());
            }
          } else {
            TestUtil.logTrace("targetNamespace attribute was empty");
          }
        }
        if (rw.className() != null) {
          if (!rw.className().equals("")) {
            if (!className.equals(rw.className())) {
              result = false;
              TestUtil.logErr("Error with className attribute:");
              TestUtil.logErr("Expected=" + className);
              TestUtil.logErr("Actual=" + rw.className());
            }
          } else {
            TestUtil.logTrace("className attribute was empty");
          }
        }
      } else {
        TestUtil.logErr("ResponseWrapper annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logErr("ResponseWrapper annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifyWebServiceClientAnnotation(Class c, String name,
      String targetNamespace, String wsdlLocation) {
    boolean result = true;
    if (c.isAnnotationPresent(javax.xml.ws.WebServiceClient.class)) {
      WebServiceClient wsc = (javax.xml.ws.WebServiceClient) c
          .getAnnotation(javax.xml.ws.WebServiceClient.class);
      if (wsc != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(wsc.toString());
        if (wsc.name() != null) {
          if (!wsc.name().equals("")) {
            if (!name.equals(wsc.name())) {
              result = false;
              TestUtil.logErr("Error with name attribute:");
              TestUtil.logErr("Expected=" + name);
              TestUtil.logErr("Actual=" + wsc.name());
            }
          } else {
            TestUtil.logTrace("name attribute was empty");
          }
        }
        if (wsc.targetNamespace() != null) {
          if (!wsc.targetNamespace().equals("")) {
            if (!targetNamespace.equals(wsc.targetNamespace())) {
              result = false;
              TestUtil.logErr("Error with targetNamespace attribute:");
              TestUtil.logErr("Expected=" + targetNamespace);
              TestUtil.logErr("Actual=" + wsc.targetNamespace());
            }
          } else {
            TestUtil.logTrace("targetNamespace attribute was empty");
          }
        }
        if (wsc.wsdlLocation() != null) {
          if (!wsc.wsdlLocation().equals("")) {
            if (wsc.wsdlLocation().indexOf(wsdlLocation) == -1) {
              result = false;
              TestUtil.logErr("Error with wsdlLocation attribute:");
              TestUtil.logErr("Expected attribute to contain=" + wsdlLocation);
              TestUtil.logErr("Actual=" + wsc.wsdlLocation());
            }
          } else {
            TestUtil.logTrace("wsdlLocation attribute was empty");
          }
        }
      } else {
        TestUtil.logErr("WebServiceClient annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logErr("WebServiceClient annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifyWebEndpointAnnotation(Class c, String methodName,
      String name) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      if (method.isAnnotationPresent(javax.xml.ws.WebEndpoint.class)) {
        WebEndpoint wep = method.getAnnotation(javax.xml.ws.WebEndpoint.class);
        if (wep != null) {
          TestUtil.logTrace("Annotation:");
          TestUtil.logTrace(wep.toString());
          if (wep.name() != null) {
            if (!name.equals(wep.name())) {
              result = false;
              TestUtil.logErr("Error with name attribute:");
              TestUtil.logErr("Expected=" + name);
              TestUtil.logErr("Actual=" + wep.name());
            }
          }
        } else {
          result = false;
          TestUtil.logErr("WebEndpoint annotation returned was null");
        }
      } else {
        result = false;
        TestUtil.logErr("WebEndpoint annotation not found");
      }
    }
    return result;
  }

  public static boolean verifyHandlerChainAnnotation(Class c, String file) {
    boolean result = true;
    if (c.isAnnotationPresent(javax.jws.HandlerChain.class)) {
      HandlerChain hc = (javax.jws.HandlerChain) c
          .getAnnotation(javax.jws.HandlerChain.class);
      if (hc != null) {
        TestUtil.logTrace("Annotation:");
        TestUtil.logTrace(hc.toString());
        if (hc.file() != null) {
          if (file != null) {
            if (!hc.file().equals(file)) {
              result = false;
              TestUtil.logErr("Error with file attribute:");
              TestUtil.logErr("Expected=" + file);
              TestUtil.logErr("Actual=" + hc.file());
            }
          } else {
            if (hc.file().equals("")) {
              result = false;
              TestUtil.logErr("Error with file attribute:");
              TestUtil.logErr(
                  "An empty string was returned and it should not be empty");
            }
          }
        } else {
          TestUtil.logTrace("file attribute returned a null");
        }
      } else {
        TestUtil.logErr("HandlerChain annotation returned was null");
        result = false;
      }
    } else {
      TestUtil.logErr("HandlerChain annotation for the class not found");
      result = false;
    }
    return result;
  }

  public static boolean verifyActionFaultActionAnnotation(Class c,
      String methodName, boolean shouldExist, String inputName,
      String outputName, String[][] faultArray) {
    boolean result = true;
    Method method = getMethod(c, methodName);
    if (method == null) {
      TestUtil.logErr("Method name not found for -> " + methodName);
      return false;
    }
    if (methodName.equals(method.getName())) {
      if (method.isAnnotationPresent(javax.xml.ws.Action.class)) {
        if (shouldExist) {
          Action action = method.getAnnotation(javax.xml.ws.Action.class);
          if (action != null) {
            TestUtil.logTrace("Annotation:");
            TestUtil.logTrace(action.toString());
            if (inputName != null) {
              if (!action.input().equals("")) {
                if (!inputName.equals(action.input())) {
                  result = false;
                  TestUtil.logErr("Error with input attribute:");
                  TestUtil.logErr("Expected=" + inputName);
                  TestUtil.logErr("Actual=" + action.input());
                }
              } else {
                TestUtil.logTrace("input attribute was empty");
              }
            }
            if (outputName != null) {
              if (!action.output().equals("")) {
                if (!outputName.equals(action.output())) {
                  result = false;
                  TestUtil.logErr("Error with output attribute:");
                  TestUtil.logErr("Expected=" + outputName);
                  TestUtil.logErr("Actual=" + action.output());
                }
              } else {
                TestUtil.logTrace("output attribute was empty");
              }
            }
            if (faultArray.length != 0) {
              if (!verifyFaultActionAnnotation(action.fault(), faultArray)) {
                result = false;
              }
            }
          } else {
            result = false;
            TestUtil.logErr("Action annotation returned was null");
          }
        } else {
          result = false;
          TestUtil.logErr(
              "Action annotation should not exist but was found for method:"
                  + methodName);
        }
      } else {
        if (shouldExist) {
          result = false;
          TestUtil.logErr(
              "Action annotation should exist but was not found for method:"
                  + methodName);
        } else {
          TestUtil.logTrace(
              "Action annotation should not exist and was not found for method:"
                  + methodName);
        }
      }
    }
    return result;
  }

  private static boolean verifyFaultActionAnnotation(FaultAction[] faArray,
      String[][] faultArray) {
    boolean result = true;
    if (faArray != null) {
      if (faArray.length != 0) {
        if (faArray.length == (faultArray.length)) {
          for (int i = 0; i < faArray.length; i++) {
            FaultAction fAction = faArray[i];
            TestUtil.logTrace("Annotation:");
            TestUtil.logTrace(fAction.toString());
            if (fAction.className() != null) {
              String className = fAction.className().getName();
              TestUtil.logTrace("className attribute=" + className);
              boolean found = false;
              for (int j = 0; j < faultArray.length; j++) {
                if (faultArray[j][0].equals(className)) {
                  found = true;
                  TestUtil.logTrace("className found:" + className);
                  if (!fAction.value().equals("")) {
                    if (!faultArray[j][1].equals(fAction.value())) {
                      result = false;
                      TestUtil.logErr("Error with value attribute:");
                      TestUtil.logErr("Expected=" + faultArray[j][1]);
                      TestUtil.logErr("Actual=" + fAction.value());
                    } else {
                      TestUtil.logTrace("value passed");
                    }
                  } else {
                    TestUtil.logTrace("value attribute was empty");
                  }
                }
              }
              if (!found) {
                result = false;
                TestUtil.logErr("The className:" + className
                    + " was not found in the list of expected values");
              }
            } else {
              TestUtil.logTrace("className attribute was null");
            }
          }
        } else {
          result = false;
          TestUtil.logErr("Error with number of FaultAction annotations:");
          TestUtil.logErr("Expected=" + faultArray.length);
          TestUtil.logErr("Actual=" + faArray.length);
        }
      } else {
        result = false;
        TestUtil.logErr("Error with number of FaultAction annotations:");
        TestUtil.logErr("Expected=" + faultArray.length);
        TestUtil.logErr("Actual=" + faArray.length);
      }
    } else {
      result = false;
      TestUtil.logErr("FaultAction was null");
    }
    return result;
  }

}
