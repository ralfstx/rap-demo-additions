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
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FileUtil_Test {

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
  public void readFromFile_empty() throws IOException {
    File file = createFile( tmpDir, "test", "" );

    String result = FileUtil.readFromFile( file );

    assertEquals( "", result );
  }

  @Test
  public void readFromFile_withTabsAndNewLines() throws IOException {
    String content = "Line\tOne\nLine\tTwo\n";
    File file = createFile( tmpDir, "test", content );

    String result = FileUtil.readFromFile( file );

    assertEquals( content, result );
  }

}
