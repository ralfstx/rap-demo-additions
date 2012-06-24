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
package org.eclipse.rap.demo.enrondata.internal.util;

import java.util.ArrayList;
import java.util.List;


public class StringUtil {

  public static List<String> splitString( String string, char ch ) {
    List<String> parts = new ArrayList<String>();
    int beginIndex = 0;
    int endIndex = string.indexOf( ch );
    while( endIndex != -1 ) {
      String nextPart = string.substring( beginIndex, endIndex );
      parts.add( nextPart );
      beginIndex = endIndex + 1;
      endIndex = string.indexOf( ch, beginIndex );
    }
    String lastPart = string.substring( beginIndex, string.length() );
    parts.add( lastPart );
    return parts;
  }

}
