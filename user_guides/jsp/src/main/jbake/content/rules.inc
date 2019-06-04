*{techID}10* Each Container must make technically accessible all Java SE
Runtime interfaces and functionality, as defined by the Specifications,
to programs running in the Container, except only as specifically
exempted by these Rules.

*{techID}10.1* Containers may impose security constraints, as defined by
the Specifications.

*{techID}11* A web Container must report an error, as defined by the
Specifications, when processing a JSP Page that does not conform to the
Specifications.

*{techID}12* The presence of a Java language comment or Java language
directive in a JSP Page that specifies ”java” as the scripting
language, when processed by a web Container, must not cause the
functional programmatic behavior of that JSP Page to vary from the
functional programmatic behavior of that JSP Page in the absence of
that Java language comment or Java language directive.

*{techID}13* The contents of any fixed template data (defined by the
Specifications) in a JSP Page, when processed by a web Container, must
not affect the functional programmatic behavior of that JSP Page,
except as defined by the Specifications.

*{techID}14* The functional programmatic behavior of a JSP Page that
specifies ”java” as the scripting language must be equivalent to the
functional programmatic behavior of the JSP Page Implementation Class
constructed from that JSP Page.
