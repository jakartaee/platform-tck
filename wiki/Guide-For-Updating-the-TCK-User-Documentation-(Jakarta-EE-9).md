
This is a rundown of the changes I've found necessary to revise the TCK
user documentation to Jakarta EE 9. Interested writers are welcome to
tackle these incrementally -- for example, just updating the user guide,
or just update portions at a time.

User Guide File specific recommendations
----------------------------------------

### Orientation

Source for the user guide is in jakartaee-tck/user\_guides/\<your-api\>

The user guide documentation source is in:
jakartaee-tck/user\_guides/\<your-api\>/srv/main/jbake/content

There are a few other files you'll need to look at scattered about. I've
listed some those that I've found in need of edits. You are welcome to
ask around if you have questions. You may also want to look at the
AsciiDoc user manual for information about formatting, etc.

You will need maven and git to accomplish this work though you won't
need to know more than the most basic maven command: mvn clean install.
I won't go into installing these, here. There are plenty of nice
instruction sheets available -- just use your favorite Search tool and
I'm sure you'll find competent advice.

Other than for the most simple changes, I would not recommend trying
this work through the Git browser interface.

If you don't know basic GIT usage, I would recommend you first run
through some on-line training for GIT. GitHub has some excellent
resources. Again, your favorite search tool will quickly point you to
competent, basic advice.

A workflow that I've found works for me is: Create a GH Fork under your
Git User ID, Clone that to your local machine. Create a local branch and
switch to it, make your changes in that branch. Commit and Push your
changes back to your Github Fork, then submit a Pull Request from the
branch in your GitHub fork through the GitHub browser UI. Once the PR
has been accepted, you can remove the branch from your fork. This is one
flow. There are lots of ways to accomplish this work.

One element I've found challenging is keeping my Fork and desktop Clone
synchronized with other changes that are happening in the repository.
You can refer to various instruction sheets about Git Remote and Git
Upstream synchronization to keep your repository up to date. 

It can become a bit confusing to new writers (and still is, to me) when
you encounter merge conflicts. GIT will help you through all of this,
but you need to be careful when you are merging your work over the top
of someone else's. The good news is, for the most part, that doesn't
happen much for this work.

Following are some specific things to look for in the user guide files.
The documentation is split into .adoc files and .inc files. The intent
was that most of the constant boiler plate material is in the .inc files
and you don't need to edit the .adoc files, except for the title page.

### jakartaee-tck/user\_guides/\<your-api\>/pom.xml

Check the following tags:

_Name_

-   Correct title, Remove old acronyms, update version ID, etc.

_Version_

_Status_ (Property should be blank for final revision)

