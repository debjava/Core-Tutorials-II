package com.novocode.naf.example.js.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.NGWindow;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.DefaultBooleanModel;
import com.novocode.naf.model.DefaultItemListModel;
import com.novocode.naf.model.DefaultObjectModel;
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.ComponentDump;
import com.novocode.naf.resource.NGComponent;
import com.novocode.naf.resource.js.JSResourceLoader;


/**
 * Comprehensive test for JavaScript functionality.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Apr 23, 2008
 * @version $Id: TestJSExample.java 417 2008-05-06 22:27:03Z szeiger $
 */

public class TestJSExample
{
  public static void main(String[] args)
  {
    /*Logger l = Logger.getLogger("com.novocode.naf.resource.js");
    l.setLevel(Level.ALL);
    l.setUseParentHandlers(false);
    ConsoleHandler h = new ConsoleHandler();
    h.setLevel(Level.ALL);
    h.setFormatter(new SingleLineFormatter());
    l.addHandler(h);*/

    final NAFApplication app = new NAFApplication(TestJSExample.class);
    JSResourceLoader.addTo(app);
    NGWindow win = app.getResourceObject("test.js#shell", NGWindow.class);

    ComponentDump.dump(win, System.out, "");
    ModelMap models = new ModelMap();

    models.put("model2", new DefaultStringModel("TestExample"));

    models.put("table-content-provider", new IStructuredContentProvider()
    {
      public Object[] getElements(Object element)
      {
        return (Object[])element;
      }

      public void dispose() {}
    
      public void inputChanged(Viewer viewer, Object old_input, Object new_input) {}
    });
    
    models.put("table-label", new ITableLabelProvider()
    {
      public Image getColumnImage(Object element, int columnIndex)
      {
        if(columnIndex == 0)
          return app.getImageManager().getImage("test.png").acquire(); // won't be released until the display gets disposed
        else return null;
      }

      public String getColumnText(Object element, int columnIndex)
      {
        String[] a = (String[])element;
        return a[columnIndex];
      }

      public void addListener(ILabelProviderListener listener) {}

      public void dispose() {}

      public boolean isLabelProperty(Object element, String property) { return false; }

      public void removeListener(ILabelProviderListener listener) {}
    });

    models.put("table-content", new DefaultObjectModel<Object>(new String[][]
    {
      new String[] { "l1c1", "l1c2", "l1c3" },
      new String[] { "l2c1", "l2c2", "l2c3" },
      new String[] { "l3c1", "l3c2", "l3c3" }
    }));

    final NGComponent menu1 = app.getResourceObject("test.js#dynamicMenu1", NGComponent.class);
    final NGComponent menu2 = app.getResourceObject("test.js#dynamicMenu2", NGComponent.class);
    final DefaultItemListModel mm = new DefaultItemListModel();
    mm.set(new NGComponent[] { menu1 });
    models.put("dynamicMenu", mm);

    final DefaultBooleanModel model1 = new DefaultBooleanModel();
    models.put("model1", model1);
    model1.addChangeListener(new IChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        mm.set(new NGComponent[] { model1.getBoolean() ? menu2 : menu1 });
      }
    });
 
    app.runMainWindow(win, models);
    app.dispose();
  }
}
