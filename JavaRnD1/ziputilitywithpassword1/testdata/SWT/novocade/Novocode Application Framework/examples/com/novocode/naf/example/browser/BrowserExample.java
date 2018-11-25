package com.novocode.naf.example.browser;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.event.DisposeWindowActionListener;
import com.novocode.naf.gui.event.SetObjectModelValueActionListener;
import com.novocode.naf.model.*;


/**
 * Browser example.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 24, 2004
 */

public class BrowserExample
{
  private static final String HOME_URL = "http://www.novocode.com/naf";

  
  public static void main(String[] args)
  {
    final NAFApplication app = new NAFApplication(BrowserExample.class);
    IWindowInstanceWidget win = app.getResourceObject("browser.naf#shell", IWindowInstanceWidget.class);
    ModelMap models = new ModelMap();

    final DefaultStringModel urlInputModel = new DefaultStringModel(HOME_URL);
    final IObjectModel<String> urlModel = urlInputModel.getForwarderModel();

    models.put("urlinput", urlInputModel);
    models.put("url", urlModel);
    models.put("closeWindow", new DisposeWindowActionListener());
    models.put("go", new SetObjectModelValueActionListener<String>(urlModel, urlInputModel));
    models.put("home", new SetObjectModelValueActionListener<String>(urlModel, HOME_URL));

    app.runMainWindow(win, models);
    app.dispose();
  }
}
