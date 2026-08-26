package tribefire.extension.spreadsheet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Declares the delimiter used for delimited spreadsheet data of an entity type. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SpreadsheetDelimiter {
	String globalId() default "";
	String value();
}
