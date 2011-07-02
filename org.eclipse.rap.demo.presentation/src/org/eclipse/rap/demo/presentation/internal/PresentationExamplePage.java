package org.eclipse.rap.demo.presentation.internal;

import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;


public class PresentationExamplePage implements IExamplePage {
  
  private final String page;

  public PresentationExamplePage( String page ) {
    this.page = page;
  }

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 1 ) );
    createBrowser( parent, page );
  }

  private static final Browser createBrowser( Composite parent, String page ) {
    Browser browser = new Browser( parent, SWT.NO_SCROLL );
    browser.setLayoutData( ExampleUtil.createFillData() );
    browser.setUrl( "./org.eclipse.rap.demo.presentation/content/" + page );
    return browser;
  }
}
