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
package org.eclipse.rap.demo.enrondata.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.rap.demo.enrondata.internal.model.MailDir;
import org.eclipse.rap.demo.enrondata.internal.model.MailNode;


final class EnronDataset {

  private final File root;

  public EnronDataset( File root ) {
    this.root = root;
  }

  public MailNode getRootNode() throws IOException {
    new EnronDatasetIndexer( root ).index();
    return new MailDir( root );
  }

}
