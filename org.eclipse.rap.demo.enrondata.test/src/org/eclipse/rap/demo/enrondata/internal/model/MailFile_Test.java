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
import java.io.IOException;

import org.eclipse.rap.demo.enrondata.test.internal.TestUtil;
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
  public void create_withNonExisting() {
    try {
      new MailFile( parent, "does-not-exist" );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a file: " ) );
    }
  }

  @Test
  public void create_withDirectory() {
    TestUtil.createDirectory( parent.directory, "subdir" );
    try {
      new MailFile( parent, "subdir" );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertTrue( exception.getMessage().startsWith( "Not a file: " ) );
    }
  }

  @Test
  public void getContent_empty() throws IOException {
    createFile( parent.directory, "test", "" );
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( "", mailFile.getContent() );
  }

  @Test
  public void getContent_withNewlineAndTab() throws IOException {
    String content = "test\tcontent\nline two\n";
    createFile( parent.directory, "test", content );
    MailFile mailFile = new MailFile( parent, "test" );

    assertEquals( content, mailFile.getContent() );
  }

}
