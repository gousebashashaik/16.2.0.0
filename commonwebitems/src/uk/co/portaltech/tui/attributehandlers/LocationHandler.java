/**
 *
 */
package uk.co.portaltech.tui.attributehandlers;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.model.InspirationMapTabModel;
import uk.co.tui.async.logging.TUILogUtils;


/**
 * @author omonikhide
 *
 */
public class LocationHandler implements DynamicAttributeHandler<Collection<LocationModel>, InspirationMapTabModel>
{
    private static final TUILogUtils LOG = new TUILogUtils("LocationHandler");

    @Resource
    private CategoryService categoryService;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform.servicelayer.model
     * .AbstractItemModel)
     */
    @Override
    public Collection<LocationModel> get(final InspirationMapTabModel model)
    {
        final List<String> locationCodes = model.getLocationCodes();

        Collection<LocationModel> locations = null;
        if (locationCodes != null)
        {
            locations = new ArrayList<LocationModel>();
            for (final String code : locationCodes)
            {
                try
                {
                    if (!StringUtils.contains(code, "A-"))
                    {
                        final LocationModel locationModel = (LocationModel) categoryService.getCategoryForCode(code);
                        locations.add(locationModel);
                    }
                }
                catch (final ModelNotFoundException e)
                {
                    LOG.warn("No location found for :" + code,e);
                }
                catch (final AmbiguousIdentifierException e1)
                {
                    LOG.warn("More than one location found for :" + code,e1);
                }
                catch (final UnknownIdentifierException uie)
                {
                    LOG.warn("No location found for :" + code,uie);
                }
            }
            return locations;
        }
        return Collections.emptyList();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#set(de.hybris.platform.servicelayer.model
     * .AbstractItemModel, java.lang.Object)
     */
    @Override
    public void set(final InspirationMapTabModel model, final Collection<LocationModel> value)
    {
        throw new UnsupportedOperationException();

    }

}
