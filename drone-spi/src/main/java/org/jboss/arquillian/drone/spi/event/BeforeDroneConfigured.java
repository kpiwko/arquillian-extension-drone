/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.drone.spi.event;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

public class BeforeDroneConfigured {

    private final Configurator<?, ? extends DroneConfiguration<?>> configurator;
    private final Class<?> droneType;
    private final Class<? extends Annotation> qualifier;

    public BeforeDroneConfigured(Configurator<?, ? extends DroneConfiguration<?>> configurator, Class<?> droneType,
            Class<? extends Annotation> qualifier) {
        this.configurator = configurator;
        this.droneType = droneType;
        this.qualifier = qualifier;
    }

    public Configurator<?, ? extends DroneConfiguration<?>> getConfigurator() {
        return configurator;
    }

    public Class<?> getDroneType() {
        return droneType;
    }

    public Class<? extends Annotation> getQualifier() {
        return qualifier;
    }

}
