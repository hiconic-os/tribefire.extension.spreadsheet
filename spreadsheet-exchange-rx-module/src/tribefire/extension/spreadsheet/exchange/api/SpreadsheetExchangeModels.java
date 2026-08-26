package tribefire.extension.spreadsheet.exchange.api;

import hiconic.rx.module.api.service.ModelSymbol;
import tribefire.extension.spreadsheet._SpreadsheetExchangeApiModel_;

/**
 * @author peter.gazdik
 */
public interface SpreadsheetExchangeModels {

	ModelSymbol configuredSpreadsheetApiModel = ModelSymbol.configured(_SpreadsheetExchangeApiModel_.reflection);

}
