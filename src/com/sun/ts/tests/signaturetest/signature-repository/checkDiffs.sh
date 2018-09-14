#!/usr/bin/ksh
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

###############################################################################
# This helper script is used to check which files in a directory have been
# changed.  This will print out the name of the file that change and then the
# diffs of that file.  It will do this for ALL files in a dir.  This is intended
# to be run from within 
# TS_HOME/src/com/sun/ts/tests/signaturetest/signature-repository
#
# This is particularly useful with sigtests for those times when you edit a 
# BUNCH of files, then regenerated a bunch of sigtests, then you want to see
# if those newly generated sigtests have changed since their last recorded state.
# and quickly scan what those sigtest changes are.
#
#  ASSUMPTIONS:
#  1.  use of svn as source control tool
#  2.  you have  a bunch of files checked out in the same directory this script
#      is running from 
#  3.  the checked out files are sig test files within:
#      TS_HOME/src/com/sun/ts/tests/signaturetest/signature-repository
#  4.  you want to see if the newly generated sigtests (ie the 'checked out' ones)
#      are different from the previously saved files.
#
#
#  EXAMPLES:
#  1.  ./checkDiffs.sh
#      (this dumps out a list of diffs)
#
#  2.  ./checkDiffs.sh -unedit
#      (this dumps out diffs and if any file did NOT change, it is unedited.)
#      NOTE:  this was useful when using sccs but not so useful for svn
#
#  3.  ./checkDiffs.sh -showfiles
#      (this dumps out just a list of files that changed)
#      NOTE:  this was useful when using sccs but not so useful for svn
#             as 'svn status' does the same thing
#
###############################################################################


ARG1=$1
UNEDIT="false"

if [ "$ARG1" = "-unedit" ]; then
    UNEDIT="true"
fi


# get list of files that being edited.  this is the list we will check.
sList=`/usr/bin/svn status | awk '{print $2}' | grep sig`

if [ "$ARG1" = "-showfiles" ]; then
    for file in $sList ; do
        echo "$file"
    done
    exit 0    
fi


for file in $sList ; do
    echo ""
    echo "-----------------------------------------"    
    echo "searching file: $file"
    LINECOUNT=`/usr/bin/svn diff $file |/usr/bin/grep -v $file | /usr/bin/wc -l`
    if [ $LINECOUNT == "1" ] ; then
        echo "No diffs"
        if [ "$UNEDIT" = "true" ]; then
            echo "unediting $file"
            `/usr/bin/svn revert $file`
        fi
    else 
        OUTPUT=`/usr/bin/svn diff $file`
        echo "$OUTPUT"
    fi
    echo "-----------------------------------------"    
    echo ""
done

exit 0
