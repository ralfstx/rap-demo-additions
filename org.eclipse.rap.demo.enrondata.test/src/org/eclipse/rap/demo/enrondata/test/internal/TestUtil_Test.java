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

import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createDirectory;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createFile;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createTempDir;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.delete;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.readFromFile;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.writeToFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;

import org.junit.After;
import org.junit.Test;


public class TestUtil_Test {

  private File tmpDir;

  @After
  public void tearDown() {
    delete( tmpDir );
  }

  @Test
  public void createTmpDir() {
    tmpDir = createTempDir();

    assertTrue( tmpDir.isDirectory() );
  }

  @Test
  public void createDirectory_createsNewDirectory() {
    tmpDir = createTempDir();
    File directory = createDirectory( tmpDir, "test" );

    assertTrue( directory.isDirectory() );
    assertEquals( tmpDir, directory.getParentFile() );
    assertEquals( "test", directory.getName() );
  }

  @Test
  public void createFile_withEmptyContent() {
    tmpDir = createTempDir();
    File file = createFile( tmpDir, "test", "" );

    assertTrue( file.isFile() );
    assertEquals( tmpDir, file.getParentFile() );
    assertEquals( "test", file.getName() );
    assertEquals( 0, file.length() );
  }

  @Test
  public void createFile_withContent() {
    tmpDir = createTempDir();
    File file = createFile( tmpDir, "test", "test content" );

    assertTrue( file.isFile() );
    assertEquals( tmpDir, file.getParentFile() );
    assertEquals( "test", file.getName() );
    assertEquals( "test content", readFromFile( file ) );
  }

  @Test
  public void writeToFile_createsFile() {
    tmpDir = createTempDir();
    File file = new File( tmpDir, "test" );

    writeToFile( file, "test content" );

    assertEquals( "test content", readFromFile( file ) );
  }

  @Test
  public void writeToFile_overwritesExistingFile() {
    tmpDir = createTempDir();
    File file = createFile( tmpDir, "test", "" );

    writeToFile( file, "test content" );

    assertEquals( "test content", readFromFile( file ) );
  }

}
