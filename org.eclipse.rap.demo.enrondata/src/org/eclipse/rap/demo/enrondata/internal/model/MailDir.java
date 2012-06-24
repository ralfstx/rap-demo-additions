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
import java.io.IOException;


public class MailDir extends MailNode {

  private static final FilenameFilter filter = new IndexFileFilter();

  final File directory;
  private int childCount;

  private MailNode[] children;

  public MailDir( File directory ) {
    super( null, directory.getName() );
    this.directory = directory;
    childCount = -1;
  }

  MailDir( MailDir parent, String name, int childCount ) {
    super( parent, name );
    directory = new File( parent.directory, name );
    this.childCount = childCount;
  }

  @Override
  public int getChildCount() {
    if( childCount == -1 ) {
      resolveChildren();
      childCount = children.length;
    }
    return childCount;
  }

  public MailNode getChild( int index ) {
    resolveChildren();
    return children[ index ];
  }

  private void resolveChildren() {
    MailDirIndex dirIndex = new MailDirIndex( this );
    if( dirIndex.exists() ) {
      readChildrenFromIndex( dirIndex );
    } else {
      readChildrenFromFile();
    }
  }

  private void readChildrenFromIndex( MailDirIndex dirIndex ) {
    try {
      children = dirIndex.readNodes();
    } catch( IOException exception ) {
      throw new RuntimeException( "Failed to read from index file", exception );
    }
  }

  private void readChildrenFromFile() {
    checkDirectory();
    File[] files = directory.listFiles( filter );
    children = new MailNode[ files.length ];
    for( int i = 0; i < files.length; i++ ) {
      children[ i ] = createNode( files[ i ] );
    }
  }

  private void checkDirectory() {
    if( !directory.isDirectory() ) {
      throw new IllegalArgumentException( "Not a directory: " + directory.getAbsolutePath() );
    }
  }

  private MailNode createNode( File file ) {
    MailNode node;
    if( file.isDirectory() ) {
      node = new MailDir( this, file.getName(), -1 );
    } else if( file.isFile() ) {
      node = new MailFile( this, file.getName() );
    } else {
      throw new RuntimeException( "Unexpected file type: " + file.getAbsolutePath() );
    }
    return node;
  }

  private static final class IndexFileFilter implements FilenameFilter {

    public boolean accept( File dir, String name ) {
      return !name.startsWith( "." );
    }
  }

}
