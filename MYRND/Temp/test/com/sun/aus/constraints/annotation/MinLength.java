package com.sun.aus.constraints.annotation;

import java.lang.annotation.*;

/**
 * A minimum length constraint for Strings. Specifies that the String must
 * contain no less than the number of characters specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MinLength(4)
 * public void setStringPropertyThree(String stringPropertyThree) { ... }
 * </code>
 * </pre> 
 *
 * @author Anders Holmgren
 */
@Constraint(Constraint.Type.STRING)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MinLength {
    /**
     * the minimum allowable number of characters in the String
     */
    int value();
}
