/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author gagan
 *
 */
public class MediaViewDataPopulator implements Populator<MediaModel, MediaViewData> {

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(MediaModel source, MediaViewData target) throws ConversionException {
        target.setCode(source.getCode());
        target.setDescription(source.getDescription());
        target.setMainSrc(source.getURL());

        target.setAltText(source.getAltText());
        target.setMime(source.getMime());
        target.setSize(source.getMediaFormat().getName());
    }

}
