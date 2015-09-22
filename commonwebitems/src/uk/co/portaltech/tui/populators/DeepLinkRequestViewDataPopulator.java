/**
 *
 */
package uk.co.portaltech.tui.populators;

import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;

/**
 * @author laxmibai.p
 *
 */
public final class DeepLinkRequestViewDataPopulator
{

   public void populate(final DeepLinkRequestData deepLinkRequestData,
      final DeepLinkRequestViewData deepLinkRequestViewData)
   {

      deepLinkRequestViewData.setAirports(deepLinkRequestData.getAirports());
      deepLinkRequestViewData.setUnits(deepLinkRequestData.getUnits());
      deepLinkRequestViewData.setWhen(deepLinkRequestData.getWhen());
      deepLinkRequestViewData.setFlexibility(deepLinkRequestData.isFlexibility());
      deepLinkRequestViewData.setNoOfAdults(deepLinkRequestData.getNoOfAdults());
      deepLinkRequestViewData.setNoOfChildren(deepLinkRequestData.getNoOfChildren());
      deepLinkRequestViewData.setNoOfSeniors(deepLinkRequestData.getNoOfSeniors());
      deepLinkRequestViewData.setChildrenAge(deepLinkRequestData.getChildrenAge());
      deepLinkRequestViewData.setFlexibleDays(deepLinkRequestData.getFlexibleDays());
      deepLinkRequestViewData.setFirst(deepLinkRequestData.getFirst());
      deepLinkRequestViewData.setLast(deepLinkRequestData.getLast());
      deepLinkRequestViewData.setDuration(deepLinkRequestData.getDuration());
      deepLinkRequestViewData.setInfantCount(deepLinkRequestData.getInfantCount());
      deepLinkRequestViewData.setSortBy(deepLinkRequestData.getSortBy());
      deepLinkRequestViewData.setUntil(deepLinkRequestData.getUntil());

      deepLinkRequestViewData.setSearchRequestType(deepLinkRequestData.getSearchRequestType());
      deepLinkRequestViewData.setIscapeRequest(deepLinkRequestData.isIscapeRequest());
      deepLinkRequestViewData.setProductCode(deepLinkRequestData.getProductCode());
      deepLinkRequestViewData.setFlightOptions(deepLinkRequestData.isFlightOptions());
      deepLinkRequestViewData.setSelectedBoardBasis(deepLinkRequestData.getSelectedBoardBasis());
      deepLinkRequestViewData.setBrandType(deepLinkRequestData.getBrandType());
      deepLinkRequestViewData.setPackageId(deepLinkRequestData.getPackageId());
      deepLinkRequestViewData.setRooms(deepLinkRequestData.getRooms());
      deepLinkRequestViewData.setModifiedDuration(deepLinkRequestData.getModifiedDuration());
      deepLinkRequestViewData.setMultiSelect(deepLinkRequestData.getMultiSelect());

      deepLinkRequestViewData.setStartdate(deepLinkRequestData.getStartDate());
      deepLinkRequestViewData.setEndDate(deepLinkRequestData.getEnddate());
      deepLinkRequestViewData.setSmerchDuration(deepLinkRequestData.getSmerchDuration());
      deepLinkRequestViewData.setProductType(deepLinkRequestData.getProductType());
      deepLinkRequestViewData.setSearchType(deepLinkRequestData.getSearchType());
      deepLinkRequestViewData.setPdd(deepLinkRequestData.getPdd());
   }

}
