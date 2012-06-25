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

import static org.eclipse.rap.demo.enrondata.internal.util.FileUtil.readFromFile;
import static org.eclipse.rap.demo.enrondata.internal.util.FileUtil.writeToFile;
import static org.eclipse.rap.demo.enrondata.internal.util.StringUtil.getLines;
import static org.eclipse.rap.demo.enrondata.internal.util.StringUtil.splitString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MailDirIndex {

  private final MailDir mailDir;
  private final File indexFile;

  public MailDirIndex( MailDir mailDir ) {
    this.mailDir = mailDir;
    indexFile = new File( mailDir.directory, ".index" );
  }

  public boolean exists() {
    return indexFile.exists();
  }

  public MailNode[] readNodes() throws IOException {
    String content = readFromFile( indexFile );
    List<String> lines = getLines( content );
    List<MailNode> nodes = readNodes( lines );
    return nodes.toArray( new MailNode[ nodes.size() ] );
  }

  private List<MailNode> readNodes( List<String> lines ) {
    List<MailNode> nodes = new ArrayList<MailNode>();
    for( String line : lines ) {
      MailNode node = readNode( line );
      if( node != null ) {
        nodes.add( node );
      }
    }
    return nodes;
  }

  private MailNode readNode( String line ) {
    MailNode node = null;
    if( line.length() > 0 ) {
      List<String> fields = getFields( line );
      String type = fields.get( 0 );
      if( "d".equals( type ) ) {
        node = readMailDir( fields );
      } else if( "f".equals( type ) ) {
        node = readMailFile( fields );
      } else {
        throw new RuntimeException( "Unexpected file type in index: " + fields.get( 0 ) );
      }
    }
    return node;
  }

  private MailNode readMailDir( List<String> fields ) {
    String name = fields.get( 1 );
    int count = Integer.parseInt( fields.get( 2 ) );
    MailNode node = new MailDir( mailDir, name, count );
    return node;
  }

  private MailNode readMailFile( List<String> fields ) {
    String name = fields.get( 1 );
    String sender = fields.get( 2 );
    String subject = fields.get( 3 );
    return new MailFile( mailDir, name, sender, subject );
  }

  public void create() throws IOException {
    if( !indexFile.exists() ) {
      StringBuilder builder = new StringBuilder();
      int childCount = mailDir.getChildCount();
      for( int i = 0; i < childCount; i++ ) {
        MailNode child = mailDir.getChild( i );
        createIndex( child );
        appendNode( builder, child );
      }
      writeToFile( indexFile, builder.toString() );
    }
  }

  private void createIndex( MailNode child ) throws IOException {
    if( child instanceof MailDir ) {
      new MailDirIndex( ( MailDir )child ).create();
    }
  }

  private static void appendNode( StringBuilder stringBuilder, MailNode node ) throws IOException {
    if( node instanceof MailDir ) {
      appendMailDir( stringBuilder, ( MailDir )node );
    } else {
      appendMailFile( stringBuilder, ( MailFile )node );
    }
  }

  private static void appendMailDir( StringBuilder stringBuilder, MailDir mailDir ) {
    stringBuilder.append( 'd' );
    stringBuilder.append( '\t' );
    stringBuilder.append( mailDir.getName() );
    stringBuilder.append( '\t' );
    stringBuilder.append( mailDir.getChildCount() );
    stringBuilder.append( '\n' );
  }

  private static void appendMailFile( StringBuilder stringBuilder, MailFile mailFile )
    throws IOException
  {
    stringBuilder.append( 'f' );
    stringBuilder.append( '\t' );
    stringBuilder.append( mailFile.getName() );
    stringBuilder.append( '\t' );
    stringBuilder.append( mailFile.getSender() );
    stringBuilder.append( '\t' );
    stringBuilder.append( mailFile.getSubject() );
    stringBuilder.append( '\n' );
  }

  private static List<String> getFields( String line ) {
    return splitString( line, '\t' );
  }

}
