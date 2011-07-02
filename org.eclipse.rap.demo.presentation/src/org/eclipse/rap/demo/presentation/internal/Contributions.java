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
import java.util.Collections;
import java.util.List;

import org.eclipse.rap.examples.IExampleContribution;
import org.eclipse.rap.examples.IExamplePage;


class Contributions {

  private List<IExampleContribution> contributions;

  Contributions() {
    contributions = new ArrayList<IExampleContribution>();
    collectContributions();
  }

  List<IExampleContribution> getContibutions() {
    return Collections.unmodifiableList( contributions );
  }

  private void collectContributions() {
    addContribution( "news", "1.4 New & Noteworthy", "Noteworthy.html" );
    addContribution( "rap", "RAP", "Start.html" );
    addContribution( "single-sourcing", "Single Sourcing", "SingleSourcing.html" );
  }

  private void addContribution( final String id, final String title, final String page ) {
    IExampleContribution contribution = new IExampleContribution() {

      public String getId() {
        return id;
      }

      public String getTitle() {
        return title;
      }

      public IExamplePage createPage() {
        return new PresentationExamplePage( page );
      }
    };
    contributions.add( contribution );
  }
}
