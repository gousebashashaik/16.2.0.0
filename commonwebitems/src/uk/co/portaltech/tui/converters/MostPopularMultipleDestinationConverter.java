/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.MostPopularDestinationdata;
import uk.co.travel.daos.util.BrandUtils;

/**
 * @author niranjani.r
 *
 */
public class MostPopularMultipleDestinationConverter extends
   AbstractConverter<List<String>, MostPopularDestinationdata>
{
   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private DestinationConverter destinationConverter;

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractConverter#populate(java.lang.Object
    * , java.lang.Object)
    */
   @Override
   public void populate(final List<String> source, final MostPopularDestinationdata target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      final List<DestinationData> destData = new ArrayList<DestinationData>();
      target.setMultiple(true);
      final List<LocationModel> locList = getLocationList(source);
      for (final LocationModel model : locList)
      {
         if (model != null
            && BrandUtils.brandCodesExistInBrandTypes(model.getBrands(),
               tuiUtilityService.getSiteReleventBrands()))
         {
            final DestinationData data = new DestinationData();
            destinationConverter.populate(model, data);
            destData.add(data);
         }
      }
      target.setLocationDatas(destData);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public MostPopularDestinationdata createTarget()
   {
      // YTODO Auto-generated method stub
      return new MostPopularDestinationdata();
   }

   private List<LocationModel> getLocationList(final List<String> locList)
   {
      final List<LocationModel> locationModels = new ArrayList<LocationModel>();
      final LinkedHashSet<String> locationHashSet = new LinkedHashSet<String>(locList);
      for (final Iterator iterator = locationHashSet.iterator(); iterator.hasNext();)
      {
         final String string = (String) iterator.next();

         locationModels.add(mstravelLocationService.getLocationModelForCodeWithBrands(string,
            tuiUtilityService.getSiteReleventBrandPks()));

      }
      return locationModels;
   }
}
