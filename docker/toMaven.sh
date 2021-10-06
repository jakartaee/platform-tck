#!/bin/bash -xe

# Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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


#git mv common/ src/test/java/com/sun/ts/tests/concurrency
#git mv api src/test/java/com/sun/ts/tests/concurrency
#git mv spec src/test/java/com/sun/ts/tests/concurrency

result="${PWD##*/}"
echo $result
if [[ "$result" != "jakartaee-tck" ]]; then
  echo "$(basename pwd) is wrong, you must be in in root of Platform TCK"
  exit 1
fi   

modules=(appclient assembly common concurrency connector ejb ejb30 ejb32 el integration internal jacc jaspic javaee javamail jaxrs jaxws jdbc jms jpa jsf jsonb jsonp jsp jstl jta jws saaj samples securityapi servlet signaturetest webservices12 webservices13 websocket xa)

for module in ${modules[*]}; do
  echo "=== Module $module ==="
  targetfolder="$PWD/$module/src/test/java"
  rm -rf $targetfolder
  mkdir -p $targetfolder
  pushd src/com/sun/ts/tests
  pwd
  git mv $module "$targetfolder"
  popd
done

# move web resources
targetfolder="$PWD/webartifacts"
rm -rf $targetfolder
mkdir -p "$targetfolder/src/test/resource"
pushd src/web
git mv * "$targetfolder"
popd


pushd .
targetfolder="$PWD/jbatch/src/test/java"
rm -rf $targetfolder
mkdir -p $targetfolder
cd src/com/ibm/jbatch
git mv * $targetfolder
popd

modules=(deliverable harness implementation javatest porting tests util)
for module in ${modules[*]}; do
  targetfolder="$PWD/runtime/$module"
  rm -rf $targetfolder
  echo "=== Module $module === targetfolder= $targetfolder"
  mkdir -p "$targetfolder/src/test/java/com/sun/ts/lib"
  pushd src/com/sun/ts/lib
  git mv $module "$targetfolder/src/test/java/com/sun/ts/lib"
  popd
done  

# fixup packages
modules=(appclient assembly common concurrency connector ejb ejb30 ejb32 el integration internal jacc jaspic javaee javamail jaxrs jaxws jdbc jms jpa jsf jsonb jsonp jsp jstl jta jws saaj samples securityapi servlet signaturetest webservices12 webservices13 websocket xa)

for module in ${modules[*]}; do
  rm -rf $module/src/test/java/com/sun/ts/tests/
  mkdir -p $module/src/test/java/com/sun/ts/tests/
  pushd    $module/src/test/java/
  pwd
  ls
  git mv -f "$module" "com/sun/ts/tests/"
  popd
done
