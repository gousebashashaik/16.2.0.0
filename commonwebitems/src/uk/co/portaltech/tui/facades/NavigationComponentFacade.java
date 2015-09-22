/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;

/**
 * @author gagan
 *
 */
public interface NavigationComponentFacade {

    NavigationComponentViewData getNavigationComponentViewData(String componentUID);
}
