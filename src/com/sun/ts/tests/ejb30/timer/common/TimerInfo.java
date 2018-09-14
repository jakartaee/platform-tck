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
package com.sun.ts.tests.ejb30.timer.common;

public class TimerInfo implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  private String testName;

  private String reason;

  private Boolean status = false;

  private Integer testNumber = 0;

  private String stringVar;

  private String stringVar2;

  private Double doubleVar = Double.valueOf(0);

  private Long longVar = Long.valueOf(0);

  private Character charVar = Character.valueOf(' ');

  private Boolean booleanVar = false;

  private Integer intVar = 0;

  public TimerInfo() {
  }

  public TimerInfo(String testName) {
    this.testName = testName;
  }

  public Boolean getBooleanVar() {
    return booleanVar;
  }

  public void setBooleanVar(Boolean booleanVar) {
    this.booleanVar = booleanVar;
  }

  public Character getCharVar() {
    return charVar;
  }

  public void setCharVar(char charVar) {
    this.charVar = charVar;
  }

  public Double getDoubleVar() {
    return doubleVar;
  }

  public void setDoubleVar(double doubleVar) {
    this.doubleVar = doubleVar;
  }

  public Integer getIntVar() {
    return intVar;
  }

  public void setIntVar(Integer intVar) {
    this.intVar = intVar;
  }

  public Long getLongVar() {
    return longVar;
  }

  public void setLongVar(Long longVar) {
    this.longVar = longVar;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getStringVar() {
    return stringVar;
  }

  public void setStringVar(String stringVar) {
    this.stringVar = stringVar;
  }

  public String getStringVar2() {
    return stringVar2;
  }

  public void setStringVar2(String stringVar2) {
    this.stringVar2 = stringVar2;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public Integer getTestNumber() {
    return testNumber;
  }

  public void setTestNumber(Integer testNumber) {
    this.testNumber = testNumber;
  }

  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + (booleanVar ? 1231 : 1237);
    result = PRIME * result + charVar;
    long temp;
    temp = Double.doubleToLongBits(doubleVar);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    result = PRIME * result + intVar;
    result = PRIME * result + (int) (longVar ^ (longVar >>> 32));
    result = PRIME * result + ((reason == null) ? 0 : reason.hashCode());
    result = PRIME * result + (status ? 1231 : 1237);
    result = PRIME * result + ((stringVar == null) ? 0 : stringVar.hashCode());
    result = PRIME * result
        + ((stringVar2 == null) ? 0 : stringVar2.hashCode());
    result = PRIME * result + testNumber;
    result = PRIME * result + ((testName == null) ? 0 : testName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TimerInfo other = (TimerInfo) obj;
    if (!booleanVar.equals(other.booleanVar)) {
      return false;
    }
    if (!charVar.equals(other.charVar)) {
      return false;
    }
    if (Double.doubleToLongBits(doubleVar) != Double
        .doubleToLongBits(other.doubleVar)) {
      return false;
    }
    if (!intVar.equals(other.intVar)) {
      return false;
    }
    if (!longVar.equals(other.longVar)) {
      return false;
    }
    if (reason == null) {
      if (other.reason != null) {
        return false;
      }
    } else if (!reason.equals(other.reason)) {
      return false;
    }
    if (!status.equals(other.status)) {
      return false;
    }
    if (stringVar == null) {
      if (other.stringVar != null) {
        return false;
      }
    } else if (!stringVar.equals(other.stringVar)) {
      return false;
    }
    if (stringVar2 == null) {
      if (other.stringVar2 != null) {
        return false;
      }
    } else if (!stringVar2.equals(other.stringVar2)) {
      return false;
    }
    if (!testNumber.equals(other.testNumber)) {
      return false;
    }
    if (testName == null) {
      if (other.testName != null) {
        return false;
      }
    } else if (!testName.equals(other.testName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TimerInfo");
    if (testName != null)
      sb.append(" testName=").append(testName);
    if (testNumber != null)
      sb.append(" testNumber=").append(testNumber);
    if (reason != null)
      sb.append(" reason=").append(reason);
    if (status != null)
      sb.append(" status=").append(status);
    if (stringVar != null)
      sb.append(" stringVar=").append(stringVar);
    if (stringVar2 != null)
      sb.append(" stringVar2=").append(stringVar2);
    if (doubleVar != null)
      sb.append(" doubleVar=").append(doubleVar);
    if (longVar != null)
      sb.append(" longVar=").append(longVar);
    if (charVar != null)
      sb.append(" charVar=").append(charVar);
    if (booleanVar != null)
      sb.append(" booleanVar=").append(booleanVar);
    if (intVar != null)
      sb.append(" intVar=").append(intVar);

    return sb.toString();
  }
}
