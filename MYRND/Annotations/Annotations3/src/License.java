import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface License {
	String name();
	String notice();
	boolean redistributable();
	Trademark[] trademarks();
}