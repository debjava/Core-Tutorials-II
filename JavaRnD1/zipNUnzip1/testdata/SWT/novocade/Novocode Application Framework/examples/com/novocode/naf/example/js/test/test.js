importPackage(com.novocode.naf.gui);
importPackage(com.novocode.naf.resource);

var group =
  { $: NGGroup, ch: [

	{ $: NGShell, id: "shell", image: "default_persp.gif", minsize: false, ch: [

		{ $: 'model', type: "title", id: "model2", prefix: "NAF [", suffix: "]" },
		
		{ $: NGMenu, role: "menubar", ch:
			{ $: NGMenu, text: "Help", ch: [
				{ $: NGButton, text: "|About", 'default': true, accelerator: "Ctrl+?",
					ch: { $: NGInclude, ref: "#about", role: "open" } },
				{ $: NGInclude, ref: "#filemenu" },
				{ $: NGSeparator },
				{ $: NGGroup, m$content: "dynamicMenu" },
				{ $: NGSeparator },
				{ $: NGButton, text: "Test", image: "test.png" }
			]}
		},

		{ $: 'layout', type: "grid" },

		{ $: NGToolBar, id: "toolbar", shadow: "out", ld$halign: "fill", ld$hgrab: true, ld$hspan: 2, ch: [
			{ $: NGMenu, role: "popup", ch: { $: NGButton, text: "Customize..." } },
			{ $: NGButton, text: "Save", image: "save_edit.gif", tooltip: "Save this file" },
			{ $: NGSeparator },
			{ $: NGDropDown, text: "|Back", image: "backward_nav.gif", m$action: "backAction", ch: [
				{ $: NGGroup, m$content: "dynamicMenu" },
				{ $: NGSeparator },
				{ $: NGMenu, text: "Options", ch: [
					{ $: NGSeparator },
					{ $: NGGroup, m$content: "dynamicMenu" },
					{ $: NGSeparator },
					{ $: NGButton, text: "Clear History" }
				]}
			]},
			{ $: NGMenu, text: "Dynamic", ch: [
				{ $: NGInclude, ref: "#filemenu" },
				{ $: NGSeparator },
				{ $: NGGroup, m$content: "dynamicMenu" },
				{ $: NGSeparator },
				{ $: NGInclude, ref: "#filemenu" }
			]},
			{ $: NGSeparator },
			{ $: NGButton, text: "About this application...", ch: { $: NGInclude, ref: "#about", role: "open" } },
			{ $: NGToggle, text: "|Toggle", image: "new_page.gif", ch: { $: "model", type: "selected", id: "model1" } }
		]},

		{ $: NGSeparator, orientation: "horizontal", ld$halign: "fill", ld$hgrab: true },

		{ $: NGComposite, ld$halign: "fill", ld$hgrab: true, ch: [
			{ $: "layout", type: "grid", columns: 20, margin: "2px", spacing: "2px" },
			{ $: NGLabel, text: "Dynamic >" },
			{ $: NGGroup, m$content: "dynamicMenu" },
			{ $: NGLabel, text: "< Dynamic" },
			{ $: NGToggle, text: "Toggle", ld$hindent: "8px", m$selected: "model1" }
		]},

		{ $: NGComposite, ld$halign: "fill", ld$hgrab: true, ld$valign: "fill", ld$vgrab: true, ch: [

			{ $: 'layout', type: "grid", columns: 2, margin: "0.5em", spacing: "0.3em" },

			{ $: NGButton, text: "F|oo || &", image: "default_persp.gif", ch: [
				{ $: 'layout-data', halign: "beginning", width: "10em", valign: "fill", hgrab: true, vgrab: true },
				{ $: 'model', type: "action", id: "action1", command: "foo" },
				{ $: 'model', type: "visible", id: "model1" }
			]},
		
			{ $: NGButton, 'default': true, ch: [
				"\u00A0Default\u00A0", //[TODO] Don't trim when parsing from JS; space should be enough instead of nbsp
				{ $: "model", type: "action", id: "defaultAction" }
			]},

			{ $: NGToggle, text: "|Checkbox", m$selected: "model1" },
			
			{ $: NGText, multi: true, m$text: "model2" },

			{ $: NGLabel, text: "Hello |World!\n(with NAF)" },

			{ $: NGLabel, ch: [
				"Hello W|orld 2!", { $: "br" },
				"(with NAF)"
			]},

			{ $: NGButton, image: "test.png" },

			{ $: NGToggle, style: "show-hide", showText: " Details >> ", hideText: " << Details (much longer text) ",
				m$selected: "model1" },
			
			{ $: NGScale, max: 5, pageIncrement: 1, m$value: "scalemodel" },
			
			{ $: NGProgressBar, indeterminate: true },

			{ $: NGSlider, max: 6, m$value: "scalemodel" },

			{ $: NGProgressBar, min: 0, max: 5, m$value: "scalemodel" },

			{ $: NGComposite, ch: [
				{ $: "layout", type: "stack", m$top: "scalemodel" },
				{ $: NGButton, text: "StackLayout Button 0" },
				{ $: NGButton, text: "StackLayout Button 1" },
				{ $: NGButton, text: "StackLayout Button 2" },
				{ $: NGButton, text: "StackLayout Button 3" },
				{ $: NGButton, text: "StackLayout Button 4" },
				{ $: NGButton, text: "StackLayout Button 5" },
			]},

			{ $: NGTable, linesVisible: true, headerVisible: true, border: true, check: true, ch: [
				{ $: "column", text: "Column 1", image: "new_page.gif" },
				{ $: "column", text: "Column 2" },
				{ $: "column", text: "Column 3" },
				{ $: "layout-data", width: "20em" },
				{ $: "model", type: "content", id: "table-content" },
				{ $: "model", type: "content-provider", id: "table-content-provider" },
				{ $: "model", type: "label-provider", id: "table-label" }
			]},

			{ $: NGStyledText, readOnly: true, bgcolor: "widget-background", fgcolor: "widget-foreground", border: false,
				ld$width: "15em", m$text: "model2" },

			{ $: NGHyperlink, acceptFocus: true, href: "mailto:szeiger@novocode.com?subject: Foo%20Bar", text: "This is a lin|k" },
			
			{ $: NGLink, ch: [
				"An SWT ",
				{ $: "a", href: "mailto:szeiger@novocode.com?subject: Foo%20Bar", ch: [ "link" ] },
				" and",
				{ $: 'br' },
				"a second line"
			]},

			{ $: NGGroupBox, text: "GroupBox", ch: { $: NGLabel, text: "Hello World" } },

			{ $: NGButton, m$text: "model2" }

		]}

	]},

	{ $: NGMessageDialog, id: "about", title: "About", icon: "information", buttons: "ok",
		message: "NAF TestJSExample.\nSecond line." }, //[TODO] \n gets decoded twice, once by JS and once by the widget

	{ $: NGGroup, id: "dynamicMenu1", ch: [
		{ $: NGButton, text: "Dynamic menu 1, item 1" },
	]},

	{ $: NGGroup, id: "dynamicMenu2", ch: [
		{ $: NGButton, text: "Dynamic menu 2, item 1" },
		{ $: NGButton, text: "Dynamic menu 2, item 2" },
	]},

	{ $: NGGroup, id: "filemenu", ch: [
		{ $: NGButton, text: "|Open..." },
		NGSeparator, // shorthand for { $: NGSeparator }
		{ $: NGButton, text: "|Quit", accelerator: "Ctrl+Q" }
	]}

  ]};

NAF.define(group);
