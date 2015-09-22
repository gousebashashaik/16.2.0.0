/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.tui.helper.DeepLinkHelper;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author extab4
 *
 */
public class FlightOptionsRequestDataConverter extends
   AbstractPopulatingConverter<DeepLinkRequestViewData, SearchResultsRequestData>
{

   @Resource(name = "deepLinkHelper")
   private DeepLinkHelper deepLinkHelper;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public SearchResultsRequestData createTarget()
   {

      return new SearchResultsRequestData();
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.
    * AbstractPopulatingConverter#populate(java.lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final DeepLinkRequestViewData source, final SearchResultsRequestData target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      target.setAirports(source.getAirports());
      // original units are stored here, will be useful for creation of back url to accom page from
      // flight options
      target.setPrimaryUnits(source.getUnits());
      if (CollectionUtils.isEmpty(source.getUnits()))
      {
         target.setAnyWhereSearch(true);
      }
      // modified only for flight options
      target.setUnits(deepLinkHelper.getUnitForAccommodationForFlightOption(
         source.getProductCode(), source.getMultiSelect()));
      target.setWhen(source.getWhen());
      target.setFlexibility(source.isFlexibility());
      target.setFlexibleDays(source.getFlexibleDays());
      target.setNoOfAdults(source.getNoOfAdults());
      target.setNoOfChildren(source.getNoOfChildren());
      target.setNoOfSeniors(source.getNoOfSeniors());
      target.setChildrenAge(source.getChildrenAge());
      target.setInfantCount(source.getInfantCount());
      target.setUntil(source.getUntil());
      target.setFirst(source.getFirst());
      target.setLast(source.getLast());
      target.setDuration(source.getDuration());
      target.setUntil(source.getUntil());
      target.setSortBy(source.getSortBy());

      target.setSearchRequestType(source.getSearchRequestType());

      target.setIscapeRequest(source.isIscapeRequest());

      target.setProductCode(source.getProductCode());
      target.setPdd(source.getPdd());

      target.setFlightOptions(source.isFlightOptions());
      target.setSelectedBoardBasis(source.getSelectedBoardBasis());
      target.setBrandType(source.getBrandType());
      target.setPackageId(source.getPackageId());
      target.setRooms(source.getRooms());
      target.setAccomDetails(source.isAccomDetails());

   }
}
