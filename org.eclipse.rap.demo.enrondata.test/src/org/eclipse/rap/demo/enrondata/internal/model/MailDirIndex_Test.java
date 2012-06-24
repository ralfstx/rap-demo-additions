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
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    assertFalse( mailDirIndex.exists() );
  }

  @Test
  public void exists_trueWithIndexFile() {
    createFile( parent.directory, ".index", "" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    assertTrue( mailDirIndex.exists() );
  }

  @Test
  public void writeNodes_createsFile() throws IOException {
    createFile( parent.directory, "child1", "" );
    MailNode child1 = new MailFile( parent, "child1" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    mailDirIndex.writeNodes( child1 );

    assertTrue( mailDirIndex.exists() );
  }

  @Test
  public void readNodes() throws IOException {
    createDirectory( parent.directory, "child1" );
    MailDir child1 = new MailDir( parent, "child1", 0 );
    createFile( parent.directory, "child2", "" );
    MailFile child2 = new MailFile( parent, "child2" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );
    mailDirIndex.writeNodes( child1, child2 );

    MailNode[] nodes = mailDirIndex.readNodes();

    assertEquals( 2, nodes.length );
    MailDir found1 = ( MailDir )nodes[ 0 ];
    MailFile found2 = ( MailFile )nodes[ 1 ];
    assertSame( parent, found1.getParent() );
    assertEquals( "child1", found1.getName() );
    assertSame( parent, found2.getParent() );
    assertEquals( "child2", found2.getName() );
  }

  @Test
  public void readNodes_setsChildCount() throws IOException {
    createDirectory( parent.directory, "maildir" );
    MailDir mailDir = new MailDir( parent, "maildir", 2 );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );
    mailDirIndex.writeNodes( mailDir );

    MailNode[] nodes = mailDirIndex.readNodes();

    MailDir found = ( MailDir )nodes[ 0 ];
    assertEquals( 2, found.getChildCount() );
  }

  @Test
  public void readNodes_ignoresEmptyLinesInIndex() throws IOException {
    createFile( parent.directory, ".index", "\n\n" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    MailNode[] nodes = mailDirIndex.readNodes();

    assertEquals( 0, nodes.length );
  }

  @Test
  public void readNodes_failsWithCorruptedIndex() throws IOException {
    createFile( parent.directory, ".index", "foo\n" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    try {
      mailDirIndex.readNodes();
      fail();
    } catch( RuntimeException e ) {
    }
  }

  @Test
  public void readNodes_failsWithCorruptedIndex2() throws IOException {
    createFile( parent.directory, ".index", "x\tfoo\tbar\n" );
    MailDirIndex mailDirIndex = new MailDirIndex( parent );

    try {
      mailDirIndex.readNodes();
      fail();
    } catch( RuntimeException e ) {
    }
  }

}
