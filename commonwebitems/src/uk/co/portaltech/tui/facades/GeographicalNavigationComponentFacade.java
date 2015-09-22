/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;

/**
 * @author gagan
 *
 */
public interface GeographicalNavigationComponentFacade {

    GeographicalNavigationComponentViewData getGeographicalNavigationData(String componentUID, String categoryCode, String seoPage, String brand);

}
