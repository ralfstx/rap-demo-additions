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
    return new MailFile( mailDir, name );
  }

  public void writeNodes( MailNode... nodes ) throws IOException {
    String content = createIndex( nodes );
    writeToFile( indexFile, content );
  }

  private static String createIndex( MailNode... nodes ) {
    StringBuilder builder = new StringBuilder();
    for( MailNode node : nodes ) {
      appendNode( builder, node );
    }
    return builder.toString();
  }

  private static void appendNode( StringBuilder stringBuilder, MailNode node ) {
    stringBuilder.append( node instanceof MailDir ? "d" : "f" );
    stringBuilder.append( '\t' );
    stringBuilder.append( node.getName() );
    stringBuilder.append( '\t' );
    stringBuilder.append( node.getChildCount() );
    stringBuilder.append( '\n' );
  }

  private static List<String> getLines( String content ) {
    return splitString( content, '\n' );
  }

  private static List<String> getFields( String line ) {
    return splitString( line, '\t' );
  }

}
