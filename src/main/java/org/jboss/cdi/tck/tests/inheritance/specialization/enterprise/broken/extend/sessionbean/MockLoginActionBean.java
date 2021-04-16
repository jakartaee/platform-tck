package org.jboss.cdi.tck.tests.inheritance.specialization.enterprise.broken.extend.sessionbean;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Specializes;

@Stateless
@Mock
@Specializes
public class MockLoginActionBean extends LoginActionBean {
}
