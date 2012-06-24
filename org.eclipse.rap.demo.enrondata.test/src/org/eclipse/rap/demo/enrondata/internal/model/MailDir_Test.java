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
    new MailDir( null, "test" );
  }

  @Test( expected = NullPointerException.class )
  public void create_withNullName() {
    new MailDir( parent, null );
  }

  @Test
  public void create_withNonExisting() {
    try {
      new MailDir( parent, "does-not-exist" );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a directory: " ) );
    }
  }

  @Test
  public void create_withFile() {
    createFile( parent.directory, "test", "" );
    try {
      new MailDir( parent, "test" );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a directory: " ) );
    }
  }

  @Test
  public void childCount_empty() {
    createDirectory( parent.directory, "maildir" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    assertEquals( 0, mailDir.getChildCount() );
  }

  @Test
  public void childCount_withFolderAndFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createDirectory( directory, "child1" );
    createFile( directory, "child2", "" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    assertEquals( 2, mailDir.getChildCount() );
  }

  @Test
  public void childCount_withDotFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createDirectory( directory, "child1" );
    createFile( directory, ".hidden", "" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    assertEquals( 1, mailDir.getChildCount() );
  }

  @Test( expected = IndexOutOfBoundsException.class )
  public void getChild_illegalIndex() {
    createDirectory( parent.directory, "maildir" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    mailDir.getChild( 0 );
  }

  @Test
  public void getChild_withDirectory() {
    File directory = createDirectory( parent.directory, "maildir" );
    createDirectory( directory, "child1" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    MailNode result = mailDir.getChild( 0 );

    assertTrue( result instanceof MailDir );
    assertEquals( "child1", result.getName() );
  }

  @Test
  public void getChild_withFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createFile( directory, "child1", "" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    MailNode result = mailDir.getChild( 0 );

    assertTrue( result instanceof MailFile );
    assertEquals( "child1", result.getName() );
  }

  @Test
  public void getChild_withDotFile() {
    File directory = createDirectory( parent.directory, "maildir" );
    createFile( directory, ".hidden", "" );
    MailDir mailDir = new MailDir( parent, "maildir" );

    try {
      mailDir.getChild( 0 );
      fail();
    } catch( IndexOutOfBoundsException exception ) {
    }
  }

}
