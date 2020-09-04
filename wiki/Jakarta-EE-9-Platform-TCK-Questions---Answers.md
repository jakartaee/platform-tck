Question: How can I add a question?

Answer: Join the [jakartaee-tck-dev mailing list and ask your question there](https://accounts.eclipse.org/mailing-list/jakartaee-tck-dev).  The [TCK committers ](https://projects.eclipse.org/projects/ee4j.jakartaee-tck/who) will see your question and try to answer it when they can.  The question/answer can be copied here by the TCK committers.](https://accounts.eclipse.org/mailing-list/jakartaee-tck-dev).

Question:  From [jaxrs-dev/msg00847.html](https://www.eclipse.org/lists/jaxrs-dev/msg00847.html)
```When extending the JAX-RS TCK, apparently unique Assertion Numbers have to get assigned. Is there some list of the existing assertions somewhere? How to know the next free number?```  

Answer:  From [jaxrs-dev/msg00848.html](https://www.eclipse.org/lists/jaxrs-dev/msg00848.html)
```The list of assertions is kept here:

https://github.com/eclipse-ee4j/jakartaee-tck/blob/master/internal/docs/jaxrs/JAXRSSpecAssertions.xml

From that an HTML version is generated:

https://github.com/eclipse-ee4j/jakartaee-tck/blob/master/internal/docs/jaxrs/JAXRSSpecAssertions.html

And just to be sure nobody accidentally deletes them, there's also a backup copy being kept here:

https://github.com/eclipse-ee4j/jakartaee-tck/blob/master/install/jaxrs/docs/assertions/JAXRSSpecAssertions.html

If you update the spec document, all those 3 files need to be updated to mirror exactly what's in the spec document, as the "JAXRSSpecAssertions.xml" file essentially contains verbatim quotes from the spec document. The HTML versions are manually updated these days, since the tools to generate them have largely been lost (there's some stuff here though: https://github.com/eclipse-ee4j/jakartaee-tck-tools).
```

Question:

Answer:

