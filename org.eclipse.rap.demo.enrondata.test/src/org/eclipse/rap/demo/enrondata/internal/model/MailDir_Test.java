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
  public void create_withNullParent() {
    new MailDir( null, "test", 0 );
  }

  @Test( expected = NullPointerException.class )
  public void create_withNullName() {
    new MailDir( parent, null, 0 );
  }

  @Test
  public void resolveChildren_failsWithNonExistingDirectory() {
    MailDir mailDir = new MailDir( parent, "does-not-exist", -1 );

    try {
      mailDir.getChildCount();
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a directory: " ) );
    }
  }

  @Test
  public void resolveChildren_failsWithFileInsteadDirectory() {
    createFile( parent.directory, "test", "" );
    MailDir mailDir = new MailDir( parent, "test", -1 );

    try {
      mailDir.getChildCount();
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a directory: " ) );
    }
  }

  @Test
  public void childCount_empty() {
    createDirectory( parent.directory, "maildir" );
    MailDir mailDir = new MailDir( parent, "maildir", -1 );

    assertEquals( 0, mailDir.getChildCount() );
  }

  @Test
  public void childCount_withFolderAndFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createDirectory( directory, "child1" );
    createFile( directory, "child2", "" );
    MailDir mailDir = new MailDir( parent, "maildir", -1 );

    assertEquals( 2, mailDir.getChildCount() );
  }

  @Test
  public void childCount_ignoresDotFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createFile( directory, ".hidden", "" );
    MailDir mailDir = new MailDir( parent, "maildir", -1 );

    assertEquals( 0, mailDir.getChildCount() );
  }

  @Test
  public void childCount_respectsValueFromConstructor() {
    MailDir mailDir = new MailDir( parent, "maildir", 23 );

    assertEquals( 23, mailDir.getChildCount() );
  }

  @Test( expected = IndexOutOfBoundsException.class )
  public void getChild_illegalIndex() {
    createDirectory( parent.directory, "maildir" );
    MailDir mailDir = new MailDir( parent, "maildir", 0 );

    mailDir.getChild( 0 );
  }

  @Test
  public void getChild_withDirectory() {
    File root = createDirectory( parent.directory, "maildir" );
    createDirectory( root, "child1" );
    MailDir mailDir = new MailDir( parent, "maildir", 1 );

    MailNode result = mailDir.getChild( 0 );

    assertTrue( result instanceof MailDir );
    assertEquals( "child1", result.getName() );
  }

  @Test
  public void getChild_withFile() {
    File root = createDirectory( parent.directory, "maildir" );
    createFile( root, "child1", "" );
    MailDir mailDir = new MailDir( parent, "maildir", 1 );

    MailNode result = mailDir.getChild( 0 );

    assertTrue( result instanceof MailFile );
    assertEquals( "child1", result.getName() );
  }

  @Test
  public void getChild_withDotFile() {
    File root = createDirectory( parent.directory, "maildir" );
    createFile( root, ".hidden", "" );
    MailDir mailDir = new MailDir( parent, "maildir", -1 );

    try {
      mailDir.getChild( 0 );
      fail();
    } catch( IndexOutOfBoundsException exception ) {
    }
  }

}
