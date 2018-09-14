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

package com.sun.ts.lib.deliverable.cts.deploy;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.enterprise.deploy.spi.TargetModuleID;
import java.util.List;
import java.util.ArrayList;
import com.sun.ts.lib.util.TestUtil;

public class OperationStatus {
  private boolean failed;

  private String progressMessage;

  private Target[] failedTargets;

  private TargetModuleID[] deployedTargetIDs;

  private ProgressObject progress;

  public OperationStatus(ProgressObject progress, Target[] failedTargets) {
    this(progress, failedTargets, progress.getDeploymentStatus().isFailed());
  }

  public OperationStatus(ProgressObject progress, Target[] failedTargets,
      boolean failed) {
    this.progress = progress;
    this.failed = failed;
    this.progressMessage = progress.getDeploymentStatus().getMessage();
    this.failedTargets = failedTargets;
    this.deployedTargetIDs = filterNonRootTargetModuleIDs(
        progress.getResultTargetModuleIDs());
  }

  public boolean isFailed() {
    return failed;
  }

  public String getProgressMessage() {
    return progressMessage;
  }

  public Target[] getFailedTargets() {
    return findErrors();
  }

  public TargetModuleID[] getDeployedTargetIDs() {
    return deployedTargetIDs;
  }

  public ProgressObject getProgressObject() {
    return progress;
  }

  /*
   * Filter out the non-root target module IDs from the specified list. The JSR
   * 88 spec is ambiguous as to what the ProgressObject.getResultTargetModuleIDs
   * method returns. The list of target module IDs may be root target module IDs
   * (those corresponding to an application or standalone module) only or root
   * plus non-root target module IDs (those corresponding to modules that are
   * part of a larger application). Since the harness is only interested in
   * stopping, starting and undeploying applications or standalone components we
   * filter out the non-root target module IDs since we don't do anything with
   * them anyway. We also need to filter the non-root target module IDs because
   * the spec is ambiguous as to what will happen if we pass a list of target
   * module IDs that contain non-root IDs to the start, stop and undeploy
   * methods (in the JSR 88 API). The spec does not state if these methods
   * should throw an error, ignore the non-root target module IDs or take some
   * other action. Since this is implementation specific we can not assume
   * anything except for the fact that the start, stop and undeploy methods
   * should have no affect on non-root modules since they can NOT be started,
   * stopped or undeployed according to the spec. See section 4.1 in the JSR 88
   * spec for details.
   */
  private TargetModuleID[] filterNonRootTargetModuleIDs(TargetModuleID[] ids) {
    List result = new ArrayList();
    for (int i = 0; i < ids.length; i++) {
      TargetModuleID id = ids[i];
      if (id.getParentTargetModuleID() == null) {
        result.add(id);
      }
    }
    return (TargetModuleID[]) (result
        .toArray(new TargetModuleID[result.size()]));
  }

  private boolean targetInList(Target target, TargetModuleID[] list) {
    boolean result = false;
    for (int i = 0; i < list.length; i++) {
      Target t = list[i].getTarget();
      if (t.getName().equals(target.getName())) {
        result = true;
        break;
      }
    }
    return result;
  }

  private Target[] findErrors() {
    List failed = new ArrayList();
    for (int i = 0; i < failedTargets.length; i++) {
      if (!targetInList(failedTargets[i], deployedTargetIDs)) {
        failed.add(failedTargets[i]);
      }
    }
    return (Target[]) failed.toArray(new Target[failed.size()]);
  }

  public String errMessage() {
    StringBuffer buf = new StringBuffer("Failed targets: ");
    int numFailures = (failedTargets == null) ? 0 : failedTargets.length;
    for (int i = 0; i < numFailures; i++) {
      buf.append(failedTargets[i].getName() + "  ");
    }

    buf.append(TestUtil.NEW_LINE + "DeploymentStatus...");
    buf.append(TestUtil.NEW_LINE + "Message             = "
        + progress.getDeploymentStatus().getMessage());
    buf.append(TestUtil.NEW_LINE + "State               = "
        + progress.getDeploymentStatus().getState());
    buf.append(TestUtil.NEW_LINE + "Command             = "
        + progress.getDeploymentStatus().getCommand());
    buf.append(TestUtil.NEW_LINE + "Action              = "
        + progress.getDeploymentStatus().getAction());
    buf.append(TestUtil.NEW_LINE + "Additional info     = "
        + progress.getDeploymentStatus().toString() + TestUtil.NEW_LINE);

    return buf.toString();
  }

}
