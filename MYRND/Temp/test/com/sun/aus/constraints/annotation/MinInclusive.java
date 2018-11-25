package com.sun.aus.constraints.annotation;

import java.lang.annotation.*;

/**
 * A minimum value constraint for numerics. Constrains numerical properties
 * to being no less in value than that specified for this constraint.
 * The following shows an example of how to apply this constraint.
 * <pre>
 * <code>@MinInclusive(23.45692)
 * public void setADouble(double aDouble) { ... }
 * </code>
 * </pre> 
 * <p>
 * Note that the minimum value is stored as a double regardless of what type
 * of numeric this constraint is applied to. Values of type long, that exceed
 * the precision of double will lose information during the conversion to double.
 * </p> 
 *
 * @author Anders Holmgren
 */
@Constraint(Constraint.Type.NUMERIC)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MinInclusive {
    /**
     * the minimum allowable value for the numerical value of the
     * property that the constraint is associated with.
     */
    double value();
}
