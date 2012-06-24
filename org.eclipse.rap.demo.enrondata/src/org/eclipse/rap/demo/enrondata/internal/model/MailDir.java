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
import java.io.FilenameFilter;


public class MailDir extends MailNode {

  private static final FilenameFilter filter = new IndexFileFilter();
  final File directory;

  MailDir( MailDir parent, String name ) {
    super( parent, name );
    directory = new File( parent.directory, name );
    if( !directory.isDirectory() ) {
      throw new IllegalArgumentException( "Not a directory: " + directory.getAbsolutePath() );
    }
  }

  public MailDir( File directory ) {
    super( null, directory.getName() );
    this.directory = directory;
  }

  @Override
  public int getChildCount() {
    return directory.list( filter ).length;
  }

  public MailNode getChild( int index ) {
    MailNode child = null;
    File[] files = directory.listFiles( filter );
    File file = files[ index ];
    if( file.isDirectory() ) {
      child = new MailDir( this, file.getName() );
    } else if( file.isFile() ) {
      child = new MailFile( this, file.getName() );
    }
    return child;
  }

  private static final class IndexFileFilter implements FilenameFilter {

    public boolean accept( File dir, String name ) {
      return !name.startsWith( "." );
    }
  }

}
