package com.novocode.naf.example.explorer;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.WindowInstance;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.jface.viewers.BackgroundTreeContentProvider;
import com.novocode.naf.model.DefaultIntModel;
import com.novocode.naf.model.DefaultObjectModel;
import com.novocode.naf.model.ModelMap;


/**
 * A complex example that shows a Windows Explorer-like shell with sidebars.
 * It also displays a tray item in the system tray (on platforms where that
 * feature is available).
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 22, 2003
 */

public class ExplorerExample
{
  private static final Object FS_ROOT = new Object();
  private static final File[] rootFiles = File.listRoots();


  public static void main(String[] args)
  {
    final NAFApplication app = new NAFApplication(ExplorerExample.class);
    IWindowInstanceWidget win = app.getResourceObject("explorer.naf", IWindowInstanceWidget.class);
    IWindowInstanceWidget tray = app.getResourceObject("tray.naf", IWindowInstanceWidget.class);

    ModelMap models = new ModelMap();
    models.put("exit", new IActionListener()
    {
      public void performAction(ActionEvent e)
      {
        e.windowInstance.application.dispose();
      }
    });
    models.put("closeWindow", new DisposeWindowActionListener());
    DefaultIntModel im = new DefaultIntModel(2);
    models.put("model1", im.getIsValueModel(1, 0));
    models.put("model2", im.getIsValueModel(2, 0));
    models.put("model3", im.getIsValueModel(3, 0));
    models.put("closeSidebar", im.getSetToValueActionListener(0));
    models.put("sidebarTitle", im.getStringReadModel(new String[] { "", "Search", "Folders", "History" }));

    models.put("treeContentProvider", new BackgroundTreeContentProvider(new ITreeContentProvider()
    {
      public Object[] getChildren(Object element)
      {
        if(element == FS_ROOT) return rootFiles;
        File[] kids = ((File)element).listFiles();
        if(kids == null) return new Object[0];
        ArrayList<File> l = new ArrayList<File>(kids.length);
        for(int i=0; i<kids.length; i++)
        {
          File f = kids[i];
          if(isRoot(f) || f.isDirectory()) l.add(f);
        }
        return l.toArray();
      }
    
      public boolean hasChildren(Object element)
      {
        if(element == FS_ROOT || isRoot((File)element)) return true;
        else return getChildren(element).length > 0;
      }
    
      public Object getParent(Object element)
      {
        if(element == FS_ROOT) return null;
        if(isRoot((File)element)) return FS_ROOT;
        else return ((File)element).getParent();
      }
      
      public Object[] getElements(Object element) { return getChildren(element); }

      public void dispose() {}
    
      public void inputChanged(Viewer viewer, Object old_input, Object new_input) {}
    }));
    
    final LabelProvider treeLabelProvider = new LabelProvider()
    {
      public String getText(Object element)
      {
        if(BackgroundTreeContentProvider.isPending(element)) return "Pending...";
        if(element == FS_ROOT) return "(Root)";
        if(isRoot((File)element)) return "Volume " + ((File)element).getAbsolutePath();
        return ((File) element).getName();
      }
    
      public Image getImage(Object element)
      {
        if(BackgroundTreeContentProvider.isPending(element)) return null;
        if(element == FS_ROOT) return null;
        if(isRoot((File)element))
          return app.getImageManager().getImage("volume.png").acquire(); // won't be released until the display gets disposed
        if (((File) element).isDirectory())
          return app.getImageManager().getImage("cfldr_obj.gif").acquire(); // won't be released until the display gets disposed
        else
          return app.getImageManager().getImage("file.gif").acquire(); // won't be released until the display gets disposed
      }
    };
    models.put("treeLabelProvider", treeLabelProvider);

    models.put("treeContent", new DefaultObjectModel<Object>(FS_ROOT));
    
    models.put("tableContentProvider", new IStructuredContentProvider()
    {
      public Object[] getElements(Object element)
      {
        if(element instanceof File[]) return (File[]) element;
        else return ((File)element).listFiles();
      }

      public void dispose() {}
    
      public void inputChanged(Viewer viewer, Object old_input, Object new_input) {}
    });
    
    models.put("tableLabelProvider", new ITableLabelProvider()
    {
      public Image getColumnImage(Object element, int columnIndex)
      {
        if(columnIndex == 0) return treeLabelProvider.getImage(element);
        return null;
      }

      public String getColumnText(Object element, int columnIndex)
      {
        if(columnIndex == 0) return treeLabelProvider.getText(element);
        else if(columnIndex == 1)
        {
          File f = (File)element;
          return f.isFile() ? String.valueOf(f.length()) : "";
        }
        else return null;
      }

      public void addListener(ILabelProviderListener listener) {}

      public void dispose() {}

      public boolean isLabelProperty(Object element, String property) { return false; }

      public void removeListener(ILabelProviderListener listener) {}
    });

    models.put("tableContent", new DefaultObjectModel<Object>(rootFiles[0]));

    WindowInstance winI = app.createInstance(win, models);
    WindowInstance trayI = app.createInstance(tray, models);
    
    winI.open();
    trayI.open(); // [TODO] Implement deferred TrayItems
    app.runApp();
    app.dispose();
  }
  
  
  private static boolean isRoot(File f)
  {
    for(int i=0; i<rootFiles.length; i++)
    {
      if(rootFiles[i].equals(f)) return true;
    }
    return false;
  }
}
