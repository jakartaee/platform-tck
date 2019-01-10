#!/bin/bash
#
# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
#

docker build --build-arg http_proxy=http://<host>:<port> \
--build-arg https_proxy=https://<host>:<port> \
--build-arg JDK_BUNDLE_URL=dummy --build-arg CTS_BUNDLE_URL=dummy \
--build-arg GLASSFISH_BUNDLE_URL=dummy --build-arg JWSDP_BUNDLE=dummy \
--build-arg JDK_FOR_JWSDP=dummy --build-arg test_suite=samples \
--tag javaeects --rm .
