<!--

    Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:import href="xfiles/resolve.xsl"/>
  <xsl:output method="text"/>
  <xsl:template match="/">
    <xsl:for-each select="//node()|//@*">
      <xsl:variable name="node-type">
        <xsl:call-template name="node-type"/>
      </xsl:variable>
      Node is of type: <xsl:value-of select="$node-type"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
