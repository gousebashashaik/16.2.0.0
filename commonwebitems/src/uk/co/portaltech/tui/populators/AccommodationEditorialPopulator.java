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
 * @author omonikhide
 *
 */
public class AccommodationEditorialPopulator implements Populator<AccommodationModel, AccommodationViewData>
{



    private final List<String> villaEditorialFeatureList = Arrays.asList(new String[]
    { "swimmingPool", "arrivalDay", "onlyFrom", "sleepsUpTo", "noOfBathrooms", "noOfBedrooms", "airCon", "airConInfo",
            "freeChildPlaces", "noOfFloors" });
    private final List<String> accommEditorialIntroductionFeatureList = Arrays
            .asList(new String[]
            { "roomsOverview", "facilitiesOverview", "officialRating", "noOfBuildings", "noOfFloors", "noOfrooms", "noOfLifts",
                    "onlyFrom" });
    private static final String VILLA = "villa";

    @Resource
    private FeatureService featureService;

    @Resource
    private BrandUtils brandUtils;

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
            if (sourceModel.getType().getCode().equalsIgnoreCase(VILLA))
            {
                targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(villaEditorialFeatureList, sourceModel,
                        new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
            }
            else
            {
                targetData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(accommEditorialIntroductionFeatureList,
                        sourceModel, new Date(), brandUtils.getFeatureServiceBrand(sourceModel.getBrands())));
            }
        }


    }

}
