package tribefire.extension.spreadsheet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Declares the decimal and grouping symbols used by a numeric spreadsheet column. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface SpreadsheetNumber {
	String globalId() default "";
	String decimalSeparator();
	String groupingSeparator();
}
