package org.jboss.cdi.tck.tests.context.passivating.broken.producer.method.enterprise;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;

@Stateful
@SessionScoped
public class ProducerMethodParamInjectionCorralBroken extends Ranch {
    @Override
    public void ping() {
        
    }
    
    @Produces
    @British
    @SessionScoped
    public Herd produce(@British Cow cow){
       return new Herd();
    }
}
