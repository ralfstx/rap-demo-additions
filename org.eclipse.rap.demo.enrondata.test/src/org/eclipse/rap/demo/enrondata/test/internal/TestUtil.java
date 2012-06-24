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
package org.eclipse.rap.demo.enrondata.test.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


public class TestUtil {

  public static File createTempDir() {
    File tempDir = new File( System.getProperty( "java.io.tmpdir" ) );
    String name = "test_" + Long.toHexString( System.nanoTime() );
    return createDirectory( tempDir, name );
  }

  public static File createDirectory( File parent, String name ) {
    checkParent( parent );
    File directory = new File( parent, name );
    checkNotExisting( directory );
    if( !directory.mkdir() ) {
      throw new RuntimeException( "Could not create directory: " + directory.getAbsolutePath() );
    }
    return directory;
  }

  public static File createFile( File parent, String name, String content ) {
    checkParent( parent );
    File file = new File( parent, name );
    checkNotExisting( file );
    writeToFile( file, content );
    return file;
  }

  public static void delete( File file ) {
    File[] children = file.listFiles();
    if( children != null ) {
      for( File child : children ) {
        delete( child );
      }
    }
    file.delete();
  }

  public static String readFromFile( File file ) {
    try {
      return tryReadFromFile( file );
    } catch( IOException exception ) {
      String message = "Failed to read from file: " + file.getAbsolutePath();
      throw new RuntimeException( message, exception );
    }
  }

  private static String tryReadFromFile( File file ) throws IOException {
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

  public static void writeToFile( File file, String content ) {
    try {
      tryWriteToFile( file, content );
    } catch( IOException exception ) {
      String message = "Failed to write to file: " + file.getAbsolutePath();
      throw new RuntimeException( message, exception );
    }
  }

  private static void tryWriteToFile( File file, String content ) throws IOException {
    OutputStream outputStream = new FileOutputStream( file );
    try {
      writeToStream( outputStream, content );
    } finally {
      outputStream.close();
    }
  }

  private static void writeToStream( OutputStream outputStream, String content ) throws IOException
  {
    Writer writer = new OutputStreamWriter( outputStream, "UTF-8" );
    try {
      writer.write( content );
    } finally {
      writer.close();
    }
  }

  private static void checkParent( File parent ) {
    if( !parent.isDirectory() ) {
      throw new IllegalStateException( "Not a directory: " + parent.getAbsolutePath() );
    }
  }

  private static void checkNotExisting( File file ) {
    if( file.exists() ) {
      throw new IllegalStateException( "File exists already: " + file.getAbsolutePath() );
    }
  }
}
