package tribefire.extension.spreadsheet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks a property as an identity column when importing spreadsheet records. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface SpreadsheetIdentity {
	String globalId() default "";
	boolean nullIsIdentifier() default false;
}
