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
  public void getSender() {
    Mail mail = new Mail( getExampleMail( "john@example.com", "", "content" ) );

    assertEquals( "john@example.com", mail.getSender() );
  }

  @Test
  public void getSender_withoutText() {
    Mail mail = new Mail( "" );

    assertEquals( "", mail.getSender() );
  }

  @Test
  public void getSubject() {
    Mail mail = new Mail( getExampleMail( "", "the subject", "content" ) );

    assertEquals( "the subject", mail.getSubject() );
  }

  @Test
  public void getSubject_withoutText() {
    Mail mail = new Mail( "" );

    assertEquals( "", mail.getSubject() );
  }

  @Test
  public void getContent_withMultipleBlocks() {
    Mail mail = new Mail( getExampleMail( "", "", "content start\n\ncontent end\n" ) );

    String content = mail.getContent();
    assertTrue( content.startsWith( "content start" ) );
    assertTrue( content.endsWith( "content end\n" ) );
  }

  @Test
  public void getContent_withMissingNewlineAtEndOfFile() {
    Mail mail = new Mail( "From: foo\n\ncontent" );

    String content = mail.getContent();
    assertEquals( "content\n", content );
  }

  @Test
  public void getContent_withWindowsLinebreaks() {
    Mail mail = new Mail( "From: foo\r\n\r\ncontent" );

    String content = mail.getContent();
    assertEquals( "content\n", content );
  }

  private static String getExampleMail( String sender, String subject, String body ) {
    return "Message-ID: <4711@example.com>\n"
        + "Date: Thu, 11 Oct 2001 11:46:07 -0700 (PDT)\n"
        + "From: " + sender + "\n"
        + "Subject: " + subject + "\n"
        + "\n"
        + body;
  }

}
