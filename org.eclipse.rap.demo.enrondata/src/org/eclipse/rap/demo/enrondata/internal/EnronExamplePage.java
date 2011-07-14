/*******************************************************************************
 * Copyright (c) 2009, 2011 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.demo.enrondata.internal;

import java.io.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.rap.demo.enrondata.internal.EnronDataset.Folder;
import org.eclipse.rap.demo.enrondata.internal.EnronDataset.Node;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;


public class EnronExamplePage implements IExamplePage {

  private static final String DEFAULT_DATASET_DIR = "/data/enron/maildir";
  private static final String DATASET_DIR_PROP = "org.eclipse.rap.demo.enronDatasetDirectory";

  private TreeViewer viewer;
  private Text text;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createTreeArea( parent );
    createTextArea( parent );
  }

  private void createTreeArea( Composite parent ) {
    Group group = new Group( parent, SWT.NONE );
    group.setText( "Enron Dataset (520.929 items)" );
    group.setLayoutData( ExampleUtil.createFillData() );
    FillLayout layout = new FillLayout();
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    group.setLayout( layout );
    createTreeViewer( group );
  }

  private void createTextArea( Composite parent ) {
    Group group = new Group( parent, SWT.NONE );
    group.setLayoutData( ExampleUtil.createFillData() );
    FillLayout layout = new FillLayout();
    layout.marginHeight = 5;
    group.setLayout( layout );
    createText( group );
  }

  private void createTreeViewer( Composite parent ) {
    viewer = new TreeViewer( parent, SWT.SINGLE | SWT.VIRTUAL | SWT.BORDER );
    viewer.setLabelProvider( new EnronLabelProvider( parent.getDisplay() ) );
    viewer.setContentProvider( new EnronLazyContentProvider( viewer ) );
    viewer.setInput( getDataSet() );
    viewer.addSelectionChangedListener( new ISelectionChangedListener() {
      
      public void selectionChanged( SelectionChangedEvent event ) {
        IStructuredSelection selection = ( IStructuredSelection )event.getSelection();
        Object firstElement = selection.getFirstElement();
        if( firstElement instanceof Node ) {
          nodeSelected( ( Node )firstElement );
        }
      }
    } );
  }

  private void createText( Composite parent ) {
    text = new Text( parent, SWT.MULTI | SWT.WRAP | SWT.BORDER );
  }

  private void nodeSelected( Node selectedNode ) {
    if( selectedNode != null ) {
      if( selectedNode.getFile().isFile() ) {
        try {
          text.setText( selectedNode.readContents() );
        } catch( IOException exception ) {
          throw new RuntimeException( "Failed to read contents from node", exception );
        }
      }
    }
  }

  private static Node getDataSet() {
    try {
      File root = getRootDirectory();
      return new EnronDataset( root ).getRootNode();
    } catch( IOException exception ) {
      throw new IllegalStateException( "Could not access data model", exception );
    }
  }

  private static File getRootDirectory() {
    String path = getRootDirectoryPath();
    File root = new File( path );
    if( !root.isDirectory() ) {
      throw new RuntimeException( "Enron dataset directory missing: " + root );
    }
    return root;
  }

  private static String getRootDirectoryPath() {
    String path = System.getProperty( DATASET_DIR_PROP );
    if( path == null ) {
      path = DEFAULT_DATASET_DIR;
    }
    return path;
  }

  private static final class EnronLabelProvider extends CellLabelProvider {

    private static final String ICON_FILE = "resources/file.png";
    private static final String ICON_FOLDER = "resources/folder.png";

    private static final int COLUMN_NAME = 0;
    private static final int COLUMN_OFFSET = 2;
    private static final int COLUMN_TIMEZONE = 1;

    private final Image fileImage;
    private final Image folderImage;

    EnronLabelProvider( final Device device ) {
      fileImage = createImage( device, ICON_FILE );
      folderImage = createImage( device, ICON_FOLDER );
    }

    @Override
    public void update( final ViewerCell cell ) {
      Object element = cell.getElement();
      if( element instanceof Node ) {
        Node node = ( Node )element;
        int columnIndex = cell.getColumnIndex();
        switch( columnIndex ) {
          case COLUMN_NAME:
            updateName( cell, node );
            break;
          case COLUMN_TIMEZONE:
            updateName( cell, node );
            break;
          case COLUMN_OFFSET:
            updateName( cell, node );
            break;
        }
      }
    }

    @Override
    public String getToolTipText( final Object element ) {
      String result = "";
      if( element instanceof File ) {
        File file = ( File )element;
        result = file.getName();
      }
      return result;
    }

    private void updateName( ViewerCell cell, Node node ) {
      cell.setText( node.getTitle() );
      cell.setImage( node instanceof Folder ? folderImage : fileImage );
    }

    private static Image createImage( Device device, String name ) {
      ClassLoader classLoader = EnronLabelProvider.class.getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream( name );
      Image result = null;
      if( inputStream != null ) {
        result = new Image( device, inputStream );
      }
      return result;
    }
  }

  private static class EnronLazyContentProvider implements ILazyTreeContentProvider {

    private final TreeViewer viewer;

    public EnronLazyContentProvider( TreeViewer viewer ) {
      this.viewer = viewer;
    }

    public Object getParent( Object element ) {
      Object result = null;
      if( element instanceof Node ) {
        result = ( ( Node )element ).getParent();
      }
      return result;
    }

    public void updateElement( Object parent, int index ) {
      if( parent instanceof Folder ) {
        Folder folder = ( Folder )parent;
        Node node = folder.getChild( index );
        if( node != null ) {
          viewer.replace( parent, index, node );
          viewer.setChildCount( node, node.getChildCount() );
        }
      }
    }

    public void updateChildCount( Object element, int currentChildCount ) {
      if( element instanceof Node ) {
        Node node = ( Node )element;
        int childCount = node.getChildCount();
        if( childCount != currentChildCount ) {
            viewer.setChildCount( element, childCount );
        }
      }
    }

    public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
      // nothing
    }

    public void dispose() {
      // nothing
    }
  }
}
