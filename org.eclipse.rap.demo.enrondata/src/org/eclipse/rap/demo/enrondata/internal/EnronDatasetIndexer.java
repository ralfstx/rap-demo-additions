/*******************************************************************************
 * Copyright (c) 2011, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.enrondata.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.rap.demo.enrondata.internal.model.MailDir;
import org.eclipse.rap.demo.enrondata.internal.model.MailDirIndex;


public class EnronDatasetIndexer {

  public static void main( String[] args ) {
    if( args.length == 0 ) {
      System.err.println( "Missing root directory" );
      System.exit( 42 );
    }
    File root = new File( args[ 0 ] );
    System.out.println( "Indexing " + root.getAbsolutePath() + " ..." );
    long t0 = System.currentTimeMillis();
    createIndex( root );
    long t1 = System.currentTimeMillis();
    System.out.println( "Done." );
    System.out.println( ( ( t1 - t0 ) / 1000.0 ) + "s" );
  }

  private static void createIndex( File root ) {
    MailDir mailDir = new MailDir( root );
    MailDirIndex index = new MailDirIndex( mailDir );
    try {
      index.create();
    } catch( IOException exception ) {
      throw new RuntimeException( exception );
    }
  }

}
