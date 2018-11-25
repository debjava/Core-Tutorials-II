package com.novocode.naf.example.hello;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;


/**
 * "Hello World" example.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Dec 6, 2003
 */

public class HelloExample
{
  public static void main(String[] args)
  {
    NAFApplication app = new NAFApplication(HelloExample.class);
    app.runMainWindow(app.getResourceObject("hello.naf", IWindowInstanceWidget.class), null);
    app.dispose();
  }
}
