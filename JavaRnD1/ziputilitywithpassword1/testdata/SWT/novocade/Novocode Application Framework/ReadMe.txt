Novocode Application Framework
Release 0.4, 2008-05-08

Written by Stefan Zeiger <szeiger@novocode.com>

Licensed under EPL 1.0. See epl-v10.html for licensing conditions. Third-party
libraries in the "lib" folder may have an accompanying "<libname>-LICENSE.txt"
file with specific licensing conditions for a library. The icons for the
example programs were taken from the Eclipse distribution (which is also
licensed under EPL).

Novocode Application Framework provides a layer of MVC-based GUI components on
top of SWT and JFace. A GUI is specified as a tree of NAF components which can
then be instantiated as SWT controls. More than just a one to one wrapper of
SWT component properties, NAF includes resource management (e.g. shared images
and fonts), simplified configuration (e.g. CSS-like size units for layout
managers) and model binding. Component trees can be described in a simple XML
notation or in JavaScript with an extended JSON format. Applications can also
be written entirely in JavaScript. The resource management framework can be
used to instantiate and configure your own classes with a few simple
annotations, and it is easily extended for other description or scripting
languages.

See History.txt for a list of changes in this version.

I am making this release available in the hope that other developers will be
interested in this project and may want to join me in implementing the missing
features. Of course, you're also welcome to use the NAF classes for your own
projects.

At the moment there is no public CVS repository for NAF. The latest preview
release can be found at http://www.novocode.com/naf/. The NAF zip file can be
imported as a project in Eclipse 3.3. It depends on some Eclipse plugins
(mainly SWT, JFace and UI) which you also need to import into the workspace.

Documentation in HTML format can be found in the "doc" folder. There is not
much documentation at the moment. After reading it, you should have a look at
the sample apps in the "examples" folder.

Mandatory dependencies:

  - Java SE 6 (Might work with Java 5 but I haven't tried it)

  - SWT from Eclipse 3.3 or later

  - SLF4J: If you don't care about log messages from NAF, just add
    slf4j-api.jar and slf4j-nop.jar to the classpath (22 kB total as of SLF4J
    1.5.0).

Optional dependencies:

  - org.eclipse.jface, org.eclipse.ui.forms, org.eclipse.core.commands and
    their respective dependencies, if you want to use NAF controls based on
    JFace or Forms.

  - Jaxen: Used for XPath interpretation in NAFNavigator. It is not required
    by any NAF core classes, so you can leave it out if you don't need
    NAFNavigator.

  - Rhino: Required for the JavaScript functionality in package
    com.novocode.naf.resource.js. You can leave it out if you don't use
    JavaScript with NAF.
