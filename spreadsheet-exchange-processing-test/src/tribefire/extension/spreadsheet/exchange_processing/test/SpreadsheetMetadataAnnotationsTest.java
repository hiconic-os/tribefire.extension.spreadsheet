package tribefire.extension.spreadsheet.exchange_processing.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.braintribe.model.generic.annotation.meta.api.MdaHandler;
import com.braintribe.model.generic.annotation.meta.api.MetaDataAnnotations;
import com.braintribe.model.generic.annotation.meta.api.analysis.MdaAnalysisContext;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.weaving.data.ProtoHasMetaData;

import tribefire.extension.spreadsheet.annotation.SpreadsheetDate;
import tribefire.extension.spreadsheet.annotation.SpreadsheetDelimiter;
import tribefire.extension.spreadsheet.annotation.SpreadsheetIdentity;
import tribefire.extension.spreadsheet.annotation.SpreadsheetNumber;
import tribefire.extension.spreadsheet.model.exchange.metadata.SpreadsheetColumnDatePatternMapping;
import tribefire.extension.spreadsheet.model.exchange.metadata.SpreadsheetColumnNumberFormatMapping;
import tribefire.extension.spreadsheet.model.exchange.metadata.SpreadsheetDataDelimiter;
import tribefire.extension.spreadsheet.model.exchange.metadata.SpreadsheetIdentityProperty;

public class SpreadsheetMetadataAnnotationsTest {

	@SpreadsheetDelimiter(";")
	private static class AnnotatedRecord {
		@SpreadsheetIdentity
		String identity() { return null; }

		@SpreadsheetDate(pattern = "dd.MM.yyyy", locale = "de_DE")
		Object date() { return null; }

		@SpreadsheetNumber(decimalSeparator = ",", groupingSeparator = ".")
		Object number() { return null; }
	}

	@Test
	public void mapsRecordDelimiter() {
		SpreadsheetDataDelimiter metadata = build(AnnotatedRecord.class.getAnnotation(SpreadsheetDelimiter.class));
		assertEquals(";", metadata.getDelimiter());
	}

	@Test
	public void mapsIdentityProperty() throws Exception {
		SpreadsheetIdentityProperty metadata = build(annotation("identity", SpreadsheetIdentity.class));
		assertFalse(metadata.getNullIsIdentifier());
	}

	@Test
	public void mapsDateFormatAndPreservesNullDefaults() throws Exception {
		SpreadsheetColumnDatePatternMapping metadata = build(annotation("date", SpreadsheetDate.class));
		assertEquals("dd.MM.yyyy", metadata.getPattern());
		assertEquals("de_DE", metadata.getLocale());
		assertNull(metadata.getDefaultTimeZone());
		assertFalse(metadata.getEmptyStringToNull());
	}

	@Test
	public void mapsNumberFormat() throws Exception {
		SpreadsheetColumnNumberFormatMapping metadata = build(annotation("number", SpreadsheetNumber.class));
		assertEquals(",", metadata.getDecimalSeparator());
		assertEquals(".", metadata.getDigitGroupingSymbol());
	}

	private <A extends Annotation> A annotation(String method, Class<A> annotationType) throws Exception {
		return AnnotatedRecord.class.getDeclaredMethod(method).getAnnotation(annotationType);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <M extends MetaData> M build(Annotation annotation) {
		MdaHandler handler = MetaDataAnnotations.registry().annoToHandler().get(annotation.annotationType());
		assertNotNull("No gmf.mda handler for " + annotation.annotationType(), handler);
		MdaAnalysisContext context = mock(MdaAnalysisContext.class);
		ProtoHasMetaData target = mock(ProtoHasMetaData.class);
		when(target.getGlobalId()).thenReturn("test-target");
		when(context.getTarget()).thenReturn(target);
		return (M) handler.buildMdList(annotation, context).get(0);
	}
}
