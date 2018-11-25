importPackage(com.novocode.naf.gui);

var win =
  { $: NGShell, title: 'HelloExample', resize: false, ch: [
    { $: NGLabel, text: ' Hello, World ! ', font: 'Arial Black, 0.5in' }
  ]};

NAF.runMainWindow(win);
