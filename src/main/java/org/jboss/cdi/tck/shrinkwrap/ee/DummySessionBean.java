package org.jboss.cdi.tck.shrinkwrap.ee;

import jakarta.ejb.Stateless;

/**
 * Dummy session bean used to make every EJB module <i>true</i> EJB module (JBoss AS7 sub-deployment).
 * 
 * See also <a href="http://community.jboss.org/message/623471?tstart=0">Test EAR deploys on AS7, but test config can't be
 * found</a> or <a href="http://community.jboss.org/message/615412">War classes cannot see EJB classes</a> threads.
 * 
 * @author Martin Kouba
 */
@Stateless(name = "org.jboss.cdi.tck.shrinkwrap.DummySessionBean")
public class DummySessionBean {

}
