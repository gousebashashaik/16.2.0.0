/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;

/**
 * @author laxmibai.p
 *
 */
public class SearchResultsRequestDataConverter extends
   AbstractPopulatingConverter<DeepLinkRequestViewData, SearchResultsRequestData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public SearchResultsRequestData createTarget()
   {

      return new SearchResultsRequestData();
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public void populate(final DeepLinkRequestViewData source, final SearchResultsRequestData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      target.setAirports(source.getAirports());
      target.setUnits(source.getUnits());
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
      if (StringUtils.isNotEmpty(source.getBrandType()))
      {
         target.setBrandType(source.getBrandType());
      }
      target.setRooms(source.getRooms());
      target.setModifiedDuration(source.getModifiedDuration());
      target.setSearchType(source.getSearchType());
   }

}
