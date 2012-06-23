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


public class MailFile implements MailNode {

  private final File file;

  public MailFile( File file ) {
    this.file = file;
    if( file == null ) {
      throw new NullPointerException( "file is null" );
    }
    if( !file.isFile() ) {
      throw new IllegalArgumentException( "Not a file: " + file.getAbsolutePath() );
    }
  }

  public String getName() {
    return file.getName();
  }
}
