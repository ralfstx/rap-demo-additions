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

import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createDirectory;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createFile;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createTempDir;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.delete;
import static org.junit.Assert.*;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MailDir_Test {

  private File tmpDir;

  @Before
  public void setUp() {
    tmpDir = createTempDir();
  }

  @After
  public void tearDown() {
    delete( tmpDir );
  }

  @Test
  public void create_withNull() {
    try {
      new MailDir( null );
      fail();
    } catch( NullPointerException exception ) {
    }
  }

  @Test
  public void create_withNonExisting() {
    File directory = new File( "/does/not/exist" );
    try {
      new MailDir( directory );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Not a directory: /does/not/exist", exception.getMessage() );
    }
  }

  @Test
  public void create_withFile() {
    File file = createFile( tmpDir, "test", "" );
    try {
      new MailDir( file );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a directory: " ) );
    }
  }

  @Test
  public void getName() {
    File directory = createDirectory( tmpDir, "test" );
    MailDir mailDir = new MailDir( directory );

    assertEquals( "test", mailDir.getName() );
  }

  @Test
  public void childCount_empty() {
    MailDir mailDir = new MailDir( tmpDir );

    assertEquals( 0, mailDir.getChildCount() );
  }

  @Test
  public void childCount_withFolderAndFile() {
    createDirectory( tmpDir, "child1" );
    createFile( tmpDir, "child2", "" );
    MailDir mailDir = new MailDir( tmpDir );

    assertEquals( 2, mailDir.getChildCount() );
  }
}