_DistributionManagement/site/url:_
`scm:git:git@github.com:eclipse-ee4j/jakartaee-tck.git`
(I don't think this is actually used)

Since this is an xml file, be very careful when you make edits to this
content. Always run mvn clean install afterword to make sure your local
build still works.

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/templates/footer.ftl

-   Check copyright footer -- in most cases, after copyright date, add:
    ',&nbsp;2020&nbsp;' (note leading comma, if there's already a second
    date, replace it if it is not 2020).

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/assets/\_config.yml

Generally, I don't think this file is used. However for completeness:

-   Check description to the current title and version
-   Check source:
    [https://github.com/eclipse-ee4j/jakartaee-tck](https://github.com/eclipse-ee4j/jakartaee-tck)\
-   Check download: recommend
    [https://jakarta.ee/specifications/](https://jakarta.ee/specifications/)\<API\>/\<Version\>
-   Same for docs (I think you can safely add /apidocs to get to
    javadocs)

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/attributes.conf

Update properties to match API, Specification, Etc.

TechnologyRI -- specificy project/product name and version (i.e. Eclipse
GlassFish 6.0)

Look for anything "wombat" that is boilerplate text and should be
replaced. In particular:

-   SpecificationInquiryList -- should be the -dev list of the
    Specification API project

Check TCKPackageName -- there should be no date text in the file-name.
See
[here](https://download.eclipse.org/ee4j/jakartaee-tck/jakartaee9/nightly/)
for a list of all TCK archives produced by this project.

### .inc files for user guide

Scan through each .inc file and check for text that should be updated.
In general, look for:

-   Reference Implementation, RI and replace with Compatible
    Implementation or CI (best to spell out Compatible Implementation
    (CI) the first time it's used in each file.
-   javax -\> jakarta -- in most cases. There are examples where this
    should not change. Work with the API committer team if there are any
    doubts.
-   Many setup documents refer to legacy versions of Java and/or Java
    EE, or Java SE. You need to read them carefully to determine what
    needs to be changed. In general -- all enterprise Java references
    should be to Jakarta EE 9; for this release the TCKs are only
    verified on Java SE 8.
-   In many cases, text may be improved by using property names (i.e.
    refer to {TechnologyRI} instead of writing out Eclipse GlassFish 6;
    or use Java SE {SEversion} instead of Java SE 8.

Note, when you make substitutions, for text in headings, that are
followed by a line with a string of heading control characters ('=',
'\~',  you'll know them when you see them), be sure that the number of
these control characters matches the number of characters in the line
preceding. If you see headings that don't look right and are followed by
a line of this kind of character, it's probably because you replaced
some text and the line-lengths don't match any more.

In general, I recommend you don't change text in any of the link target
strings (these will generally look like a bunch of words concatenated by
hyphens, enclosed in double square brackets '[[]]'). You are, of course
welcome to do this, but check to make sure that any text elsewhere that
points to the link target is also changed.

Additional comments on specific User Guide files
------------------------------------------------

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/title.adoc

Update the Date, the copyright. Check for any other legacy text and
update as needed. (Note to self, these should have been put in the
property list.)

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/intro.adoc

This file contains the legacy platform list. I have been replacing these
with an include directive:

include::platforms.inc[]

Add that file (platforms.inc), with git add

Put the tested platforms in that file. In most cases it will be a single
line:

-   '\* CentOS Linux 7'

Alternatively, just edit this file directly and we can make this change
at another revision.

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/packages.inc

Verify items listed in this file are changed from javax to jakarta.

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/tck-packages.inc

Check that the right version numbers are listed. You can confirm them
from the lib folder
([here](https://github.com/eclipse-ee4j/jakartaee-tck/tree/master/lib)).
If you know that packages are added or removed you may make those edits.
For the most part, just check the versions match what is in the lib
folder. 

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/rebuild.inc

This file is used if the rebuild property in attributes.conf is set. If
so, review and revise the content. This is rendered in the final
document as Appendix B. 

Other things to look for
------------------------

### jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content/faq.adoc

The original has "reference implementation" in one of the questions. If
it hasn't been replaced, replace "reference" with "compatible".

Run the User Guide build
------------------------

You may want to run a build, when you first set up your local
repository. Certainly, when you are all done, or, whenever you want to
look at what you have done. This should be easy. Use the mvn command to
build the user guide.

cd to jakartaee-tck/user\_guides/\<your-api\>

type: mvn clean install

You will see some warnings. As long as the end result is "BUILD SUCCESS"
you can look at the newly rendered content.

### Review the generated content

The final html render (.html files) is in:
jakartaee-tck/user\_guides/\<your-api\>/target/staging. The first file
is toc.html. The PDF copy (.pdf file) is in:
jakartaee-tck/\<your-api\>/target/generated-docs/

If you find content you would like to fix but aren't sure where it came
from -- use file-name.html (from the browser) as the clue to find the
correct .adoc file; in your local editor, open that same file with from
the .adoc files in
jakartaee-tck/user\_guides/\<your-api\>/src/main/jbake/content. In most
cases, text you need to change is in a .inc file, but not always. The
.adoc files will show you which .inc file is used within the rendered
text body.

I find using the generated pdf file is convenient for doing keyword
searches across the whole document: javax, reference, ri, etc.

Files in other folders (not in User Guide)
-------------------------------------------

### jakartaee-tck/install/\<your-api\>/docs

Other documents are in jakartaee-tck/install/\<your-api\>/docs.
Generally, there will be at least two files: index.html and
something-ReleaseNotes.html.

Edit changes directly into these files with your favorite editor. Check
your changes with your browser.

In general, I just use Git rename to change the Release Notes file to
incorporate the new version number. Don't forget to update the URL links
in index.html (and elsewhere) when you do that.

### Assertions folder (jakartaee-tck/install/\<your-api\>/docs/assertions)

***Advanced writers/developers only***

These files contain very large tables. I think my best advice is to
ignore these. If you feel competent, you can change the javax API
references to jakarta. If you are feeling particularly brave, you can
change the API package names to the new Jakarta API names. You will need
to be careful that your substitution strings don't pattern match in the
wrong columns. Changes should be limited to:

-   Title and Specification Name/version
-   javax-\>jakarta and new jakarta API name changes in the description
    column in both files
-   change javax to jakarta -- in the method field column in the javadoc
    assertion file.

Don't change anything any other column.

In a future release, these will likely be regenerated. There is a script
that can generate these from the javadoc files. The Specification
assertion list is generated by hand.

### Bin folder (jakartaee-tck/install/\<your-api\>/bin)

***Advanced developers/writers only***

If you feel inclined, you may update the text comments of the various
tx.jte files in jakartaee-tck/install/\<your-api\>/bin. In general,
clarify language about the compatible implementation; remove references
to a reference implementation; update the initial compatible
implementation project/product name details.

Look in various txt files for javax, RI, or reference implementation
strings.

Remember, if you change any file content you must update the copyright dates in
the license header at the top.

Do not make changes in other files. If in doubt -- ask, or just leave it
alone. You're always welcome to file an issue in the TCK project to
remind the team to get back to whatever you've found.

I would recommend you work with the initial compatible implementation
team to verify that any changes you have made to anything in the bin
folder do not have an unintended consequence to the first CI.

Some example PRs
----------------

If you want to see the gory details, check out any of these PRs:

-   RESTful Web Services TCK user doc changes
    ([\#416](https://github.com/eclipse-ee4j/jakartaee-tck/pull/416))
-   JAX-WS TCK Doc changes
    ([\#380](https://github.com/eclipse-ee4j/jakartaee-tck/pull/380))
-   Servlet TCK Doc changes
    ([\#389](https://github.com/eclipse-ee4j/jakartaee-tck/pull/389))
-   Authorization 2.0 TCK Doc changes
    ([\#406](https://github.com/eclipse-ee4j/jakartaee-tck/pull/406))

Feel free to comment on these, ask questions, or comment on this
document.
