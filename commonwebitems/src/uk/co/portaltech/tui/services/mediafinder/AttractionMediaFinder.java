/**
 *
 */
package uk.co.portaltech.tui.services.mediafinder;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.fest.util.Collections;

import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;

/**
 * @author omonikhide
 *
 */
public class AttractionMediaFinder implements MediaFinder {

    @Resource
    private CategoryService      categoryService;



    @Override
    public SearchResultData<MediaModel> search(SearchRequestData request) {

        return null;
    }

    @Override
    public HasFeatures searchAutomatic(SearchRequestData request, String code, String type) {

        return null;
    }

    @Override
    public HasFeatures searchManual(SearchRequestData request, String code, String type) {

        return null;
    }

    @Override
    public HasFeatures searchAttractions(SearchRequestData request, String code) {
        LocationModel locationModel = (LocationModel) categoryService.getCategoryForCode(code);
        List<MediaModel> medias = new ArrayList<MediaModel>();
        Collection<AttractionModel> attractions = locationModel.getAttractions();
        if (!Collections.isEmpty(attractions)) {
            for (AttractionModel attraction : attractions) {
                medias.add(attraction.getPicture());
            }
        }
        return null;
    }

}
