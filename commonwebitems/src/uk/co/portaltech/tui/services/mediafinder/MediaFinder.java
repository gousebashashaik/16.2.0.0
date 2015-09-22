/**
 *
 */
package uk.co.portaltech.tui.services.mediafinder;

import de.hybris.platform.core.model.media.MediaModel;

import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;

/**
 * This interface would return a list of Media model which is based on the model set for the carousel item.
 *
 * @author omonikhide
 *
 */
public interface MediaFinder
{
    SearchResultData<MediaModel> search(SearchRequestData request);

    HasFeatures searchAutomatic(SearchRequestData request, String code, String type);

    HasFeatures searchManual(SearchRequestData request, String code, String type);

    HasFeatures searchAttractions(SearchRequestData request, String code);

}
