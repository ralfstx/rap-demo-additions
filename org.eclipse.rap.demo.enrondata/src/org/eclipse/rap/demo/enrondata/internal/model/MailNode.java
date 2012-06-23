/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.enrondata.internal.model;


public abstract class MailNode {

  private final MailDir parent;
  private final String name;

  MailNode( MailDir parent, String name ) {
    if( name == null ) {
      throw new NullPointerException( "name is null" );
    }
    this.parent = parent;
    this.name = name;
  }

  public MailNode getParent() {
    return parent;
  }

  public String getName() {
    return name;
  }

}
