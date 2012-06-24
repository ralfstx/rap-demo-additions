/*******************************************************************************
 * Copyright (c) 2011, 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.enrondata.internal.model;

import static org.eclipse.rap.demo.enrondata.internal.util.StringUtil.getLines;

import java.util.List;


public class Mail {

  private String sender;
  private String subject;
  private final String content;

  public Mail( String text ) {
    List<String> lines = getLines( text );
    StringBuilder buffer = new StringBuilder();
    boolean headerFinished = false;
    for( String line : lines ) {
      if( headerFinished ) {
        buffer.append( line );
        buffer.append( '\n' );
      } else if( sender == null && line.startsWith( "From:" ) ) {
        sender = line.substring( "From:".length() ).trim();
      } else if( subject == null && line.startsWith( "Subject:" ) ) {
        subject = line.substring( "Subject:".length() ).trim();
      } else if( "".equals( line ) ) {
        headerFinished = true;
      }
    }
    content = buffer.toString();
  }

  public String getSender() {
    return sender;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }
}
