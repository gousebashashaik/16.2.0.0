/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.tui.converters.FacilityOption;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * @author l.furrer
 *
 */
public class AccommodationFacilitiesPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   @Resource
   private Converter<FacilityModel, FacilityViewData> facilityConverter;

   private ConfigurablePopulator<FacilityModel, FacilityViewData, FacilityOption> facilityConfiguredPopulator;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   /** The log. */
   private static final Logger LOG = Logger.getLogger(AccommodationFacilitiesPopulator.class);

   @Required
   public void setFacilityConfiguredPopulator(
      ConfigurablePopulator<FacilityModel, FacilityViewData, FacilityOption> facilityConfiguredPopulator)
   {
      this.facilityConfiguredPopulator = facilityConfiguredPopulator;
   }

   @Override
   public void populate(AccommodationModel sourceModel, AccommodationViewData targetData)
      throws ConversionException
   {

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      ArrayList<FacilityViewData> facilityDataList = new ArrayList<FacilityViewData>();
      Iterator<FacilityModel> facilitiesModelItr = sourceModel.getFacilities().iterator();
      while (facilitiesModelItr.hasNext())
      {
         FacilityModel facilityModel = facilitiesModelItr.next();
         FacilityViewData facilityData = facilityConverter.convert(facilityModel);
         LOG.debug(sourceModel.getAccommodationId() + " :::::::FacilityModel Code-"
            + facilityModel.getCode() + ":Name -" + facilityModel.getName());
         facilityConfiguredPopulator.populate(facilityModel, facilityData,
            Arrays.asList(FacilityOption.FACILITY_BASIC));
         facilityData.setName(facilityModel.getName());
         LOG.debug("facilityData.getFacilityType() is " + facilityData.getFacilityType());
         LOG.debug("facilityData.getFacilityType() is " + facilityData.getParentFacilityType());
         LOG.debug("facilityData.getParentFacilityTypeName() is "
            + facilityData.getParentFacilityTypeName());
         facilityDataList.add(facilityData);
      }

      targetData.setFacilities(facilityDataList);
      tuiProductUrlResolver.setOverrideSubPageType("facilities");
      String facilitiesUrl = tuiProductUrlResolver.resolve(sourceModel);
      targetData.setFacilitiesUrl(facilitiesUrl);
   }
}
