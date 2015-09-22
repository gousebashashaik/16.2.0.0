/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author cxw
 *
 */
public class BrightCoveVideoPopulator implements Populator<Collection<MediaContainerModel>, List<MediaViewData>>
{

    @Resource
    private BrandUtils brandUtils;

    @Override
    public void populate(final Collection<MediaContainerModel> galleryVideos, final List<MediaViewData> videoDataList)
            throws ConversionException
    {
        for (final MediaContainerModel mediaContainer : galleryVideos)
        {
            final MediaViewData mediaData = new MediaViewData();
            mediaData.setCode(mediaContainer.getQualifier());
            if (CollectionUtils.isNotEmpty( mediaContainer.getBrands()))
            {
                mediaData.setBrand(brandUtils.getFeatureServiceBrand(mediaContainer.getBrands()));
            }
            videoDataList.add(mediaData);
        }
    }
}
