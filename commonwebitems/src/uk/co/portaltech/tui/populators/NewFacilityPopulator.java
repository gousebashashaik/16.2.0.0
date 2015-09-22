/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author sureshbabu.rn
 *
 */
public class NewFacilityPopulator implements Populator<AccommodationModel, AccommodationViewData>
{

    @Resource
    private BrandUtils brandUtils;


    private final List<String> accomfacilityFeatureList = Arrays.asList(new String[]
    { "pool_scene_1_new", "pool_scene_2_new", "pool_scene_3_new", "food_drink_1_new", "food_drink_2_new", "food_drink_3_new",
            "entertainment_1_new", "entertainment_2_new", "entertainment_3_new", "sport_activities_1_new", "sport_activities_2_new",
            "sport_activities_3_new", "health_beauty_1_new", "health_beauty_2_new", "health_beauty_3_new" });


    @Resource
    private FeatureService featureService;





    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData) throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");
        if (sourceModel.getType() != null)
        {

            targetData.putFeatureCodesAndValues(featureService.getValuesForFeatures(accomfacilityFeatureList, sourceModel,
                    new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));

        }
    }
}
