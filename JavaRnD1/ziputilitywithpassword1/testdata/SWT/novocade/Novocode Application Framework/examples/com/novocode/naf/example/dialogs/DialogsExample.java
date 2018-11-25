package com.novocode.naf.example.dialogs;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.event.ActionEvent;
import com.novocode.naf.gui.event.ActionMethod;
import com.novocode.naf.model.ModelMap;


/**
 * Example that shows off the various kinds of dialogs.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 8, 2003
 */

public class DialogsExample
{
  public static void main(String[] args)
  {
    NAFApplication app = new NAFApplication(DialogsExample.class);
    IWindowInstanceWidget win = app.getResourceObject("dialogs.naf", IWindowInstanceWidget.class);
    ModelMap models = new ModelMap();
    models.addAutoModels(DialogsExample.class);
    app.runMainWindow(win, models);
    app.dispose();
  }
  
  @ActionMethod
  public static void buttonSelected(ActionEvent e)
  {
    System.out.println("Dialog button \""+e.command+"\" selected.");
  }
}
