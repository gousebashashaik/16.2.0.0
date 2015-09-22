/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;


import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author omonikhide
 *
 */
public class MediaPopulator implements Populator<Collection<MediaContainerModel>, List<MediaViewData>> {
    @Resource
    private  MediaService                mediaService;

    @Resource
    private ImageFormatMapping  imageFormatMapping;

    @Resource
    private MediaContainerService       mediaContainerService;

    private List<String>        imageFormats;

    private static final TUILogUtils LOG = new TUILogUtils("MediaPopulator");

    @Override
    public void populate(Collection<MediaContainerModel> galleryImages, List<MediaViewData> imageDataList) throws ConversionException {
        for (final String imageFormat : imageFormats) {
            for (MediaContainerModel mediaContainer : galleryImages) {

                final String mediaFormatQualifier = imageFormatMapping.getMediaFormatQualifierForImageFormat(imageFormat);
                if (mediaFormatQualifier != null)
                {
                    final MediaFormatModel mediaFormat = mediaService.getFormat(mediaFormatQualifier);
                    if (mediaFormat != null)
                    {
                        try {
                            final MediaModel media = mediaContainerService.getMediaForFormat(mediaContainer, mediaFormat);
                            if (media != null)
                            {
                                MediaViewData mediaData = new MediaViewData();
                                mediaData.setCode(mediaContainer.getQualifier());
                                mediaData.setMainSrc(media.getURL());
                                mediaData.setMime(media.getMime());
                                mediaData.setDescription(mediaContainer.getCaption());
                                mediaData.setAltText(media.getAltText());
                                mediaData.setSize(media.getMediaFormat().getName());
                                imageDataList.add(mediaData);
                            }
                        } catch (ModelNotFoundException mne) {
                            LOG.warn("Media item not found for mogul id " + mediaContainer.getMediaMogulId(), mne);
                        }
                    }
                }

            }
        }

    }

    public List<String> getImageFormats() {
        return imageFormats;
    }

    public void setImageFormats(List<String> imageFormats) {
        this.imageFormats = imageFormats;
    }

}
