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

import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.createTempDir;
import static org.eclipse.rap.demo.enrondata.test.internal.TestUtil.delete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MailNode_Test {

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
  public void create_withNullName() {
    new TestMailNode( parent, null );
  }

  @Test
  public void getParent_withNull() {
    MailNode mailNode = new TestMailNode( null, "test" );

    assertNull( mailNode.getParent() );
  }

  @Test
  public void getParent() {
    MailNode mailFile = new TestMailNode( parent, "test" );

    assertSame( parent, mailFile.getParent() );
  }

  @Test
  public void getName() {
    MailNode mailFile = new TestMailNode( parent, "test" );

    assertEquals( "test", mailFile.getName() );
  }

  private static class TestMailNode extends MailNode {

    TestMailNode( MailDir parent, String name ) {
      super( parent, name );
    }

    @Override
    public int getChildCount() {
      return 0;
    }

  }

}
