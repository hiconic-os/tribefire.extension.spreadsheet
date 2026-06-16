package tribefire.extension.spreadsheet.exchange.wire.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;

import hiconic.rx.access.module.api.AccessContract;
import hiconic.rx.access.module.api.AccessServiceModelConfiguration;
import hiconic.rx.module.api.service.ModelConfiguration;
import hiconic.rx.module.api.service.ModelConfigurations;
import hiconic.rx.module.api.wire.RxModuleContract;
import hiconic.rx.module.api.wire.RxPlatformContract;
import hiconic.rx.scripting.api.ScriptingContract;
import tribefire.extension.spreadsheet._SpreadsheetExchangeApiModel_;
import tribefire.extension.spreadsheet.exchange.processing.service.SpreadsheetExchangeProcessor;
import tribefire.extension.spreadsheet.model.exchange.api.request.SpreadsheetExchangeRequest;

/**
 * This module:
 * <ul>
 * <li>Registers a configuration model for {@link _SpreadsheetExchangeApiModel_}.
 * <li>On this model binds {@link SpreadsheetExchangeProcessor} as a processor for {@link SpreadsheetExchangeRequest}.
 * </ul>
 */
@Managed
public class SpreadsheetExchangeRxModuleSpace implements RxModuleContract {

	@Import
	private RxPlatformContract platform;

	@Import
	private ScriptingContract scripting;

	@Import
	private AccessContract access;

	@Override
	public void configureModels(ModelConfigurations configurations) {
		ModelConfiguration configuredModel = configurations.configuredModel(_SpreadsheetExchangeApiModel_.reflection);
		AccessServiceModelConfiguration serviceModelConfig = access.accessModelConfigurations().serviceModelConfiguration(configuredModel);

		serviceModelConfig.bindAccessRequest(SpreadsheetExchangeRequest.T, this::spreadsheetExchangeProcessor);
	}

	@Managed
	private SpreadsheetExchangeProcessor spreadsheetExchangeProcessor() {
		SpreadsheetExchangeProcessor bean = new SpreadsheetExchangeProcessor();
		bean.setEngineResolver(scripting.scriptingEngineResolver());
		bean.setStreamPipeFactory(platform.transientData().streamPipeFactory());
		return bean;
	}

}