/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.web.view.data.SearchResultFlightDetailViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author shyamaprasada.vs
 *
 *
 *         This class generates iScapePackage id for a given accommodation
 */

public final class PackageIdGenerator
{

   private static final TUILogUtils LOG = new TUILogUtils("PackageIdGenerator");

   private PackageIdGenerator()
   {
   }

   /**
    * Note forshortlist should be true only for shortlist as TH package id has selling code appended
    * only in shortlist.
    *
    * @param searchResultData
    * @param brand
    * @param forShortlist
    * @return packageId
    */
   public static String getIscapePackageId(final SearchResultViewData searchResultData,
      final String brand, final boolean forShortlist)
   {
      if ("TH".equalsIgnoreCase(brand))
      {
         if (forShortlist)
         {
            return generateIscapeTHPackageId(searchResultData).concat(
               searchResultData.getSellingCode());
         }
         else
         {
            return generateIscapeTHPackageId(searchResultData);
         }

      }
      else if ("FC".equalsIgnoreCase(brand))
      {
         return generateIscapePackageId(searchResultData);
      }

      return StringUtils.EMPTY;
   }

   /**
    *
    * @param searchResultData SearchResultViewData
    *
    * @return String iscapePackageId
    *
    *
    */
   public static String generateIscapePackageId(final SearchResultViewData searchResultData)
   {

      final StringBuilder flightInfoCode = new StringBuilder("");

      final StringBuilder iscapePackageId = new StringBuilder();

      addSectorInformation(flightInfoCode, searchResultData.getItinerary().getOutbounds());

      addSectorInformation(flightInfoCode, searchResultData.getItinerary().getInbounds());

      iscapePackageId.append(searchResultData.getAccommodation().getCode())
         .append(searchResultData.getSellingCode()).append(flightInfoCode);
      // .append(getSellingCodeFromTracsUnitCode(searchResultData

      LOG.debug("Iscape ID generated for given Accommodation");

      return iscapePackageId.toString();

   }

   public static String generateIscapeTHPackageId(final SearchResultViewData searchResultData)
   {

      final StringBuilder flightInfoCode = new StringBuilder("");

      final StringBuilder iscapePackageId = new StringBuilder();

      addSectorInformation(flightInfoCode, searchResultData.getItinerary().getOutbounds());

      addSectorInformation(flightInfoCode, searchResultData.getItinerary().getInbounds());

      iscapePackageId.append(searchResultData.getAccommodation().getCode()).append(flightInfoCode);

      LOG.debug("Iscape ID generated for given Accommodation");

      return iscapePackageId.toString();

   }

   /**
    * @param flightInfoCode
    * @param sector
    */
   private static void addSectorInformation(final StringBuilder flightInfoCode,
      final List<SearchResultFlightDetailViewData> sector)
   {
      for (final SearchResultFlightDetailViewData outbound : sector)
      {

         if (null != outbound.getSchedule())
         {
            flightInfoCode.append(outbound.getSchedule().getDepartureDateTimeInMilli()).append(
               outbound.getSchedule().getArrivalDateTimeInMilli());
            if (StringUtils.isNotEmpty(outbound.getCarrier().getCode()))
            {
               flightInfoCode.append(outbound.getCarrier().getCode());
            }
         }
      }
   }

   /**
    * This method converts the given TRACS unit code into selling Code
    *
    * @param tracsUnitCode
    * @return String
    *
    *         private static String getSellingCodeFromTracsUnitCode( final String tracsUnitCode) {
    *         final String unitCode = tracsUnitCode.substring(0, 6);
    *
    *         return new StringBuilder() .append(unitCode.substring(unitCode.length() / 2))
    *         .append(unitCode.substring(0, unitCode.length() / 2)) .toString(); return null; }
    */
}
