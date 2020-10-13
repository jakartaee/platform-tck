#!/bin/sh +x
#
# Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

ECHO=`which echo`
STARTING_PWD=`pwd`

if [ ! -d "${TS_HOME}" ]; then
    echo "YOU MUST SET TS_HOME in your environment"
    exit 1
fi
if [ ! -d "${CTS_TOOLS}" ]; then
    echo "YOU MUST SET CTS_TOOLS in your environment"
    exit 1
fi

PREVIOUS_CONCURRENCYVERSION=1.0
TMPPREVIOUS_CONCURRENCYVERSION=${PREVIOUS_CONCURRENCYVERSION}
CURRENT_CONCURRENCYVERSION=1.0
TMPCURRENT_CONCURRENCYVERSION=${CURRENT_CONCURRENCYVERSION}

SRCDIR=/work/concurency/sources

TMPSRCDIR=${SRCDIR}
WORKDIR=/work/concurency/assertions/javadoc/
TMPWORKDIR=${WORKDIR}

display_values(){
     ${ECHO} "Previous Concurrency version=[${TMPPREVIOUS_CONCURRENCYVERSION}]"
     ${ECHO} "Current Concurrency version=[${TMPCURRENT_CONCURRENCYVERSION}]"
     ${ECHO} "Source Dir=[${TMPSRCDIR}]"
     ${ECHO} "Work Dir=[${TMPWORKDIR}]"
}

while (true)
do
    
    display_values
    ${ECHO} "Are the values correct (y/n): [n]"
    read input
    case "${input}" in
    y|Y)
      PREVIOUS_CONCURRENCYVERSION=${TMPPREVIOUS_CONCURRENCYVERSION}
      CURRENT_CONCURRENCYVERSION=${TMPCURRENT_CONCURRENCYVERSION}
      SRCDIR=${TMPSRCDIR}
      WORKDIR=${TMPWORKDIR}
      break
      ;;
    n|N)
      ${ECHO} "Enter the correct data(<cr> to keep existing value):"
      ${ECHO} "Previous Concurrency version=[${TMPPREVIOUS_CONCURRENCYVERSION}]"
      read input
      if [ ! -z "${input}" ]; then
         TMPPREVIOUS_CONCURRENCYVERSION=${input}
      fi
      ${ECHO} "Enter the correct data(<cr> to keep existing value):"
      ${ECHO} "Current Concurrency version=[${TMPCURRENT_CONCURRENCYVERSION}]"
      read input
      if [ ! -z "${input}" ]; then
         TMPCURRENT_CONCURRENCYVERSION=${input}
      fi
      ${ECHO} "Source Dir=[${TMPSRCDIR}]"
      read input
      if [ ! -z "${input}" ]; then
         TMPSRCDIR=${input}
      fi
      ${ECHO} "Work Dir=[${TMPWORKDIR}]"
      read input
      if [ ! -z "${input}" ]; then
         TMPWORKDIR=${input}
      fi
      ;;
    esac
done

PREVIOUS_CONCURRENCYVERSION_UNDERSCORE=`echo ${TMPPREVIOUS_CONCURRENCYVERSION} | tr '.' '_'`


CHOICE=0
while (true)
do
   WORKSPACE_TYPE=""
   ${ECHO} ""
   ${ECHO} "Choose one of the following type"
   ${ECHO} "1. Generate new javadoc assertions from source"
   ${ECHO} "------------------"
   ${ECHO} "If not merging, skip to step 44"
   ${ECHO} "------------------"
   ${ECHO} "2. Merge new assertions with existing ones"
   ${ECHO} "------------------"
   ${ECHO} "  reivew document at this point "
   ${ECHO} "------------------"
   ${ECHO} "3. Create sorted merged assertions file"
   ${ECHO} "4. Copy merged assertions file to final xml file and generate html file"
   ${ECHO} "44. Copy new assertions xml and html file to final destination"

   ${ECHO} "8. Generate html file from final xml file"
   ${ECHO} "9. exit"
   ${ECHO} ""
   ${ECHO} -n " Please enter [1-9]:"
   read input
   case "${input}" in
        1)
            #generate the assertions from the source. 
            # grab all the assertions including the inherited ones
            rm -rf $WORKDIR
            mkdir -p $WORKDIR
            cd $CTS_TOOLS/tools/scripts
            chmod +x *.sh
            ant build
             assert-gen-ctor-and-comment-exception-concurrency 'CONCURRENCY' "CONCURRENCY ${CURRENT_CONCURRENCYVERSION}" "Java API for Concurrency" "${CURRENT_CONCURRENCYVERSION}" \
             ${SRCDIR} \
             ${WORKDIR} \
             jakarta.enterprise.concurrent

            cd $STARTING_PWD
          ;;
        2)
          # merge the old assertion doc with the new assertion doc.
          # After the merge, this new doc must be spot checked by hand.
          #   Review the modified section for changes to existing assertions. If the modified assertion is chosen, the <modified/>
          #    tag must be removed from each assertion that is reviewed.
          cd $CTS_TOOLS/tools/assert-merge

          ant -v run -Dverfile=${TS_HOME}/internal/docs/concurrency/ConcurrencyJavadocAssertions.xml \
                  -Dnewfile=${WORKDIR}/assertions-nums.xml -Doutfile=${WORKDIR}/merged.assertions.xml
          echo "Merged file=${WORKDIR}/merged.assertions.xml"
          cd $STARTING_PWD
          ;;
        3)
          cd $CTS_TOOLS/tools/xsl-transformer/scripts
          chmod +x run
          cp ${WORKDIR}/merged.assertions.xml ${WORKDIR}/merged.assertions.nonsorted.xml
          ./run ${WORKDIR}/merged.assertions.nonsorted.xml $CTS_TOOLS/docs/xsl/assertions/sort-javadoc-assertion-ids.xsl ${WORKDIR}/merged.assertions.sorted.xml
          ;;
        4)
          cp ${WORKDIR}/merged.assertions.sorted.xml ${TS_HOME}/internal/docs/concurrency/ConcurrencyJavadocAssertions.xml
          cd $STARTING_PWD
          ant create.javadoc.html.file
          ;;
        44)
          cp ${WORKDIR}/assertions-nums.xml ${TS_HOME}/internal/docs/concurrency/ConcurrencyJavadocAssertions.xml
          cp ${WORKDIR}/assertions.html ${TS_HOME}/internal/docs/concurrency/ConcurrencyJavadocAssertions.html
          ;;
        8)
          ant create.javadoc.html.file
          ;;
        9)
          exit 0
          ;;
    esac
done

