/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.tui.web.view.data.ArticleViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author gagan
 *
 */
public class ArticlePopulator implements Populator<ArticleModel, ArticleViewData> {

    @Resource
    private Converter<MediaModel, MediaViewData> mediaModelConverter;

    private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(ArticleModel articleModel, ArticleViewData articleViewData) throws ConversionException {
        articleViewData.setContent(articleModel.getContent());
        articleViewData.setPublishDate(articleModel.getPublishDate());
        articleViewData.setSource(articleModel.getSource());
        articleViewData.setTitle(articleModel.getTitle());
        articleViewData.setType(articleModel.getType() == null ? "" : articleModel.getType().getCode());
        articleViewData.setUrl(articleModel.getUrl());

        MediaContainerModel mediaContainer = articleModel.getArticleImage();

        if (mediaContainer != null) {
            articleViewData.setArticleImage(new ArrayList<MediaViewData>());
            for (MediaModel media : mediaContainer.getMedias()) {
                MediaViewData articleImageViewData = mediaModelConverter.convert(media);
                mediaViewDataPopulator.populate(media, articleImageViewData);
                articleViewData.getArticleImage().add(articleImageViewData);
            }
        }
    }

    /**
     * @return the mediaViewDataPopulator
     */
    public Populator<MediaModel, MediaViewData> getMediaViewDataPopulator() {
        return mediaViewDataPopulator;
    }

    /**
     * @param mediaViewDataPopulator
     *            the mediaViewDataPopulator to set
     */
    public void setMediaViewDataPopulator(Populator<MediaModel, MediaViewData> mediaViewDataPopulator) {
        this.mediaViewDataPopulator = mediaViewDataPopulator;
    }

}
