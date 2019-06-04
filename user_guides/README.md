= TCK User Guides

This directory contains all the TCK User Guides.

All User Guides except for the "javaee" (platform and Web Profile)
User Guide are derived from a common template in the Template
directory.  After making changes to the template, the
apply_template.sh script can be used to apply the changes to
all the derived TCK User Guides.  This is a bit of a kludge
and really should be done at build time using Maven; that's a
project for the future.

Note that when using the template for a particular specification's
TCK User Guide, none of the *.adoc files should be modified.
See the README file in Template/src/main/jbake/content.
