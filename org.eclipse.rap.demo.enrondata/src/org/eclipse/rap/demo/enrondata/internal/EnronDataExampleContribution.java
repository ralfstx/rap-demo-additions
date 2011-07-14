package org.eclipse.rap.demo.enrondata.internal;

import org.eclipse.rap.examples.IExampleContribution;
import org.eclipse.rap.examples.IExamplePage;


public class EnronDataExampleContribution implements IExampleContribution {

  public String getId() {
    return "complex-data";
  }

  public String getTitle() {
    return "Complex Data";
  }

  public IExamplePage createPage() {
    return new EnronExamplePage();
  }
}
