package org.jboss.cdi.tck.tests.event.observer.context.async;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class Counter {

    private AtomicInteger count = new AtomicInteger();

    public void increment() {
        count.incrementAndGet();
    }

    public AtomicInteger getCount() {
        return count;
    }

}
