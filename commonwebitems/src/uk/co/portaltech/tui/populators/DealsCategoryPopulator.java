/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.travel.model.DealCollectionModel;
import uk.co.portaltech.tui.web.view.data.DealsCategoryViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;


/**
 * @author narendra.bm
 *
 */
public class DealsCategoryPopulator implements Populator<DealCollectionModel, DealsCategoryViewData>
{


    private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final DealCollectionModel source, final DealsCategoryViewData target) throws ConversionException
    {

        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setCollectionUrl(source.getCollectionUrl());
        target.setImageUrl(source.getImageUrl());
    }

    /**
     * @return the mediaViewDataPopulator
     */
    public Populator<MediaModel, MediaViewData> getMediaViewDataPopulator()
    {
        return mediaViewDataPopulator;
    }

    /**
     * @param mediaViewDataPopulator
     *           the mediaViewDataPopulator to set
     */
    public void setMediaViewDataPopulator(final Populator<MediaModel, MediaViewData> mediaViewDataPopulator)
    {
        this.mediaViewDataPopulator = mediaViewDataPopulator;
    }
}
