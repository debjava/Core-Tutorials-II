/**
 * A simple model binding example, similar to the JavaFX/Swing version at
 * https://openjfx.dev.java.net/Learning_More_About_JavaFX.html#borders
 */

importPackage(com.novocode.naf.gui);

var clicks = new com.novocode.naf.model.DefaultIntModel();

var win =
  { $: NGShell, width: '110%', ch: [
    { $: 'layout', type: 'grid', margin: '30px', spacing: '10px' },
    { $: NGButton, text: "|I'm a button!", ld$halign: 'fill-grab', m$action: function() { clicks.value++ } },
    { $: NGLabel, align: 'center', ld$halign: 'fill-grab', ch: [
      { $: 'model', type: 'text', prefix: 'Number of button clicks: ', bind: clicks }
    ]}
  ]};

NAF.runMainWindow(win);
