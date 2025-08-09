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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;

public class RemoveClassesArchiveProcessor implements ProtocolArchiveProcessor {

    public void process(TestDeployment td, Archive<?> archive) {
        final Node toDelete = archive.get("/WEB-INF/classes/ee");
        if (toDelete != null) {
            deleteChildren(toDelete, archive);
        }
    }

    private void deleteChildren(Node node, Archive<?> archive) {
        final Set<Node> children = node.getChildren();
        if (!children.isEmpty()) {
            List<Node> nodesToDelete = new ArrayList<>(children);
            nodesToDelete.forEach(childNode -> {
                deleteChildren(childNode, archive);
            });
        }
        archive.delete(node.getPath());
    }

}
