package com.novocode.naf.example.ishell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.novocode.naf.gui.layout.FormDataCreator;
import com.novocode.naf.swt.custom.CustomSeparator;
import com.novocode.naf.swt.custom.ScaledImage;
import com.novocode.naf.swt.custom.ishell.DesktopForm;
import com.novocode.naf.swt.custom.ishell.InternalShell;


public class InternalShellExampleSWT
{
  private static int nextWindowNumber = 1;
  private static Image iconImage;

  
  public static void main(String[] args)
  {
    Display display = new Display();
    iconImage = new Image(display, InternalShellExampleSWT.class.getResourceAsStream("ishell.png"));
    Image backgroundImage = new Image(display, InternalShellExampleSWT.class.getResourceAsStream("IMG_2006-a-fixed-seamless.jpg"));

    Shell shell = new Shell(display);
    shell.setImage(iconImage);
    FormLayout layout = new FormLayout();
    layout.spacing = 0;
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    shell.setLayout(layout);
    shell.setText("Internal Shell Example (SWT)");

    CustomSeparator sep = new CustomSeparator(shell, SWT.HORIZONTAL);
    sep.setLayoutData(FormDataCreator.createFormData(sep, "0, 1, 0"));

    final DesktopForm desktop = new DesktopForm(shell, SWT.NONE);
    desktop.setShowMaximizedTitle(true);
    desktop.setLayoutData(FormDataCreator.createFormData(desktop, "0, 1, :prev, 1"));
    
    // Add a background image to the DesktopForm
    final ScaledImage si = new ScaledImage(desktop, SWT.NONE);
    si.setImage(backgroundImage);
    si.setImagePlacement(ScaledImage.IMAGE_PLACEMENT_TILE);
    // Note: Don't set a layout on a DesktopForm
    si.setLocation(0, 0);
    si.setSize(desktop.getSize());
    desktop.addListener(SWT.Resize, new Listener()
    {
      public void handleEvent(Event event)
      {
        si.setSize(desktop.getSize());
      }
    });

    Menu bar = new Menu(shell, SWT.BAR);
    shell.setMenuBar(bar);

    MenuItem createItem = new MenuItem(bar, SWT.CASCADE);
    createItem.setText("&Create");
    Menu createMenu = new Menu(shell, SWT.DROP_DOWN);
    createItem.setMenu(createMenu);
    createNewWindowItem(desktop, createMenu, SWT.SHELL_TRIM, true, false);
    createNewWindowItem(desktop, createMenu, SWT.SHELL_TRIM, true, true);
    createNewWindowItem(desktop, createMenu, SWT.SHELL_TRIM, false, false);
    createNewWindowItem(desktop, createMenu, SWT.DIALOG_TRIM, false, false);
    createNewWindowItem(desktop, createMenu, SWT.RESIZE|SWT.MAX, true, false);
    createNewWindowItem(desktop, createMenu, SWT.CLOSE|SWT.RESIZE|SWT.ON_TOP, true, false);
    createNewWindowItem(desktop, createMenu, SWT.CLOSE|SWT.MAX, false, false);
    createNewWindowItem(desktop, createMenu, SWT.CLOSE|SWT.RESIZE, true, false);
    createNewWindowItem(desktop, createMenu, SWT.CLOSE, false, false);
    createNewWindowItem(desktop, createMenu, SWT.RESIZE, false, false);
    createNewWindowItem(desktop, createMenu, SWT.MAX, false, false);
    createNewWindowItem(desktop, createMenu, SWT.MIN, false, false);
    createNewWindowItem(desktop, createMenu, SWT.TOOL | SWT.CLOSE, false, false);
    createNewWindowItem(desktop, createMenu, SWT.NONE, false, false);
    final MenuItem windowItem = new MenuItem(bar, SWT.CASCADE);
    windowItem.setText("&Window");
    final Menu windowMenu = new Menu(shell, SWT.DROP_DOWN);
    windowItem.setMenu(windowMenu);
    windowMenu.addListener(SWT.Show, new Listener()
    {
      public void handleEvent(Event event)
      {
        for(MenuItem i : windowMenu.getItems()) i.dispose();
        InternalShell[] is = desktop.getShells();

        final MenuItem showMaxTitleItem = new MenuItem(windowMenu, SWT.CHECK);
        showMaxTitleItem.setSelection(desktop.getShowMaximizedTitle());
        showMaxTitleItem.setText("Show &Title of Maximized Windows");
        showMaxTitleItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            desktop.setShowMaximizedTitle(showMaxTitleItem.getSelection());
          }
        });
        final MenuItem autoMaxTitleItem = new MenuItem(windowMenu, SWT.CHECK);
        autoMaxTitleItem.setSelection(desktop.getAutoMaximize());
        autoMaxTitleItem.setText("&Auto-Maximize Windows");
        autoMaxTitleItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            desktop.setAutoMaximize(autoMaxTitleItem.getSelection());
          }
        });
        final MenuItem enableCtrlTabTitleItem = new MenuItem(windowMenu, SWT.CHECK);
        enableCtrlTabTitleItem.setSelection(desktop.getEnableCtrlTab());
        enableCtrlTabTitleItem.setText("&Enable Ctrl+Tab");
        enableCtrlTabTitleItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            desktop.setEnableCtrlTab(enableCtrlTabTitleItem.getSelection());
          }
        });
        final MenuItem allowDeactivateItem = new MenuItem(windowMenu, SWT.CHECK);
        allowDeactivateItem.setSelection(desktop.getAllowDeactivate());
        allowDeactivateItem.setText("Allow &Deactivate");
        allowDeactivateItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            desktop.setAllowDeactivate(allowDeactivateItem.getSelection());
          }
        });
        final MenuItem showBackgroundImageItem = new MenuItem(windowMenu, SWT.CHECK);
        showBackgroundImageItem.setSelection(si.getVisible());
        showBackgroundImageItem.setText("Show Background &Image");
        showBackgroundImageItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            si.setVisible(showBackgroundImageItem.getSelection());
          }
        });

        new MenuItem(windowMenu, SWT.SEPARATOR);

        final InternalShell active = desktop.getActiveShell();
        MenuItem closeItem = new MenuItem(windowMenu, SWT.PUSH);
        boolean forceClose = (active == null) ? false : ((active.getStyle() & SWT.CLOSE) == 0);
        closeItem.setText("&Close Active Window" + (forceClose ? " (Force)" : ""));
        if(active == null) closeItem.setEnabled(false);
        else closeItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            active.close();
          }
        });
        final boolean maximized = (active == null) ? false : active.getMaximized();
        MenuItem maxResItem = new MenuItem(windowMenu, SWT.PUSH);
        maxResItem.setText(maximized ? "&Restore Active Window" : "&Maximize Active Window");
        if(active == null || (active.getStyle() & SWT.MAX) == 0) maxResItem.setEnabled(false);
        else maxResItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            active.setMaximized(!maximized);
          }
        });
        MenuItem minItem = new MenuItem(windowMenu, SWT.PUSH);
        minItem.setText("Mi&nimize Active Window");
        if(active == null || (active.getStyle() & SWT.MIN) == 0) minItem.setEnabled(false);
        else minItem.addListener(SWT.Selection, new Listener()
        {
          public void handleEvent(Event event)
          {
            active.setMinimized(true);
          }
        });

        if(is.length > 0) new MenuItem(windowMenu, SWT.SEPARATOR);

        for(int i=0; i<is.length; i++)
        {
          final InternalShell ishell = is[i];
          MenuItem menuItem = new MenuItem(windowMenu, SWT.CHECK);
          String title = ishell.getText();
          if(i <= 8) title = "&" + (char)('0'+i+1) + ' ' + title;
          if(!ishell.isVisible()) title += " (Minimized)";
          menuItem.setText(title);
          if(ishell == active) menuItem.setSelection(true);
          menuItem.addListener(SWT.Selection, new Listener()
          {
            public void handleEvent(Event event)
            {
              if(!ishell.isDisposed()) ishell.setActive();
            }
          });
        }
      }
    });

    /*desktop.addDesktopListener(new DesktopListener()
    {
      public void shellCreated(Event event)
      {
      }

      public void shellDisposed(Event event)
      {
      }

      public void shellActivated(Event event)
      {
      }
    });*/

    createInternalShell(desktop, SWT.SHELL_TRIM, true, false);

    shell.setSize(700, 500);
    shell.open();
    desktop.setFocus();

    while(!shell.isDisposed())
    {
      if(!display.readAndDispatch()) display.sleep();
    }

    iconImage.dispose();
    display.dispose();
  }


  private static void createNewWindowItem(final DesktopForm desktop, final Menu menu,
    final int style, final boolean sizeGrip, final boolean customMenu)
  {
    MenuItem newWindowItem = new MenuItem(menu, SWT.PUSH);
    newWindowItem.setText(styleString(style, sizeGrip, customMenu));
    newWindowItem.addListener(SWT.Selection, new Listener()
    {
      public void handleEvent(Event e)
      {
        createInternalShell(desktop, style, sizeGrip, customMenu);
      }
    });
  }
  
  
  private static String styleString(int style, boolean sizeGrip, boolean customMenu)
  {
    StringBuffer b = new StringBuffer();
    if((style & SWT.CLOSE) != 0) b.append("|CLOSE");
    if((style & SWT.RESIZE) != 0) b.append("|RESIZE");
    if((style & SWT.MAX) != 0) b.append("|MAX");
    if((style & SWT.MIN) != 0) b.append("|MIN");
    if((style & SWT.TOOL) != 0) b.append("|TOOL");
    if((style & SWT.ON_TOP) != 0) b.append("|ON_TOP");
    String styleStr;
    if(b.length() == 0) styleStr = "NONE";
    else styleStr = b.substring(1);
    if(sizeGrip) styleStr = styleStr + ", SizeGrip";
    if(customMenu) styleStr = styleStr + ", Custom Menu";
    return styleStr;
  }
  
  
  private static InternalShell createInternalShell(DesktopForm desktop, int style, boolean sizeGrip, boolean customMenu)
  {
    int windowNumber = nextWindowNumber++;
    InternalShell ishell = new InternalShell(desktop, style);
    ishell.setImage(iconImage);
    ishell.setText("Internal Shell "+windowNumber);

    if(customMenu)
    {
      Menu menu = new Menu(ishell);
      MenuItem item = new MenuItem(menu, SWT.PUSH);
      item.setText("Custom Menu Item");
      ishell.setCustomMenu(menu);
    }

    Composite ishellContent = ishell.getContentPane();
    ishellContent.setLayout(new FormLayout());
    CLabel label = new CLabel(ishellContent, SWT.NONE);
    label.setText(windowNumber+". "+styleString(style, sizeGrip, customMenu));
    Button b1 = new Button(ishellContent, SWT.NONE);
    b1.setText(" 1 ");
    Button b2 = new Button(ishellContent, SWT.NONE);
    b2.setText(" 2 ");
    Button b3 = new Button(ishellContent, SWT.NONE);
    b3.setText(" 3 ");

    new FormDataCreator(ishellContent)
      .layout(label, "4px,-4px,4px,:next-4px")
      .layout(b1, "4px,,,-4px")
      .layout(b2, ":prev+4px,,,-4px")
      .layout(b3, ":prev+4px,,,-4px");

    if(sizeGrip) ishell.createSizeGrip(SWT.NONE);

    ishell.pack();
    ishell.open();
    return ishell;
  }
}
