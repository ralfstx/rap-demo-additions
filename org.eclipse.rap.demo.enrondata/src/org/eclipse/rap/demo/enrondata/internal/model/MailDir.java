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
import java.util.Arrays;
import java.util.Comparator;


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

  public MailNode[] getChildren() {
    resolveChildren();
    MailNode[] result = new MailNode[ children.length ];
    System.arraycopy( children, 0, result, 0, children.length );
    return result;
  }

  private void resolveChildren() {
    if( children == null ) {
      MailDirIndex dirIndex = new MailDirIndex( this );
      if( dirIndex.exists() ) {
        readChildrenFromIndex( dirIndex );
      } else {
        readChildrenFromFile();
      }
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
    Arrays.sort( children, new MailNodeComparator() );
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

  private static final class MailNodeComparator implements Comparator<MailNode> {

    public int compare( MailNode node1, MailNode node2 ) {
      int result = compareTypes( node1, node2 );
      if( result == 0 ) {
        result = compareNames( node1, node2 );
      }
      return result;
    }

    private int compareTypes( MailNode node1, MailNode node2 ) {
      int result = 0;
      boolean isDir1 = node1 instanceof MailDir;
      boolean isDir2 = node2 instanceof MailDir;
      if( isDir1 && !isDir2 ) {
        result = -1;
      } else if( !isDir1 && isDir2 ) {
        result = 1;
      }
      return result;
    }

    private static int compareNames( MailNode node1, MailNode node2 ) {
      int result = 0;
      String name1 = node1.getName();
      String name2 = node2.getName();
      int number1 = getOrdinalNumber( name1 );
      int number2 = getOrdinalNumber( name2 );
      if( number1 != -1 && number2 != -1 ) {
        if( number1 < number2 ) {
          result = -1;
        } else if( number1 > number2 ) {
          result = 1;
        }
      } else {
        result = name1.compareTo( name2 );
      }
      return result;
    }

    private static int getOrdinalNumber( String name ) {
      int length = name.length();
      if( name.charAt( length - 1 ) != '.' ) {
        return -1;
      }
      for( int i = 0; i < length - 1; i++ ) {
        if( !Character.isDigit( name.charAt( i ) ) ) {
          return -1;
        }
      }
      return Integer.parseInt( name.substring( 0, length - 1 ) );
    }

  }

}
