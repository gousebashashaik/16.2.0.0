/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.web.view.data.BookFlowAccommodationViewData;
import uk.co.portaltech.tui.web.view.data.FlightOptionRequestData;

/**
 * @author deepakkumar.k
 *
 */
public class FlightOptionRequestPopulator implements
   Populator<BookFlowAccommodationViewData, FlightOptionRequestData>
{
   private static final int SEVEN = 7;

   private static final int SEVENTEEN = 17;

   private static final int NINE = 9;

   private static final int TWO = 2;

   @Resource
   private ConfigurationService configurationService;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */

   @Override
   public void populate(final BookFlowAccommodationViewData source,
      final FlightOptionRequestData target) throws ConversionException
   {

      target.setBoardBasisPackageNumber(getBoardBasisPackageNumber(source));
      target.setMthyr(getMOnthAndYear(source));
      target.setDurT(setDurationType(source));
      target.setLs(Boolean.FALSE);
      target.setTuidesc(source.getPackageData().getAccommodation().getLocation().getDestination()
         .getCode());
      target.setDay(source.getSearchRequestData().getWhen().split("-")[0]);
      target.setMps(getMaximumPartySize());
      target.setSda("true");
      target.setPconfig(setPartyconfiguration(source));
      target.setTchd(source.getSearchRequestData().getChildCount());
      target.setRating(0);
      target.setDess("true");
      target.setAct(0);
      target.setJsen(Boolean.TRUE);
      target.setAttrstr("||||||||null|null");
      target.setTinf(source.getSearchRequestData().getInfantCount());
      target.setMnth((source.getSearchRequestData().getWhen().split("-"))[1]);

      target.setBc(SEVENTEEN);
      target.setMargindt(SEVEN);
      target.setTadt(source.getSearchRequestData().getNoOfAdults());
      target
         .setNumr(Integer.toString(source.getPackageData().getAccommodation().getRooms().size()));
      target.setDepm(SEVEN);
      target.setDur(source.getPackageData().getDuration());
      target.setDtx(0);
      target.setDxsel(0);
      target.setDac(source.getPackageData().getItinerary().getOutbounds().get(0)
         .getDepartureAirportCode());
      target.setLoct(0);
      target.setTsnr(source.getSearchRequestData().getNoOfSeniors());
      target.setYear((source.getSearchRequestData().getWhen().split("-"))[TWO]);
      target.setDta(Boolean.FALSE);
      target.setUc(source.getPackageData().getAccommodation().getCode());
      target.setPid(PackageIdGenerator.generateIscapePackageId(source.getPackageData()));
      target.setSd(setSelectedDate(source));
   }

   /**
    * get max no of child and adult from properties files
    *
    * @return int- max party size
    */
   private int getMaximumPartySize()
   {

      return configurationService.getConfiguration().getInt("holiday.search.maxNoOfAdult", NINE)
         + configurationService.getConfiguration().getInt(" holiday.search.maxNoOfChildy", NINE);
   }

   /**
     *
     */
   private String getBoardBasisPackageNumber(final BookFlowAccommodationViewData source)

   {
      final StringBuilder sb = new StringBuilder();
      if (StringUtils.isNotEmpty(source.getPackageData().getAccommodation().getRooms().get(0)
         .getBoardBasisCode()))
      {
         sb.append(source.getPackageData().getAccommodation().getRooms().get(0).getBoardBasisCode()
            + "|");
      }
      sb.append(PackageIdGenerator.generateIscapePackageId(source.getPackageData()));

      return sb.toString();

   }

   /**
    * @param source
    * @return Retrieving selected date in (dd/mm/yyyy)
    */
   private String setSelectedDate(final BookFlowAccommodationViewData source)
   {
      final String[] all = source.getSearchRequestData().getWhen().split("-");
      return (new StringBuilder(all[0]).append("/").append(all[1]).append("/").append(all[TWO]))
         .toString();
   }

   /**
    * @param source
    * @return Retrieving party config .
    */
   private String setPartyconfiguration(final BookFlowAccommodationViewData source)
   {
      final StringBuilder sb1 = new StringBuilder();
      for (final Integer age : source.getSearchRequestData().getChildrenAge())
      {
         sb1.append(age).append("-");

      }

      final StringBuilder sb =
         new StringBuilder().append((source.getPackageData().getAccommodation().getRooms()).size())
            .append("|").append(source.getSearchRequestData().getNoOfAdults()).append("|")
            .append(source.getSearchRequestData().getNoOfSeniors()).append("|")
            .append(source.getSearchRequestData().getNoOfChildren()).append("|")
            .append(source.getSearchRequestData().getInfantCount()).append("|").append(sb1)
            .append("/");

      return sb.toString();
   }

   /**
    * @param source
    * @return Retrieving duration in format(7/w(1 week))
    */
   private String setDurationType(final BookFlowAccommodationViewData source)
   {
      final int duration = source.getPackageData().getDuration();
      final StringBuilder sb = new StringBuilder().append(duration).append("/");
      if (duration % SEVEN == 0)
      {
         return sb.append("w").toString();
      }
      return sb.append("n").toString();
   }

   /**
    * @param source
    * @return Retrieving month and years.
    */
   private String getMOnthAndYear(final BookFlowAccommodationViewData source)
   {
      final String[] all = source.getSearchRequestData().getWhen().split("-");
      return (new StringBuilder(all[1]).append("/").append(all[TWO])).toString();
   }

}
