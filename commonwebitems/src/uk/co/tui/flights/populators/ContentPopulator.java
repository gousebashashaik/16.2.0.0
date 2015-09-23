package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.tui.flights.data.ContentViewData;


/**
 * This populator {@link ContentPopulator} converts contentvalue model object into content view data pbject.
 * 
 * @author sivaramya.t
 */
public class ContentPopulator implements Populator<ContentValueModel, ContentViewData>
{
	/**
	 * @Description This method is used to populate the values from content value model i.e, {@link ContentValueModel} to
	 *              content view data {@link ContentViewData}
	 * 
	 */
	@Override
	public void populate(final ContentValueModel contentValueModel, final ContentViewData contentViewdata)
			throws ConversionException
	{

		contentViewdata.setContent((String) contentValueModel.getValue());

	}
}
