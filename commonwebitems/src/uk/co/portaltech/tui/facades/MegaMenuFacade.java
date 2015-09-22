/**
 *
 */
package uk.co.portaltech.tui.facades;

import java.util.List;
import java.util.Map;

import uk.co.portaltech.tui.exception.NoSuchComponentException;


/**
 * @author sreenivasulu.v
 *
 */
public interface MegaMenuFacade
{

    Object getMegaMenuViewData(String componentid, Object parentcomponent, Object modelobject) throws NoSuchComponentException;

    Object getCollectionimages(List<Map<String, String>> listofMapsData);
}
