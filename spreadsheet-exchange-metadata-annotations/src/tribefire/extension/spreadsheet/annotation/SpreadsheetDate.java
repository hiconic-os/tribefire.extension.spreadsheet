package tribefire.extension.spreadsheet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.model.generic.annotation.meta.AnnotationDefaults;
import com.braintribe.model.generic.annotation.meta.NullDefault;

/** Declares how a spreadsheet column is converted to and from a date property. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
public @interface SpreadsheetDate {
	String globalId() default "";
	String pattern();

	@NullDefault
	String locale() default AnnotationDefaults.NULL_STRING;

	@NullDefault
	String defaultTimeZone() default AnnotationDefaults.NULL_STRING;

	boolean emptyStringToNull() default false;
}
