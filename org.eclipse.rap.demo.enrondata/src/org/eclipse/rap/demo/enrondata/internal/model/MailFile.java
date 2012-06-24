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

import static org.eclipse.rap.demo.enrondata.internal.util.FileUtil.readFromFile;

import java.io.File;
import java.io.IOException;


public class MailFile extends MailNode {

  private final File file;

  MailFile( MailDir parent, String name ) {
    super( parent, name );
    file = new File( parent.directory, name );
  }

  @Override
  public int getChildCount() {
    return 0;
  }

  public String getContent() throws IOException {
    checkFile();
    return readFromFile( file );
  }

  private void checkFile() {
    if( !file.isFile() ) {
      throw new IllegalArgumentException( "Not a file: " + file.getAbsolutePath() );
    }
  }

}
