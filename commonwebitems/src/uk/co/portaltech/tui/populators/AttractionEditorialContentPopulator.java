/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;

/**
 * @author s.consolino
 *
 */
public class AttractionEditorialContentPopulator implements Populator<ItemModel, AttractionViewData> {

    private static final String             FEATURE_DESCRIPTOR_EDITORIAL_CONTENT = "editorialContent";

    @Resource
    private FeatureService                  featureService;

    @Resource
    private TUIUrlResolver<AttractionModel> attractionUrlResolver;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final ItemModel source, final AttractionViewData target) throws ConversionException
    {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        target.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(Arrays.asList(new String[]
        { "name", "latitude", "longitude", "editorialContent" }), source, new Date(), null));

        target.setEditorialContent(target.getFeatureValue(FEATURE_DESCRIPTOR_EDITORIAL_CONTENT));

        if (source instanceof AttractionModel)
        {
            final AttractionModel attraction = (AttractionModel) source;
            target.setName(attraction.getName());
            target.setUrl(attractionUrlResolver.resolve(attraction));
        }

    }
}
