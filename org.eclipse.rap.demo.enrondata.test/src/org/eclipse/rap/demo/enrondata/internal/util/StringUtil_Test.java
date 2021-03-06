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

import static org.eclipse.rap.demo.enrondata.internal.util.StringUtil.splitString;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class StringUtil_Test {

  @Test( expected = NullPointerException.class )
  public void splitString_failsWithNullString() {
    splitString( null, '.' );
  }

  @Test
  public void splitString_emptyString() {
    List<String> parts = splitString( "", '.' );

    assertEquals( list( "" ), parts );
  }

  @Test
  public void splitString_emptyParts() {
    List<String> parts = splitString( ".", '.' );

    assertEquals( list( "", "" ), parts );
  }

  @Test
  public void splitString_multipleParts() {
    List<String> parts = splitString( "foo.bar", '.' );

    assertEquals( list( "foo", "bar" ), parts );
  }

  @Test
  public void splitString_emptyPart() {
    List<String> parts = splitString( "foo..bar", '.' );

    assertEquals( list( "foo", "", "bar" ), parts );
  }

  @Test
  public void splitString_noOccurence() {
    List<String> parts = splitString( "foo", '.' );

    assertEquals( list( "foo" ), parts );
  }

  @Test
  public void splitString_skipsEmptyLastElement() {
    List<String> parts = splitString( "..", '.', true );

    assertEquals( list( "", "" ), parts );
  }

  @Test
  public void getLines_skipEmptyLastLine() {
    List<String> lines = StringUtil.getLines( "one\ntwo\n" );

    assertEquals( list( "one", "two" ), lines );
  }

  private static List<String> list( String... strings ) {
    ArrayList<String> list = new ArrayList<String>();
    for( String string : strings ) {
      list.add( string );
    }
    return list;
  }

}
