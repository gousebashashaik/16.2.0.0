/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;

/**
 * @author Charles Wilkinson
 *
 */
public class AttractionUrlResolver extends TUIUrlResolver<ItemModel> {


    @Override
    public String resolve(ItemModel source) {


        String name = getFeatureService().getFirstFeatureValueAsString("name", source, new Date(), null);
        String fallBackName = StringUtils.EMPTY;
        String code = StringUtils.EMPTY;

        if (source instanceof AttractionModel) {
            AttractionModel attraction = (AttractionModel) source;
            fallBackName = attraction.getName();
            code = attraction.getCode();
        } else if (source instanceof ExcursionModel) {
            ExcursionModel excursion = (ExcursionModel) source;
            fallBackName = excursion.getName();
            code = excursion.getCode();
        }
        if (StringUtils.isBlank(name)) {
            name = fallBackName;
        }

        String url = pattern.replace("{category-path}", buildPathString(getCategoryPath(source)));
        if (StringUtils.isNotBlank(name)) {
            url = url.replace("{attraction-name}", urlSafe(name));
        }
        url = url.replace("{attraction-code}", code);

        url = getContext() + url;

        return url;
    }

    protected List<CategoryModel> getCategoryPath(final ItemModel source) {
        if (source instanceof AttractionModel) {
            AttractionModel attraction = (AttractionModel) source;
            if (attraction.getLocations() != null) {
                Collection<LocationModel> locations = attraction.getLocations();
                List<CategoryModel> locationModels = new ArrayList<CategoryModel>();
                if (locations != null) {
                    for (LocationModel locationModel : locations) {
                        locationModels.add(locationModel);
                    }
                }
                return locationModels;

            }
        } else if (source instanceof ExcursionModel) {
            ExcursionModel excursion = (ExcursionModel) source;
            Set<ExcursionPriceModel> prices = excursion.getExcursionPrices();
            for (ExcursionPriceModel price : prices) {
                LocationModel locationModel = price.getLocation();
                if (locationModel != null) {
                    return Collections.<CategoryModel> singletonList(locationModel);
                }

            }
        }
        return Collections.<CategoryModel> emptyList();
    }
}
