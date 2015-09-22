/**
 *
 */
package uk.co.tui.flights.decorator;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import uk.co.portaltech.commons.DateUtils;
import uk.co.tui.exception.SearchResultsBusinessException;
import uk.co.tui.flights.anite.request.FlightSearchCriteria;
import uk.co.tui.flights.exception.FlightsServiceException;
import uk.co.tui.flights.service.FlightSearchService;
import uk.co.tui.flights.web.view.data.DreamLinerTooltipViewData;
import uk.co.tui.flights.web.view.data.ItineraryViewData;


/**
 * @author vijaya.putheti
 *
 */
public class FlightSearchResultDecorator
{

   @Resource
   private FlightSearchService flightSearchService;

   @Resource
   private ConfigurationService configurationService;

   private static final String FO_TRACS_END_DATE = "FO.tracs.endDate";

   public static final String DATE_FORMAT_FOR_TRACS = "dd-MM-yyyy";

   /**
    * This method decorates the search results
    *
    * @param itineraryViewData
    * @param flightSearchCriteria
    * @param dreamlinerData
    * @param seasonEndDate
    * @param catalogVersion
    * @return ItineraryViewData
    * @throws SearchResultsBusinessException
    */
   public ItineraryViewData decorateFlightSearchResults(final ItineraryViewData itineraryViewData,
         final FlightSearchCriteria flightSearchCriteria, final DreamLinerTooltipViewData dreamlinerData,
         final String seasonEndDate, final CatalogVersionModel catalogVersion) throws SearchResultsBusinessException
   {
      try
      {
         itineraryViewData.setSeasonEndDate(seasonEndDate);
         final String[] depAirportCode = (String[]) flightSearchCriteria.getDepartureAirportCode().toArray();
         itineraryViewData.setDepAirportData(flightSearchService.getAirportDataForCode(depAirportCode[0], catalogVersion));
         final String[] arrAirportCode = (String[]) flightSearchCriteria.getArrivalAirportCode().toArray();
         itineraryViewData.setArrAirportData(flightSearchService.getAirportDataForCode(arrAirportCode[0], catalogVersion));
         itineraryViewData.setFlightSearchCriteria(flightSearchCriteria);
         itineraryViewData.setDreamLiner(dreamlinerData);
         itineraryViewData.setTracsEndDate(DateUtils.formatdate(
               configurationService.getConfiguration().getString(FO_TRACS_END_DATE), DATE_FORMAT_FOR_TRACS, "yyyy-MM-dd"));
      }
      catch (final FlightsServiceException e)
      {
         throw new SearchResultsBusinessException("6002", e);
      }
      return itineraryViewData;
   }

}
