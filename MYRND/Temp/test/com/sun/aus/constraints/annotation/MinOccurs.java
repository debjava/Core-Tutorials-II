package com.sun.aus.constraints.annotation;

import java.lang.annotation.*;

/**
 * A constraint on the minimum number of items in a collection type 
 * property. To be valid the collection must have no less items than the value
 * specified by this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MinOccurs(8)
 * public void setStringList({@code List<String>} stringList) { ... }
 * </code>
 * </pre> 
 *
 * @author Anders Holmgren
 */
@Constraint(Constraint.Type.COLLECTION)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MinOccurs {
    /**
     * the minimum allowable number of items in the Collection
     */
    int value();
}
