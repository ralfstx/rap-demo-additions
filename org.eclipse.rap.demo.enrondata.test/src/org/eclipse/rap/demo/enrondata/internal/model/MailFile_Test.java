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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.rap.demo.enrondata.internal.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MailFile_Test {

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
    new MailFile( null, "test" );
  }

  @Test( expected = NullPointerException.class )
  public void create_withNullName() {
    new MailFile( parent, null );
  }

  @Test
  public void getChildCount() {
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( 0, mailFile.getChildCount() );
  }

  @Test
  public void getContent_failsWithNonExistingFile() throws IOException {
    MailFile mailFile = new MailFile( parent, "does-not-exist" );

    try {
      mailFile.getContent();
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a file: " ) );
    }
  }

  @Test
  public void getContent_failsWithDirectoryInsteadFile() throws IOException {
    createDirectory( parent.directory, "subdir" );
    MailFile mailFile = new MailFile( parent, "subdir" );

    try {
      mailFile.getContent();
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a file: " ) );
    }
  }

  @Test
  public void getContent() throws IOException {
    createFile( parent.directory, "test", "content" );
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( "content", mailFile.getContent() );
  }

  @Test
  public void getSender() throws IOException {
    createFile( parent.directory, "test", getExampleMail() );
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( "john.doe@nowhere.com", mailFile.getSender() );
  }

  @Test
  public void getSubject() throws IOException {
    createFile( parent.directory, "test", getExampleMail() );
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( "Re: Foo bar", mailFile.getSubject() );
  }

  @Test
  public void getSenderAndSubject_fromConstructor() throws IOException {
    MailFile mailFile = new MailFile( parent, "test", "sender", "subject" );

    assertEquals( "sender", mailFile.getSender() );
    assertEquals( "subject", mailFile.getSubject() );
  }

  @Test
  public void details_resolvedOnlyOnce() throws IOException {
    File file = createFile( parent.directory, "test", getExampleMail() );
    MailFile mailFile = new MailFile( parent, "test" );
    mailFile.getSubject();
    FileUtil.writeToFile( file, "" );

    assertEquals( "john.doe@nowhere.com", mailFile.getSender() );
    assertEquals( "Re: Foo bar", mailFile.getSubject() );
  }

  private static String getExampleMail() {
    return "Message-ID: <4711.foo@bar>\n"
           + "From: john.doe@nowhere.com\n"
           + "Subject: Re: Foo bar\n"
           + "\n"
           + "mail content\n";
  }

}
