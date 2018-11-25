package com.novocode.naf.example.xpath;

import java.util.*;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.NGWindow;
import com.novocode.naf.gui.WindowInstance;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.DefaultBooleanModel;
import com.novocode.naf.model.DefaultObjectModel;
import com.novocode.naf.model.DefaultStringModel;
import com.novocode.naf.model.ModelField;
import com.novocode.naf.model.ModelMap;
import com.novocode.naf.resource.*;
import com.novocode.naf.xml.NAFNavigator;
import com.novocode.naf.xml.NAFXPath;


/**
 * An XPath explorer which allows you to evaluate XPath expressions
 * on NAF widget trees.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Nov 18, 2004
 */

public class XPathExample
{
  // These ModelFields are added to a ModelMAp with addAutoModels() in the XPathExample constructor
  @ModelField public final DefaultStringModel expression = new DefaultStringModel("/");
  @ModelField public final DefaultBooleanModel autoExpand = new DefaultBooleanModel(true);
  @ModelField public final DefaultObjectModel<Object> treeContent = new DefaultObjectModel<Object>();

  // Null ModelFields of a concrete class with a default constructor will be instantiated automatically
  @ModelField public DefaultActionBroadcaster expandAll;

  private final NGNode base;
  private final NAFNavigator nav = NAFNavigator.getInstance();


  @ActionMethod public void evaluate(ActionEvent e)
  {
    try
    {
      NAFXPath xp = new NAFXPath(expression.getValue());
      List<?> nodes = xp.selectNodes(base);
      treeContent.setValue(nodes.toArray());
      if(autoExpand.getBoolean()) expandAll.performAction(new ActionEvent(e.source, null, e.windowInstance ));
    }
    catch(Exception ex) { treeContent.setValue(new Object[] { ex }); }
  }


  @ActionMethod public void exit(ActionEvent e)
  {
    e.windowInstance.application.dispose();
  }


  public XPathExample()
  {
    final NAFApplication app = new NAFApplication(XPathExample.class);
    NGWindow win = app.getResourceObject("gui.naf", NGWindow.class);
    this.base = win; // You can use any NGComponent here

    treeContent.setValue(new Object[] { base });

    ModelMap models = new ModelMap();
    models.addAutoModels(this);

    models.put("treeContentProvider", new ITreeContentProvider()
    {
      public Object[] getChildren(Object element)
      {
        if(element instanceof Object[]) return (Object[])element;
        if(element instanceof String) return null;
        try
        {
          Iterator<?> it = nav.getChildAxisIterator(element);
          if(it == null) return null;
          List<Object> l = new ArrayList<Object>();
          while(it.hasNext()) l.add(it.next());
          return l.toArray();
        }
        catch(Exception ex) { return null; }
      }

      public boolean hasChildren(Object element)
      {
        if(element instanceof String) return false;
        if(element instanceof Object[]) return true;
        Object[] children = getChildren(element);
        return children != null && children.length > 0;
      }

      public Object getParent(Object element)
      {
        if(element instanceof String) return treeContent.getValue();
        if(element instanceof Object[]) return null;
        try { return nav.getParentNode(element); } catch(Exception ex) { return null; }
      }

      public Object[] getElements(Object element) { return getChildren(element); }

      public void dispose() {}

      public void inputChanged(Viewer viewer, Object old_input, Object new_input) {}
    });

    models.put("treeLabelProvider", new LabelProvider()
    {
      Image fileImage = app.getImageManager().getImage("file.gif").acquire();
      Image nodeImage = app.getImageManager().getImage("bundle_obj.gif").acquire();
      Image errImage = app.getImageManager().getImage("showerr_tsk.gif").acquire();

      public String getText(Object element)
      {
        if(element instanceof Throwable) return ((Throwable)element).toString();
        if(element instanceof Object[]) return "";
        if(element instanceof NAFNavigator.Attr) return element.toString();
        if(element instanceof NAFNavigator.Document) return ((NAFNavigator.Document)element).root.getResource().getURL().toString();
        if(element instanceof NGComponent)
        {
          StringBuilder b = new StringBuilder().append(nav.getElementName(element));
          try
          {
            for(Iterator<?> it = nav.getAttributeAxisIterator(element); it != null && it.hasNext();)
            {
              Object a = it.next();
              b.append("  @").append(nav.getAttributeName(a));
              b.append("=\"").append(nav.getAttributeStringValue(a)).append("\"");
            }
          }
          catch(Exception ignored) {}
          return b.toString();
        }
        return element.toString();
      }

      public Image getImage(Object element)
      {
        if(element instanceof Throwable) return errImage;
        if(element instanceof NAFNavigator.Document) return fileImage;
        if(element instanceof NGComponent) return nodeImage;
        return null;
      }
    });

    WindowInstance wi = app.createInstance(win, models);
    evaluate(new ActionEvent(win, null, wi));
    wi.open();
    app.runApp();
    app.dispose();
  }


  public static void main(String[] args)
  {
    new XPathExample();
  }
}
