package com.sun.aus.constraints.annotation;

import java.lang.annotation.*;

/**
 * A mandatory constraint. Specifies that the property is mandatory, or in
 * other words the property value must not be null.
 * This is only useful on when applied to non primitive properties, although it
 * is harmless otherwise. The following shows an example of how to apply this
 * constraint
 * <pre>
 * <code>@Mandatory
 * public void setStringPropertyOne(String stringPropertyOne) { ... }
 * </code>
 * </pre>
 *
 * @author Anders Holmgren
 */
@Constraint
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mandatory {}
