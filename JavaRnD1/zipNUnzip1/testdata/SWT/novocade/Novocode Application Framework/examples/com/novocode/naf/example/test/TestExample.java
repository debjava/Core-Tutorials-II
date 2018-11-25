package com.novocode.naf.example.test;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.NGWindow;
import com.novocode.naf.gui.event.ChangeEvent;
import com.novocode.naf.gui.event.IChangeListener;
import com.novocode.naf.model.*;
import com.novocode.naf.resource.*;


/**
 * Example for testing new features.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Mar 1, 2004
 */

public class TestExample
{
  public static void main(String[] args) throws Exception
  {
    long t0 = System.currentTimeMillis();
    Class.forName("org.eclipse.swt.widgets.Control");
    System.out.println("Loading SWT took "+(System.currentTimeMillis()-t0)+"ms");

    t0 = System.currentTimeMillis();
    final NAFApplication app = new NAFApplication(TestExample.class);
    System.out.println("Creating NAFApplication took "+(System.currentTimeMillis()-t0)+"ms");

    t0 = System.currentTimeMillis();
    NGWindow win = app.getResourceObject("test.naf#shell", NGWindow.class);
    System.out.println("Loading resource took "+(System.currentTimeMillis()-t0)+"ms");

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

    final NGComponent menu1 = app.getResourceObject("test.naf#dynamicMenu1", NGComponent.class);
    final NGComponent menu2 = app.getResourceObject("test.naf#dynamicMenu2", NGComponent.class);
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
