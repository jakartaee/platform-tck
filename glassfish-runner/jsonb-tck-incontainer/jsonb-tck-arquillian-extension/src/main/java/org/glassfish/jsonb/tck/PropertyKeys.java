/*
 * Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.glassfish.jsonb.tck;

public interface PropertyKeys {

    final String SYSTEM_PROPERTIES_FILE_NAME = "test.properties";

    final String PLATFORM_MODE = "platform.mode";

    final String PLATFORM_MODE_JAKARTAEE = "jakartaEE";

    final String ACTIVE_MAVEN_PROFILES = ArquillianExtension.PROPERTY_PREFIX + "activeMavenProfiles";

    final String IN_CONTAINER = ArquillianExtension.PROPERTY_PREFIX + "inContainer";

    // Artifacts with a group matching one of these prefixes will not be added to the package.
    // Expects a comma-separated list of prefixes.
    final String GROUP_PREFIXES_TO_IGNORE = ArquillianExtension.PROPERTY_PREFIX + "coordinatePrefixesToIgnore";

    // package type, e.g. EAR, EJBJAR, WAR. Default is WAR
    final String PACKAGE = ArquillianExtension.PROPERTY_PREFIX + "packageType";

    final String DEFAULT_PERSISTENCE_UNIT = "persistence.unit.name";

    final String JUNIT_AUTO_EXTENSIONS_ENABLED = "junit.jupiter.extensions.autodetection.enabled";
}
