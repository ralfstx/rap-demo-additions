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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class Mail_Test {

  @Test
  public void getSubject() {
    Mail mail = new Mail( getExampleMail() );

    assertEquals( "Fw: If Men Really Ruled the World", mail.getSubject() );
  }

  @Test
  public void getSender() {
    Mail mail = new Mail( getExampleMail() );

    assertEquals( "john.d.williamson@us.andersen.com", mail.getSender() );
  }

  @Test
  public void getContent() {
    Mail mail = new Mail( getExampleMail() );

    String content = mail.getContent();
    assertTrue( content.startsWith( "content start" ) );
    assertTrue( content.endsWith( "content end\n" ) );
  }

  private String getExampleMail() {
    return "Message-ID: <524130.1075855037521.JavaMail.evans@thyme>\n"
           + "Date: Thu, 11 Oct 2001 11:46:07 -0700 (PDT)\n"
           + "From: john.d.williamson@us.andersen.com\n"
           + "Subject: Fw: If Men Really Ruled the World\n"
           + "Mime-Version: 1.0\n\n"
           + "content start\n"
           + "bla bla bla\n"
           + "content end\n";
  }

}
