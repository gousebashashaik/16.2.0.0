/**
 *
 */
package uk.co.portaltech.tui.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Arrays;
import java.util.List;

import uk.co.portaltech.travel.enums.LocationType;


/**
 * @author omonikhide
 *
 */

public class GeographyHierarchyInterceptor implements ValidateInterceptor
{



    static final List<LocationType> LOCATIONHIERARCHY = Arrays.asList(LocationType.CONTINENT, LocationType.COUNTRY,
            LocationType.REGION, LocationType.DESTINATION, LocationType.RESORT);

    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
     * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
     */
    /**
     * This interceptor is to enforce certain rules on the Tui hierarchy system. It kicks in just before a location model
     * is persisted to the database and it checks that a valid sub-type or super-type is attached to the model.
     *
     * @author omonikhide
     */
    @Override
    public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
    {
        //Need to revisit
    }

}
