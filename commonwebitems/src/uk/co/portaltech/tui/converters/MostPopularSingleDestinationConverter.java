/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractConverter;

import java.util.ArrayList;
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
public class MostPopularSingleDestinationConverter extends
   AbstractConverter<String, MostPopularDestinationdata>
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
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public MostPopularDestinationdata createTarget()
   {
      return new MostPopularDestinationdata();
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.commerceservices.converter.impl.AbstractConverter#populate(java.lang.Object
    * , java.lang.Object)
    */
   @Override
   public void populate(final String source, final MostPopularDestinationdata target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");
      final List<DestinationData> destData = new ArrayList<DestinationData>();
      target.setMultiple(false);
      final LocationModel model = getLocation(source);

      if (model != null
         && BrandUtils.brandCodesExistInBrandTypes(model.getBrands(),
            tuiUtilityService.getSiteReleventBrands()))
      {
         final DestinationData data = new DestinationData();
         destinationConverter.populate(model, data);
         destData.add(data);
      }
      target.setLocationDatas(destData);
   }

   private LocationModel getLocation(final String loc)
   {
      final String location = loc;
      return mstravelLocationService.getLocationModelForCodeWithBrands(location,
         tuiUtilityService.getSiteReleventBrandPks());
   }

}
