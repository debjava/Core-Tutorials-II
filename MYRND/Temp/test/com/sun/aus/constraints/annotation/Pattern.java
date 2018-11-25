package com.sun.aus.constraints.annotation;

import java.lang.annotation.*;

/**
 * A regular expression constraint for Strings. The value of the 
 * property needs to match the regular expression specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@Pattern("(\\d{4}\\-){3}\\d{4}")
 * public void setCreditCard(String creditCard) { ... }
 * </code>
 * </pre>
 * The regular expression support is the same as for the java.util.regex package
 *
 * @author Anders Holmgren
 */
@Constraint(Constraint.Type.STRING)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Pattern {
    /**
     * the pattern that the property value must conform to
     */
    String value();
}
