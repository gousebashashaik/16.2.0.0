package uk.co.tui.flights.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.tui.flights.data.ContentMediaViewData;



/**
 * This populator converts Media model to Mediaview data object.
 * 
 */
public class ContentMediaDataPopulator implements Populator<MediaModel, ContentMediaViewData>
{

	@Override
	public void populate(final MediaModel source, final ContentMediaViewData target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
		target.setMainSrc(source.getURL());
		target.setAltText(source.getAltText());
		target.setMime(source.getMime());
		target.setSize(source.getMediaFormat().getName());
		target.setMediaFormat(source.getMediaFormat().getName());
	}

}
