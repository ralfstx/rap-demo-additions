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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


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

  public String getContent() throws IOException {
    InputStream inputStream = new FileInputStream( file );
    try {
      return readFromStream( inputStream );
    } finally {
      inputStream.close();
    }
  }

  private static String readFromStream( InputStream inputStream ) throws IOException {
    Reader reader = new InputStreamReader( inputStream, "UTF-8" );
    try {
      return readContent( reader );
    } finally {
      reader.close();
    }
  }

  private static String readContent( Reader reader ) throws IOException {
    StringBuilder content = new StringBuilder();
    char[] buffer = new char[ 81920 ];
    int read = reader.read( buffer );
    while( read != -1 ) {
      content.append( buffer, 0, read );
      read = reader.read( buffer );
    }
    return content.toString();
  }

}
