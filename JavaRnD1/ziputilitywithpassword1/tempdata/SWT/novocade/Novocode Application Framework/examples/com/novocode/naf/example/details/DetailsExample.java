package com.novocode.naf.example.details;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;


/**
 * This example demonstrates the following features:
 * <ul>
 *   <li>Toggle button</li>
 *   <li>Dynamic layout</li>
 *   <li>CLabel model</li>
 * </ul>
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 13, 2003
 */

public class DetailsExample
{
  private static final String[] items = new String[]
  {
    "NGComponent",
    "DerfaultColorModel",
    "StringModel",
    "NGWindow",
    "myClass",
    "SWTUtil"
  };


  public static void main(String[] args)
  {
    NAFApplication app = new NAFApplication(DetailsExample.class);
    ModelMap models = new ModelMap();

    models.put("items", new DefaultStringListModel(items));
    final IObjectModel<String> name = new DefaultStringModel();
    models.put("name", name);
    final DefaultCLabelModel valid = new DefaultCLabelModel();
    models.put("valid", valid);
    final IBooleanModel isOK = new DefaultBooleanModel();
    models.put("isOK", isOK);
    name.addChangeListener(new IChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        String s = name.getValue();
        if(s == null) s = "";
        if(!isJavaIdentifier(s))
        {
          valid.setTextAndImageID("Class name \""+name.getValue()+"\" must be a valid Java identifier", "error");
          isOK.setBoolean(false);
        }
        else
        {
          isOK.setBoolean(true);
          if(!Character.isUpperCase(s.charAt(0)))
            valid.setTextAndImageID("Class name \""+name.getValue()+"\" should start with an upper-case letter", "warn");
          else valid.setTextAndImageID(null, null);
        }
      }
    });

    app.runMainWindow(app.getResourceObject("details.naf", IWindowInstanceWidget.class), models);
    app.dispose();
  }
  
  
  private static boolean isJavaIdentifier(String s)
  {
    if(s == null || s.length() == 0) return false;
    if(!Character.isJavaIdentifierStart(s.charAt(0))) return false;
    for(int i=1; i<s.length(); i++)
      if(!Character.isJavaIdentifierPart(s.charAt(i))) return false;
    return true;
  }
}
