package com.novocode.naf.example.bars;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.model.*;


/**
 * MenuBar and ToolBar example.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 22, 2003
 */

public class BarsExample
{
  public static void main(String[] args)
  {
    final NAFApplication app = new NAFApplication(BarsExample.class);
    IWindowInstanceWidget win = app.getResourceObject("bars.naf#shell", IWindowInstanceWidget.class);
    ModelMap models = new ModelMap();
    models.put("model2", new DefaultStringModel("BarsExample"));
    app.runMainWindow(win, models);
    app.dispose();
  }
}
