package org.jboss.arquillian.drone.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.spi.Enhancer;
import org.jboss.arquillian.drone.spi.event.AfterDroneInstantiated;
import org.jboss.arquillian.drone.spi.event.BeforeDroneDestroyed;

public class DroneEnhancer {

    private static final Logger log = Logger.getLogger(DroneEnhancer.class.getName());

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void enhanceDrone(@Observes AfterDroneInstantiated droneInstance, DroneContext context) {
        List<Enhancer> enhancers = new ArrayList<Enhancer>(serviceLoader.get().all(Enhancer.class));
        Collections.sort(enhancers, PrecedenceComparator.getInstance());

        Object browser = droneInstance.getInstance();
        final Class<?> type = droneInstance.getDroneType();
        final Class<? extends Annotation> qualifier = droneInstance.getQualifier();

        for (Enhancer enhancer : enhancers) {
            if (enhancer.canEnhance(type, qualifier)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Eenhancing using enhancer: " + enhancer.getClass().getName() + ", with precedence "
                            + enhancer.getPrecedence());
                }

                // we actually need browser instance to be updated in context for us
                browser = enhancer.enhance(browser, qualifier);
            }
        }

        // replace in context with enriched instance
        context.replace(type, qualifier, browser);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void deenhanceDrone(@Observes BeforeDroneDestroyed droneInstance, DroneContext context) {

        List<Enhancer> enhancers = new ArrayList<Enhancer>(serviceLoader.get().all(Enhancer.class));
        // here we are deenhancing in reversed order
        Collections.sort(enhancers, PrecedenceComparator.getReversedOrder());

        Object browser = droneInstance.getInstance();
        final Class<?> type = droneInstance.getDroneType();
        final Class<? extends Annotation> qualifier = droneInstance.getQualifier();

        for (Enhancer enhancer : enhancers) {
            if (enhancer.canEnhance(type, qualifier)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Deenhancing using enhancer: " + enhancer.getClass().getName() + ", with precedence "
                            + enhancer.getPrecedence());
                }

                // we actually need browser instance to be updated in context for us
                browser = enhancer.deenhance(browser, qualifier);
            }
        }

        // replace in context with deenriched instance
        context.replace(type, qualifier, browser);
    }

}
