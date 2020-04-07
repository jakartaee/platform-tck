Content:
--------
   jca_1.5 :  contains assertion info related to JCA 1.5
   jca_1.6 :  contains assertion info related to JCA 1.6
   <current dir> : contains assertion info related to upcoming CTS7/EE7 dev cycle.  some of 
                   the files in here may have same numbers as last (1.6) rev *but* these 
                   should be considered the current/live assertion docs that could change
                   for CTS7 (there is a Connector MR due out then so we expect SOME changes)



To generage api assertions invoke the following:
-----------------------------------------------
A.  this generates a little less of an API assertion doc as it does not 
    include all constructor (as well as other) class hierarchical stuff.
 > cd $TS_TOOLS_HOME/tools/scripts
 > ./assert-gen.sh  "JSR-322"   "JSR-322" "J2EE Connector Architeture Spec"  "1.6" \
        /export/home/files2/src/glassfish/connector-api/src/java \
        /export/home/files2/projects/svn-spider/trunk/internal/docs/connector/javadoc_asserts \
        jakarta.resource \
        jakarta.resource.cci \
        jakarta.resource.spi \
        jakarta.resource.spi.endpoint \
        jakarta.resource.spi.security \
        jakarta.resource.spi.work

B.  this generates a fairly significant amount of extra API Assertions
 > cd $TS_TOOLS_HOME/tools/scripts
 > ./assert-gen-inherited-ctor-and-comment-exception.sh Connector "Connector 1.6" \
	"J2EE Connector Architeture Spec" "1.6" \
	/files2/projects/gfv4.0/appserver/connectors/jakarta.resource/src/main/java \
	/Users/phendley/eclipse/workspace/spider-trunk/internal/docs/connector/javadoc_asserts \
	jakarta.resource \
	jakarta.resource.cci \
	jakarta.resource.spi \
	jakarta.resource.spi.endpoint \
	jakarta.resource.spi.security \
	jakarta.resource.spi.work

hints:
  1.  do top level bringover of svn-tools directory!!!  (this always catches me)
  2.  cd svn-tools/tools/xsl-transformer
      run "ant build"  (or maybe its just "ant") and it should build a "run" script that
      accompanyoies the run.sh script located in TS_TOOLS_HOME/tools/xsl-transformer/scripts
  3.  may be necessary to a toplevel build of tools workspace by doing following:
       > cd $TS_TOOLS_HOME
       > ant


To generage spec assertions:
----------------------------
After this, generate spec assertions by doing the following:
  cd $TS_HOME/install/j2ee/bin
  ant -f $TS_HOME/bin/coverage-build.xml connector



Connector 1.7 Update:
---------------------
For Connector 1.6, we maintained two sets of documents: (1) for standalone connector 
and (1) for Connector that lives in EE.  The connector that lived in the EE had more
tests in it (for testing EJB's and MDB stuff).   In JCA 1.7, we are no longer going
to maintain two sets of different docs.  This was turning out to be a maintenance 
nightmare so from now on the one set of connector assertion docs will be applied
to both standalone connector as well as full Profile Connector tests.

