/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.BoardBasisContentModel;
import uk.co.portaltech.travel.model.TransferTimeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.model.ItineraryLeg;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author narendra.bm
 *
 */
public class EssentialInfoPopulator<T> implements Populator<List<AccommodationModel>, List<ItineraryLeg>>
{

    @Resource
    private FeatureService featureService;
    @Resource
    private BrandUtils brandUtils;

    private final List<String> featureDescriptorListForAccom = new ArrayList(Arrays.asList(new String[]
    { "name", "officialRating", "tRating", "noOfrooms", "noOfFloors", "distanceFromAirport" }));
    private final List<String> featureDescriptorListForTransferTime = new ArrayList(Arrays.asList(new String[]
    { "primaryTransferMode", "transferTime" }));
    private final List<String> featureDescriptorListForBoardBasis = new ArrayList(Arrays.asList(new String[]
    { "meals_description", "drinks_description", "snacks_description" }));

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final List<AccommodationModel> source, final List<ItineraryLeg> target) throws ConversionException
    {
        ItineraryLeg itineraryLeg = null;
        for (final AccommodationModel accomModel : source)
        {
            itineraryLeg = new ItineraryLeg();
            populateFeaturesForAccom(itineraryLeg, accomModel);
            populateTransferTime(itineraryLeg, accomModel);
            populateBoardBasis(itineraryLeg, accomModel);
            populateShipFirst((List<T>) target, itineraryLeg, accomModel.getType().getCode());
            itineraryLeg.setTitle(itineraryLeg.getFeatureValue("name"));
            itineraryLeg.getFeatureCodesAndValues().remove("name");
        }
    }

    /**
     * @param itineraryLeg
     * @param accomModel
     */
    private void populateFeaturesForAccom(final ItineraryLeg itineraryLeg, final AccommodationModel accomModel)
    {
        itineraryLeg.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorListForAccom, accomModel,
                new Date(), brandUtils.getFeatureServiceBrand(accomModel.getBrands())));
    }

    /**
     * @param itineraryLeg
     * @param accomModel
     */
    private void populateTransferTime(final ItineraryLeg itineraryLeg, final AccommodationModel accomModel)
    {
        final Collection<TransferTimeModel> transferTimes = accomModel.getTransferTimes();
        final List<TransferTimeModel> timeModels = new ArrayList<TransferTimeModel>();
        if (CollectionUtils.isNotEmpty(transferTimes))
        {
            for (final TransferTimeModel timeModel : transferTimes)
            {
                timeModels.add(timeModel);
            }
            itineraryLeg.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorListForTransferTime,
                    timeModels.get(0), new Date(), null));
            itineraryLeg.putFeatureValue("airportName", airportName(timeModels.get(0)));
        }
    }

    /**
     * @param itineraryLeg
     * @param accomModel
     */
    private void populateBoardBasis(final ItineraryLeg itineraryLeg, final AccommodationModel accomModel)
    {
        final Collection<BoardBasisContentModel> boardBasisContent = accomModel.getBoardBasisContents();
        final List<BoardBasisContentModel> boadBasisModels = new ArrayList<BoardBasisContentModel>();
        if (CollectionUtils.isNotEmpty(boardBasisContent))
        {
            for (final BoardBasisContentModel bbModel : boardBasisContent)
            {
                boadBasisModels.add(bbModel);
            }
            itineraryLeg.putFeatureCodesAndValues(featureService.getValuesForFeatures(featureDescriptorListForBoardBasis,
                    boadBasisModels.get(0), new Date(), null));

        }
    }

    /**
     * @param itineraryLegs
     * @param itineraryLeg
     * @param code
     */
    private void populateShipFirst(final List<T> itineraryLegs, final ItineraryLeg itineraryLeg, final String code)
    {
        if (StringUtils.equalsIgnoreCase(AccommodationType.SHIP.getCode(), code))
        {
            itineraryLegs.add(0, (T) itineraryLeg);
        }
        else
        {
            itineraryLegs.add((T) itineraryLeg);
        }
    }

    private List<Object> airportName(final TransferTimeModel transferTimeModel)
    {
        final List<Object> result = new ArrayList<Object>();
        result.add(transferTimeModel.getAirportName());
        return result;
    }

}
