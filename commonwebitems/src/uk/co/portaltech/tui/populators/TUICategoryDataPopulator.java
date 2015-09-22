/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author gagan
 *
 */
public class TUICategoryDataPopulator
        implements
            Populator<CategoryModel, TUICategoryData> {

    @Resource
    private Converter<CategoryModel, TUICategoryData> tuiCategoryDataConverter;

    @Resource
    private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

    @Resource
    private FeatureService featureService;

    @Resource
    private SessionService sessionService;

    @Resource
    private LocationService tuiLocationService;

    @Resource
    private BrandUtils brandUtils;

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final CategoryModel sourceModel,
            final TUICategoryData targetData) throws ConversionException {

        targetData.setCode(sourceModel.getCode());
        targetData.setName(sourceModel.getName());
        targetData.setDescription(sourceModel.getDescription());

        if (!LocationType.CONTINENT.getCode().equals(
                ((LocationModel) sourceModel).getType().getCode())) {
            targetData.setLocationAccomTypes(tuiLocationService
                    .getLocationAccomTypes(sourceModel.getCode()));
        }

        final String targetDataURL = tuiCategoryModelUrlResolver
                .resolve(sourceModel);
        targetData.setUrl(targetDataURL);
        final BrandDetails brandDetails = (BrandDetails) sessionService
                .getAttribute(CommonwebitemsConstants.BRAND_DETAILS);

        final List<String> featureDescriptorList = new ArrayList(
                Arrays.asList(new String[]{LONGITUDE, LATITUDE}));
        final Map<String, List<Object>> features = featureService
                .getValuesForFeatures(featureDescriptorList, sourceModel,
                        new Date(),
                        brandUtils.getFeatureServiceBrand(sourceModel
                                .getBrands()));

        targetData.setLatitude(getFirstFeatureValue(features.get(LATITUDE))
                .toString());
        targetData.setLongitude(getFirstFeatureValue(features.get(LONGITUDE))
                .toString());

        final List<CategoryModel> subcategories = sourceModel.getCategories();
        if (CollectionUtils.isNotEmpty(subcategories)) {
            final List<LocationModel> locationModels = new ArrayList<LocationModel>();
            final List<String> relevantBrands = brandDetails
                    .getRelevantBrands();
            for (final CategoryModel catModel : subcategories) {
                if (catModel instanceof LocationModel) {
                    locationModels.add((LocationModel) catModel);
                }
            }

            final List<LocationModel> filteredLocations = tuiLocationService
                    .getLocationsFilteredByBrand(locationModels, relevantBrands);

            final List<TUICategoryData> subCategoryDataList = new ArrayList<TUICategoryData>();
            for (final CategoryModel subCategoryModel : filteredLocations) {
                final TUICategoryData tuiCategoryData = tuiCategoryDataConverter
                        .convert(subCategoryModel);
                populate(subCategoryModel, tuiCategoryData);
                subCategoryDataList.add(tuiCategoryData);
            }
            targetData.setCategories(subCategoryDataList);
        }

    }

    /**
     * Returns the first value from the list
     *
     * @param featureValues
     * @return object
     */
    private Object getFirstFeatureValue(final List<Object> featureValues) {
        if (featureValues != null && !featureValues.isEmpty()) {
            return featureValues.get(0);
        }
        return "";
    }

}
