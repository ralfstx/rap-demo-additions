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
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createDirectory;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createTempDir;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.delete;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MailDirIndex_Test {

  private File tmpDir;
  private MailDir parent;

  @Before
  public void setUp() {
    tmpDir = createTempDir();
    parent = new MailDir( tmpDir );
  }

  @After
  public void tearDown() {
    delete( tmpDir );
  }

  @Test( expected = NullPointerException.class )
  public void create_failsWithNull() {
    new MailDirIndex( null );
  }

  @Test
  public void exists_falseWithoutIndexFile() {
    MailDirIndex index = new MailDirIndex( parent );

    assertFalse( index.exists() );
  }

  @Test
  public void exists_trueWithIndexFile() {
    createFile( parent.directory, ".index", "" );
    MailDirIndex index = new MailDirIndex( parent );

    assertTrue( index.exists() );
  }

  @Test
  public void create_createsIndexFile() throws IOException {
    File indexFile = createFile( parent.directory, ".index", "" );
    new MailFile( parent, "child1" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    assertTrue( indexFile.exists() );
  }

  @Test
  public void create_doesNotTouchExistingIndexFile() throws IOException {
    File indexFile = createFile( parent.directory, ".index", "\n" );
    new MailFile( parent, "child1" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    assertEquals( 1, indexFile.length() );
  }

  @Test
  public void create_withFile() throws IOException {
    createFile( parent.directory, "child", "" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    MailNode[] nodes = index.readNodes();
    assertEquals( 1, nodes.length );
    MailFile found = ( MailFile )nodes[ 0 ];
    assertSame( parent, found.getParent() );
    assertEquals( "child", found.getName() );
  }

  @Test
  public void create_withDirectory() throws IOException {
    createDirectory( parent.directory, "child" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    MailNode[] nodes = index.readNodes();
    assertEquals( 1, nodes.length );
    MailDir found = ( MailDir )nodes[ 0 ];
    assertSame( parent, found.getParent() );
    assertEquals( "child", found.getName() );
  }

  @Test
  public void create_includesAllChildren() throws IOException {
    createFile( parent.directory, "child1", "" );
    createDirectory( parent.directory, "child2" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    MailNode[] nodes = index.readNodes();
    assertEquals( 2, nodes.length );
  }

  @Test
  public void create_worksRecursively() throws IOException {
    File dir_1 = createDirectory( parent.directory, "dir-1" );
    File dir_2 = createDirectory( parent.directory, "dir-2" );
    File dir_1_1 = createDirectory( dir_1, "dir-1-1" );
    createFile( dir_1_1, "file-1-1-1", "" );
    createFile( dir_1_1, "file-1-1-2", "" );
    MailDirIndex index = new MailDirIndex( parent );

    index.create();

    assertTrue( new File( dir_1, ".index" ).exists() );
    assertTrue( new File( dir_2, ".index" ).exists() );
    assertTrue( new File( dir_1_1, ".index" ).exists() );
  }

  @Test
  public void readNodes_setsChildCount() throws IOException {
    File childDir = createDirectory( parent.directory, "child" );
    createDirectory( childDir, "nested-1" );
    createDirectory( childDir, "nested-2" );
    MailDirIndex index = new MailDirIndex( parent );
    index.create();

    MailNode[] nodes = index.readNodes();

    MailDir found = ( MailDir )nodes[ 0 ];
    assertEquals( 2, found.getChildCount() );
  }

  @Test
  public void readNodes_ignoresEmptyLinesInIndex() throws IOException {
    createFile( parent.directory, ".index", "\n\n" );
    MailDirIndex index = new MailDirIndex( parent );

    MailNode[] nodes = index.readNodes();

    assertEquals( 0, nodes.length );
  }

  @Test
  public void readNodes_failsWithCorruptedIndex() throws IOException {
    createFile( parent.directory, ".index", "foo\n" );
    MailDirIndex index = new MailDirIndex( parent );

    try {
      index.readNodes();
      fail();
    } catch( RuntimeException e ) {
    }
  }

  @Test
  public void readNodes_failsWithCorruptedIndex2() throws IOException {
    createFile( parent.directory, ".index", "x\tfoo\tbar\n" );
    MailDirIndex index = new MailDirIndex( parent );

    try {
      index.readNodes();
      fail();
    } catch( RuntimeException e ) {
    }
  }

}
