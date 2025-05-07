/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.glassfish.persistence.tck;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;

/**
 * We need to remove the test class and related classes added by Arquillian automatically. They were causing issues with Shrinkwrap used in the TCK tests. Those classes are already in a JAR in WEB-INF/lib.
 * @author Ondro Mihalyi
 */
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
