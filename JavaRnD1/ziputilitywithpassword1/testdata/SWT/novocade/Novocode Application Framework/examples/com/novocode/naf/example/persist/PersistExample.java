package com.novocode.naf.example.persist;

import java.io.StringWriter;

import org.w3c.dom.Document;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;
import com.novocode.naf.persist.PersistenceManager;
import com.novocode.naf.xml.DOMUtil;


/**
 * This example demonstrates model persistence.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 26, 2004
 */

public class PersistExample
{
  private static final String NO_STATE_INFO = "No saved state.";

  public static void main(String[] args)
  {
    final NAFApplication app = new NAFApplication(PersistExample.class);
    final IWindowInstanceWidget mainNGC = app.getResourceObject("persist.naf#main", IWindowInstanceWidget.class);
    final IWindowInstanceWidget persistNGC = app.getResourceObject("persist.naf#persist", IWindowInstanceWidget.class);
    final IWindowInstanceWidget errorNGC = app.getResourceObject("persist.naf#error", IWindowInstanceWidget.class);
    final PersistenceManager pm = new PersistenceManager();

    ModelMap models = new ModelMap();

    /* This model contains the XML persistence data which
     * would usually be stored to a file or database system. */
    final DefaultStringModel data = new DefaultStringModel(NO_STATE_INFO);
    models.put("data", data);

    models.put("reset", data.getSetToValueActionListener(NO_STATE_INFO));

    models.put("open", new IActionListener()
    {
      public void performAction(ActionEvent e)
      {
        ModelMap models2 = new ModelMap();
        models2.put("window", new DefaultWindowStateModel());
        models2.put("weights", new DefaultObjectModel<int[]>());
        models2.put("page", new DefaultIntModel());

        boolean cancelled = false;
        String persisted = data.getValue();
        if(!NO_STATE_INFO.equals(persisted))
        {
          try
          {
            pm.restoreFromDOM(models2, DOMUtil.parseDocument(persisted));
          }
          catch(Exception ex)
          {
            ModelMap errModels = new ModelMap();
            errModels.put("msg", new DefaultStringModel(ex.toString()));
            app.createInstance(errorNGC, errModels, e.windowInstance).open();
            cancelled = true;
          }
        }

        if(!cancelled)
        {
          models2.put("close", new IActionListener()
          {
            public void performAction(ActionEvent e)
            {
              Document doc = pm.createDOM(e.windowInstance.getPersistModels());
              StringWriter wr = new StringWriter();
              DOMUtil.dumpNode(doc, wr);
              data.setValue(wr.toString());
              e.windowInstance.dispose();
            }
          });
  
          app.createInstance(persistNGC, models2, e.windowInstance).open();
        }
      }
    });

    app.runMainWindow(mainNGC, models);
    app.dispose();
  }
}
