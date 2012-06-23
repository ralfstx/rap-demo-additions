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

import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createFile;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createTempDir;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.delete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MailFile_Test {

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
      new MailFile( null );
      fail();
    } catch( NullPointerException exception ) {
    }
  }

  @Test
  public void create_withNonExisting() {
    File nonExisting = new File( "/does/not/exist" );
    try {
      new MailFile( nonExisting );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Not a file: /does/not/exist", exception.getMessage() );
    }
  }

  @Test
  public void create_withDirectory() {
    try {
      new MailFile( tmpDir );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a file: " ) );
    }
  }

  @Test
  public void getName() {
    File file = createFile( tmpDir, "test", "" );
    MailFile mailFile = new MailFile( file );

    assertEquals( "test", mailFile.getName() );
  }

}
