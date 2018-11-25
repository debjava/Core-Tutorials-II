package com.novocode.naf.example.thread;

import com.novocode.naf.app.NAFApplication;
import com.novocode.naf.gui.IWindowInstanceWidget;
import com.novocode.naf.gui.event.*;
import com.novocode.naf.model.*;


/**
 * Example that shows how to have a background thread do the
 * work and update some models while the GUI stays responsive.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 3, 2004
 */

public final class ThreadExample implements Runnable
{
  private IStringListModel detailsModel = new DefaultStringListModel();
  private IObjectModel<String> msgModel = new DefaultStringModel();
  private IIntModel progressModel = new DefaultIntModel();
  private IObjectModel<String> estimateModel = new DefaultStringModel();


  public ThreadExample(ModelMap models)
  {
    models.put("details",  detailsModel);
    models.put("msg",      msgModel);
    models.put("progress", progressModel);
    models.put("estimate", estimateModel);
    models.put("close",    new DisposeWindowActionListener());
  }


  public void run()
  {
    String[] src = new String[50];
    for(int i=0; i<src.length; i++) src[i] = "img"+i+".jpg";
    long t0 = System.currentTimeMillis();

    for(int i=0; i<src.length; i++)
    {
      if(i > 0)
      {
        long timeTaken = System.currentTimeMillis() - t0;
        long timeRemaining = ((timeTaken * src.length / i) - timeTaken + 500)/1000;
        estimateModel.setValue(timeRemaining + " Seconds Remaining");
      }
      msgModel.setValue("Pretending to work with "+src[i]+"...");
      try { Thread.sleep(300); } catch(InterruptedException ignored) {}
      detailsModel.add("Nothing done with "+src[i]+".");
      progressModel.setInt(1000*(i+1)/src.length);
    }

    detailsModel.add("Finished.");
    msgModel.setValue("");
    estimateModel.setValue("");
  }    


  public static void main(String[] args) throws Exception
  {
    final NAFApplication app = new NAFApplication(ThreadExample.class);
    IWindowInstanceWidget win = app.getResourceObject("thread.naf", IWindowInstanceWidget.class);
    ModelMap models = new ModelMap();
    ThreadExample importer = new ThreadExample(models);
    new Thread(importer).start();
    app.runMainWindow(win, models);
    app.dispose();
    System.exit(0);
  }
}
