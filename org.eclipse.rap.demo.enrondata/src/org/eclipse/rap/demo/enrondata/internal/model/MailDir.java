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

import java.io.File;


public class MailDir implements MailNode {

  private final File directory;

  public MailDir( File directory ) {
    this.directory = directory;
    if( directory == null ) {
      throw new NullPointerException( "directory is null" );
    }
    if( !directory.isDirectory() ) {
      throw new IllegalArgumentException( "Not a directory: " + directory.getAbsolutePath() );
    }
  }

  public int getChildCount() {
    return directory.listFiles().length;
  }

  public String getName() {
    return directory.getName();
  }

}
