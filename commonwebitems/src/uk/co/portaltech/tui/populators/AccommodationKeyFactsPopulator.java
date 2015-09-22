/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author omonikhide
 *
 */
public class AccommodationKeyFactsPopulator implements Populator<AccommodationModel, AccommodationViewData>
{
    @Resource
    private FeatureService featureService;

    @Resource
    private I18NService i18NService;

    private static final String LANGUAGE = "language";

    @Resource
    private BrandUtils brandUtils;

    @Override
    public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData)
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[]
        { LANGUAGE, "currency", "flightDurationFromUk", "transferTime", "timezone" }));
        targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, sourceModel, new Date(),
                brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
        final String languageCode = featureService.getFirstFeatureValueAsString("language", sourceModel, new Date(),
                brandUtils.getFeatureServiceBrand(sourceModel.getBrands()));
        if (StringUtils.isNotBlank(languageCode))
        {
            final Locale locale = new Locale(languageCode);
            final List<Object> valueList = new ArrayList<Object>();
            valueList.add(locale.getDisplayLanguage(i18NService.getCurrentLocale()));
            targetData.putFeatureValue(LANGUAGE, valueList);
        }

    }
}
