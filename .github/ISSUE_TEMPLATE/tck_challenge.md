---
name: Platform TCK Challenge
about: Create TCK challenges in the appropriate SPEC project 
title: 'Challenges for tests written specifically for Platform SPEC requirements belong here'
labels: 'challenge'
assignees: ''

---

**Follow the TCK Process document**
Please follow the instructions in the TCK Process document (https://jakarta.ee/committees/specification/tckprocess) which may be updated occasionally.

**Filing a Challenge**

TCK Challenges MUST be filed via the specification project’s issue tracker using the label challenge and include the following information:

1. The relevant specification version and section number(s).
2. The coordinates of the challenged test(s).
3. The exact TCK version.
4. The implementation being tested, including name and company.
5. A full description of why the test is invalid and what the correct behavior is believed to be.
6. Any supporting material; debug logs, test output (.jtr file), test logs, run scripts, etc.

For an example of an existing TCK challenge, see https://github.com/eclipse-ee4j/servlet-api/issues/378

**Additional context**
Add any other context about the problem here.

** Note that the source for this page is contained in https://github.com/eclipse-ee4j/jakartaee-tck/tree/master/.github/ISSUE_TEMPLATE, pull requests are welcome to improve!  
I'm also sharing this link so other projects see how they can specify an ISSUE_TEMPLATE for new issues. :-)
