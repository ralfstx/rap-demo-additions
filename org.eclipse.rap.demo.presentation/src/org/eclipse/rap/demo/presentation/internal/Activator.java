/*******************************************************************************
 * Copyright (c) 2011 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.presentation.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.examples.IExampleContribution;
import org.osgi.framework.*;


public class Activator implements BundleActivator {

  private static final String EXAMPLE_CONTRIB = IExampleContribution.class.getName();

  private Contributions contibutions;
  private List<ServiceRegistration<?>> registrations;

  public Activator() {
    contibutions = new Contributions();
    registrations = new ArrayList<ServiceRegistration<?>>();
  }

  public void start( BundleContext context ) throws Exception {
    for( IExampleContribution contribution : contibutions.getContibutions() ) {
      ServiceRegistration<?> registration;
      registration = context.registerService( EXAMPLE_CONTRIB, contribution, null );
      registrations.add( registration );
    }
  }

  public void stop( BundleContext context ) throws Exception {
    for( ServiceRegistration<?> registration : registrations ) {
      registration.unregister();
    }
  }
}
