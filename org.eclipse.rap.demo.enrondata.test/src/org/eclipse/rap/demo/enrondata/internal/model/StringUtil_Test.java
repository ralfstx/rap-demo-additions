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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class StringUtil_Test {

  @Test( expected = NullPointerException.class )
  public void splitString_failsWithNullString() {
    StringUtil.splitString( null, '.' );
  }

  @Test
  public void splitString_emptyString() {
    List<String> parts = StringUtil.splitString( "", '.' );

    assertEquals( list( "" ), parts );
  }

  @Test
  public void splitString_emptyParts() {
    List<String> parts = StringUtil.splitString( ".", '.' );

    assertEquals( list( "", "" ), parts );
  }

  @Test
  public void splitString_multipleParts() {
    List<String> parts = StringUtil.splitString( "foo.bar", '.' );

    assertEquals( list( "foo", "bar" ), parts );
  }

  @Test
  public void splitString_emptyPart() {
    List<String> parts = StringUtil.splitString( "foo..bar", '.' );

    assertEquals( list( "foo", "", "bar" ), parts );
  }

  @Test
  public void splitString_noOccurence() {
    List<String> parts = StringUtil.splitString( "foo", '.' );

    assertEquals( list( "foo" ), parts );
  }

  private static List<String> list( String... strings ) {
    ArrayList<String> list = new ArrayList<String>();
    for( String string : strings ) {
      list.add( string );
    }
    return list;
  }

}
