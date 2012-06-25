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
  private Details details;

  MailFile( MailDir parent, String name, String sender, String subject ) {
    this( parent, name );
    details = new Details( sender, subject );
  }

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

  public String getSender() throws IOException {
    resolveDetails();
    return details.sender;
  }

  public String getSubject() throws IOException {
    resolveDetails();
    return details.subject;
  }

  private void checkFile() {
    if( !file.isFile() ) {
      throw new IllegalArgumentException( "Not a file: " + file.getAbsolutePath() );
    }
  }

  private void resolveDetails() throws IOException {
    if( details == null ) {
      String content = readFromFile( file );
      Mail mail = new Mail( content );
      String sender = mail.getSender();
      String subject = mail.getSubject();
      details = new Details( sender, subject );
    }
  }

  private static final class Details {
    public final String sender;
    public final String subject;

    public Details( String sender, String subject ) {
      this.sender = sender;
      this.subject = subject;
    }
  }
}
