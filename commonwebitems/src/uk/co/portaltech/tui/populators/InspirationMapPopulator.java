/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.converters.AttractionConverter;
import uk.co.portaltech.tui.resolvers.TuiCategoryUrlResolver;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MarkerMapViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author s.consolino
 *
 */
public class InspirationMapPopulator implements Populator<List<LocationModel>, InspirationMapViewData> {

    /**
     *
     */
    private static final String INTRO1_BODY = "intro1Body";

    /**
     *
     */
    private static final String CLASSIFICATION = "classification";

    /**
     *
     */
    private static final String LOW_PER_PERSON_PRICE = "lowPerPersonPrice";

    /**
     *
     */
    private static final String LONGITUDE = "longitude";

    /**
     *
     */
    private static final String LATITUDE = "latitude";

    /**
     *
     */
    private static final String NAME = "name";





    @Resource
    private BrandUtils brandUtils;


    @Resource
    private FeatureService         featureService;

    @Resource
    private TuiCategoryUrlResolver tuiCategoryModelUrlResolver;

    @Resource
    private  MediaDataPopulator             mediaDataPopulator;

    @Resource
    private AttractionConverter    attractionConverter;

    @Resource
    private LocationSubCategoriesPopulator locationSubCategoriesPopulator;
    @Resource
    private ViewSelector viewSelector;

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(List<LocationModel> source, InspirationMapViewData target) throws ConversionException {
        Assert.notNull(source, "Converter source must not be null");
        Assert.notNull(target, "Converter target must not be null");

        List<MarkerMapViewData> markerMapViewDataList = null;
        if(CollectionUtils.isNotEmpty(target.getMarkerMapViewDataList()))
        {
            markerMapViewDataList = target.getMarkerMapViewDataList();
        }
        else
        {
            markerMapViewDataList = new ArrayList<MarkerMapViewData>();
        }

        target.setMarkupListCount(markerMapViewDataList.size());
        //List<MarkerMapViewData>
        for (LocationModel locationModel : source) {
            MarkerMapViewData marker = new MarkerMapViewData();

            // First gets the data for the main marker
            marker.setUrl(tuiCategoryModelUrlResolver.resolve(locationModel));
            getMarkerMapViewData(locationModel, marker,brandUtils.getFeatureServiceBrand(locationModel.getBrands()));

            Collection<AttractionModel> attractionsModels = locationModel.getAttractions();
            // This gets all the attractions associated with the top location
            if (attractionsModels != null && !attractionsModels.isEmpty()) {
                for (AttractionModel attractionModel : attractionsModels) {
                    AttractionViewData attractionData = attractionConverter.convert(attractionModel);
                    List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] { LATITUDE, LONGITUDE, NAME }));
                    attractionData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(featureDescriptorList, attractionModel, new Date(), null));
                    MediaModel thumbnail = attractionModel.getThumbnail();
                    if (thumbnail != null) {
                        MediaViewData mediaData = new MediaViewData();
                        mediaDataPopulator.populate(thumbnail, mediaData);
                        attractionData.setThumbnail(mediaData);
                    }
                    attractionData.setType(attractionModel.getAttractionType().toString());
                    if ("SIGHT".equalsIgnoreCase(attractionData.getType())) {
                        marker.getSights().add(attractionData);
                    } else if ("EVENT".equalsIgnoreCase(attractionData.getType())) {
                        marker.getEvents().add(attractionData);
                    } else {
                        // If it is any other type such as 'OTHER' treat it as a sight.
                        marker.getSights().add(attractionData);
                    }
                }
            }
            marker.setName(featureService.getFirstFeatureValueAsString(NAME, locationModel, new Date(),brandUtils.getFeatureServiceBrand(locationModel.getBrands())));
            // This gets the sublocations for a particular location using the sublocation populator
            LocationData locationData = new LocationData();
            locationSubCategoriesPopulator.populate(locationModel, locationData);
            marker.setLocation(locationData);
            marker.setLocations(locationData.getSubLocations());
            marker.setCode(locationModel.getCode());
            //RETAIL CODE
            markerMapViewDataList.add(marker);
        }
        target.setMarkerMapViewDataList(markerMapViewDataList);
        target.setMarkupListCount(markerMapViewDataList.size());

    }

    private MarkerMapViewData getMarkerMapViewData(LocationModel locationModel, MarkerMapViewData markerMapViewData, String brand) {

        List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] {NAME, LATITUDE, LONGITUDE, LOW_PER_PERSON_PRICE, CLASSIFICATION,INTRO1_BODY }));
        Map<String, List<Object>> features = featureService.getOptimizedValuesForFeatures(featureDescriptorList, locationModel, new Date(), brand);

        markerMapViewData.setName(getFirstFeatureValue(features.get(NAME)).toString());
        markerMapViewData.setLatitude(getFirstFeatureValue(features.get(LATITUDE)).toString());
        markerMapViewData.setLongitude(getFirstFeatureValue(features.get(LONGITUDE)).toString());
        markerMapViewData.setPrice(getFirstFeatureValue(features.get(LOW_PER_PERSON_PRICE)).toString());
        markerMapViewData.setClassification(getFirstFeatureValue(features.get(CLASSIFICATION)).toString());
          if(viewSelector.checkIsMobile())
          {
            markerMapViewData.setIntroText(getFirstFeatureValue(features.get(INTRO1_BODY)).toString());
          }
        if (locationModel.getThumbnail() != null) {
            MediaViewData mediaData = new MediaViewData();
            mediaDataPopulator.populate(locationModel.getThumbnail(), mediaData);
            markerMapViewData.setThumbnail(mediaData);
        }
        return markerMapViewData;
    }

    private Object getFirstFeatureValue(List <Object> featureValues)
    {
        if (featureValues != null && !featureValues.isEmpty())
        {
            return featureValues.get(0);
        }
        return "";
    }
}
