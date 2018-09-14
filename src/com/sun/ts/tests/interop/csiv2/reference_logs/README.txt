Overview:
1.  Content
2.  File Names
3.  How to use/create these


------------
1.  Content:
------------
These are sample log files taken from a successful run of the CSIV2
tests.  These were generated from CTS7 using JDK7 against Glassfish 4.0.
These logs are useful aids when trying to troubleshoot CSIV2 failures.


---------------
2.  File Names:
---------------
These files each map to a specific CSIV2 test.  The name of each file
indicates which test it came from.  The format of each name breaks down
as follows:
   serverInterceptorLog.txt.<top_level_dir>.<testname>_<forward_or_reverse>
   
   Where:
     top_level_dir:  This is the directory under src/com/sun/ts/tests/interop/csiv2
                     such as ac_ssl_ssln_upr_noid or ew_ssl_sslr_upn_upid or etc...
     testname:       This is the actual name of the test, usually identified inside
                     the Client.java file by the @testName tag.
     forward_or_reverse : This indicates if a forward or reverse test was being run.
                          There will be NOTHING appended after the testname if the test is a
                          forward test  (e.g. serverInterceptorLog.txt.ac_ssl_ssln_upr_noid_eb_testid2)
                          There will be "reverse" appended if the test is a reverse
                          test (e.g. serverInterceptorLog.txt.ac_ssl_ssln_upr_noid_eb_testid2_reverse)


----------------------------
3.  How to use/create these:
----------------------------
These files get automatically created when you enable the "harness.log.traceflag" property 
in the ts.jte file.  They get placed into the directory identified by the "log.file.location"
property in the ts.jte file.

The files contain logging information that typically gets sent back to the client side for 
test verification.  What this means is if you have a CSIV2 test failing, you can capture the 
log file file for your failed test and compare it to the corresponding log in the reference_logs
directory to see what the differences are.  The differences will give a good indication of
why the test failed.  A successfully run test may not look identical to the corresponding
log in the reference_logs directory but it should look close and the main IOR values and
tags should match.  

You should expect some differences between your csiv2 log file(s) and those in the
reference_logs directory.  Those differences will likely include hostnames, auth-tokens, 
port numbers, and other things that are expected to be specific for your environment.   
Things that should NOT be different should include IOR properties, and things that
are NOT machine specific.

As an example, if you have a failed test log and notice that it is different from the 
corresponding test log in the reference_logs dir because the log from a failed test run 
is missing a "<client-interceptor>" tag, then you can isolate the problem as being
in the interceptor.  Is the interceptor configured and working correctly?  Did
the test die before getting to the interceptor or did it die within the interceptor?
The content of the log files should help isolate and troubleshoot these kinds of issues.. 

For more details on these logs, view the CTS TCK User Guide, Appendix C.

