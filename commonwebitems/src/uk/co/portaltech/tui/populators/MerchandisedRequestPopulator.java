/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.Facet;
import uk.co.portaltech.travel.thirdparty.endeca.MerchandiserSearchContext;
import uk.co.portaltech.tui.web.view.data.FilterRequest;
import uk.co.portaltech.tui.web.view.data.MerchandiserRequest;
import uk.co.portaltech.tui.web.view.data.SliderRequest;


/**
 * @author manju.ts
 *
 */
public class MerchandisedRequestPopulator implements Populator<MerchandiserRequest, MerchandiserSearchContext>
{



    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final MerchandiserRequest source, final MerchandiserSearchContext target) throws ConversionException
    {
        target.setPageLabel(source.getPageLabel());
        target.setInventoryLabel(source.getInventoryLabel());
        setFilters(source, target);
    }

    /**
     *
     * @param target
     * @param source
     */
    private void setFilters(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {

        if (source.getFilters() != null)
        {
            //Pass the rating values
            setRatings(source, target);
            //Pass the Best For values
            setBestFor(source, target);

            //Pass the Holiday type values
            setHolidayType(source, target);

            //Set the featues
            setFeatures(source, target);
            //Set the destination options
            setDestinationOptions(source, target);

        }
    }



    /**
     * @param source
     * @param target
     */
    private void setRatings(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {
        //Send the Trip advisor rating
        final SliderRequest tARating = source.getFilters().getTripadvisorrating();
        if (tARating != null && tARating.isChanged())
        {
            target.gettARating().setId(tARating.getCode());
            target.gettARating().setMax(ceilValue(tARating.getMax()));
            target.gettARating().setMin(ceilValue(tARating.getMin()));
            target.gettARating().setName(tARating.getName());
        }
        //Send the First choice rating
        final SliderRequest fcRating = source.getFilters().getFcRating();
        if (fcRating != null && fcRating.isChanged())
        {
            target.getFcRating().setId(fcRating.getCode());
            target.getFcRating().setMax(fcRating.getMax());
            target.getFcRating().setMin(fcRating.getMin());
            target.getFcRating().setName(fcRating.getName());
        }
    }

    /**
     * @param source
     * @param target
     */
    private void setBestFor(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {
        Facet bestFor = null;
        final List<String> bestforValues = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(source.getFilters().getBestfor()))
        {
            bestFor = new Facet();

            for (final FilterRequest best : source.getFilters().getBestfor())
            {
                bestforValues.add(best.getValue());
            }
            bestFor.setValues(bestforValues);
            target.setBestFor(bestFor);
        }
    }

    /**
     * @param source
     * @param target
     */
    private void setHolidayType(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {
        Facet collection = null;
        final List<String> collectionValues = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(source.getFilters().getHolidayType()))
        {
            collection = new Facet();

            for (final FilterRequest holiday : source.getFilters().getHolidayType())
            {
                collectionValues.add(holiday.getValue());
            }
            collection.setValues(collectionValues);
            target.setCollections(collection);
        }
    }


    /**
     * @param source
     * @param target
     */
    private void setFeatures(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {
        final Map<String, List<String>> featuremap = new HashMap<String, List<String>>();

        Facet featureSelected = null;
        final List<String> featureValues = new ArrayList<String>();

        if (CollectionUtils.isNotEmpty(source.getFilters().getFeatures()))
        {
            featureSelected = new Facet();
            for (final FilterRequest feature : source.getFilters().getFeatures())
            {
                if (featuremap.containsKey(feature.getCategoryCode()))
                {
                    final List<String> entries = featuremap.get(feature.getCategoryCode());
                    entries.add(feature.getValue());
                }
                else
                {
                    final List<String> entries = new ArrayList<String>();
                    entries.add(feature.getValue());
                    if (feature.getCategoryCode() != null)
                    {
                        featuremap.put(feature.getCategoryCode(), entries);
                    }
                }

                //this is for features'  N values need to pass the names
                featureValues.add(feature.getName());
            }
            featureSelected.setValues(featureValues);
            target.setAccomFeatures(featureSelected);
        }
        target.setFeatures(featuremap);

    }


    /**
     * @param source
     * @param target
     */
    private void setDestinationOptions(final MerchandiserRequest source, final MerchandiserSearchContext target)
    {
        if (CollectionUtils.isNotEmpty(source.getFilters().getDestinations()))
        {
            uk.co.portaltech.travel.thirdparty.endeca.FilterRequest destinationContext = null;
            for (final FilterRequest destination : source.getFilters().getDestinations())
            {
                destinationContext = new uk.co.portaltech.travel.thirdparty.endeca.FilterRequest();
                if (destination.getParent() != null)
                {
                    destinationContext.setId(destination.getValue());
                    destinationContext.setName(destination.getName());
                    target.getDestinationOptions().add(destinationContext);
                }
            }
        }
    }

    /**
     * @param value
     *
     * @return ceiled value.
     */
    @SuppressWarnings("boxing")
    private String ceilValue(final String value) {
        if (StringUtils.isNotEmpty(value)) {
            return String.valueOf(Double.valueOf(Math.ceil(Float.parseFloat(value))).intValue());
        }
        return StringUtils.EMPTY;
    }
}
