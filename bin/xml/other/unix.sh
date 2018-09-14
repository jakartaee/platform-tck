#!/bin/sh
#
# Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

for arg in "$@" ; do
   case $arg in
      startxterm)
          newdir="$2"
          if [ ! -d $newdir ]
          then
              exit -1
          fi
          cd $2
          if [ $? -ne 0 ]
          then
              exit -1
          fi
          if [ "$TERM" = "" ]
          then
               TERM=xterm
          fi
          echo $newdir
          $TERM &
          exit 0
          break
          ;;
   esac
done
exit 0
