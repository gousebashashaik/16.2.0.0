/**
 *
 */
package uk.co.portaltech.tui.attributehandlers;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.model.InspirationMapTabModel;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author omonikhide
 *
 */
public class LocationsByTypeHandler implements
   DynamicAttributeHandler<Collection<AccommodationModel>, InspirationMapTabModel>
{

   private static final TUILogUtils LOG = new TUILogUtils("LocationsByTypeHandler");

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   public static final int TWO = 2;

   /*
    * (non-Javadoc)
    *
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform
    * .servicelayer.model .AbstractItemModel)
    */
   @Override
   public Collection<AccommodationModel> get(final InspirationMapTabModel model)
   {
      final List<String> locationCodes = model.getLocationCodes();

      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPks = brandDetails.getRelevantBrands();

      Collection<AccommodationModel> accommodations = null;
      if (locationCodes != null)
      {
         accommodations = new ArrayList<AccommodationModel>();
         for (String code : locationCodes)
         {
            try
            {

               if (StringUtils.contains(code, "A-"))
               {
                  code = code.substring(TWO);
                  final AccommodationModel accom =
                     accommodationService.getAccomodationByCodeAndCatalogVersion(code,
                        cmsSiteService.getCurrentCatalogVersion(), brandPks);
                  if (accom != null)
                  {
                     accommodations.add(accom);
                  }
               }
            }
            catch (final ModelNotFoundException e)
            {
               LOG.warn("No Accommodation found for :" + code, e);
            }
            catch (final AmbiguousIdentifierException e1)
            {
               LOG.warn("More than one Accommodation found for :" + code, e1);
            }
            catch (final UnknownIdentifierException uie)
            {
               LOG.warn("No Accommodation found for :" + code, uie);
            }
         }
         return accommodations;
      }
      return Collections.emptyList();
   }

   /*
    * (non-Javadoc)
    *
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#set(de.hybris.platform
    * .servicelayer.model .AbstractItemModel, java.lang.Object)
    */
   @Override
   public void set(final InspirationMapTabModel model, final Collection<AccommodationModel> value)
   {
      throw new UnsupportedOperationException();

   }

}
