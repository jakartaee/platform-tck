/*
 * Copyright (c) "2022" Red Hat and others
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package maven.resolver;

public class Utils {
    static String[] getListOfActiveMavenProfiles() {
        String activeManveProfilesRawValue = System.getProperty(PropertyKeys.ACTIVE_MAVEN_PROFILES);
        if (activeManveProfilesRawValue != null) {
            return activeManveProfilesRawValue.split("\\s*,\\s*");
        } else {
            return new String[] {};
        }
    }
}
