package com.novocode.naf.example.splash;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.WindowInstance;
import com.novocode.naf.model.*;


/**
 * Display a splash screen.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 5, 2004
 */

public final class SplashExample implements Runnable
{
  private IObjectModel<String> msgModel = new DefaultStringModel();
  private IIntModel progressModel = new DefaultIntModel();

  private WindowInstance wi;


  public SplashExample(ModelMap models)
  {
    models.put("msg",      msgModel);
    models.put("progress", progressModel);
  }


  public void run()
  {
    for(int i=0; i<100; i++)
    {
      msgModel.setValue("Initialization stage "+(i/20+1)+" of 5...");
      try { Thread.sleep(50); } catch(InterruptedException ignored) {}
      progressModel.setInt(i+1);
    }
    wi.dispose();
  }    


  public static void main(String[] args) throws Exception
  {
    final NAFApplication app = new NAFApplication(SplashExample.class);
    ModelMap models = new ModelMap();
    SplashExample splashRunner = new SplashExample(models);
    splashRunner.wi = app.createInstance(app.getResourceObject("splash.naf", IWindowInstanceWidget.class), models);
    splashRunner.wi.open();
    new Thread(splashRunner).start();
    app.runApp();
    app.dispose();
    System.exit(0);
  }
}
