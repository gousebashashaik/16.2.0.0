/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

import javax.annotation.Resource;


import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.LowDepositModel;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.services.lowdeposit.LowDepositService;
import uk.co.portaltech.travel.services.lowdeposit.PartyCompositionCriteria;
import uk.co.portaltech.travel.services.lowdeposit.VisionBrochureCriteria;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.tui.async.logging.TUILogUtils;


/**
 *
 *
 */
public class SearchResultLowDepositPopulator
{

    @Resource
    private LowDepositService lowDepositService;


    private static final TUILogUtils LOG = new TUILogUtils("SearchResultLowDepositPopulator");

    /**
     * This method populate the price per chargeable person after deciding whether perPerson deposit or perFamily deposit
     *
     * @param holiday
     * @param target
     * @param partyCompositionCriteria
     *
     */
    public void populateLowDeposit(final Holiday holiday, final SearchResultViewData target,
            final PartyCompositionCriteria partyCompositionCriteria) throws ConversionException
    {

        LOG.debug("populateLowDeposit start");

        final VisionBrochureCriteria brochureCriteria = new VisionBrochureCriteria();
        getBrochureCriteria(brochureCriteria, holiday);

        //Setting the haul type
        /**
         * This needs to be business clarified when we have stop over flights. The below condition would work only when we
         * have direct flight.
         */
        brochureCriteria.setFlightType(target.getItinerary().getInbounds().get(0).getHaulType());

        //fire rules to check N weeks before which low deposit applies




        // By default per person Low deposit is applied


        LowDepositModel lowDeposit = null;
        //If departure date is more than configured value then only calculate low deposit
        final int chargeablePersons = partyCompositionCriteria.getNoOfPassengers() - partyCompositionCriteria.getInfantCount();
        if (holiday.getDepositAmount() != null && holiday.getDepositAmount().doubleValue() != 0.0)
        {
            lowDeposit = lowDepositService.getLowDeposit(brochureCriteria);

            if (lowDeposit != null && holiday.getDepositAmount().doubleValue() > lowDeposit.getPerFamily().doubleValue()
                    && holiday.getDepositAmount().doubleValue() > lowDeposit.getPerPerson().doubleValue() * chargeablePersons)
            {
                // fire partycomposition rules

                target.getPrice().setLowDepositExists(true);
                if (partyCompositionCriteria.isPerPersonDeposit() || (lowDeposit.getPerFamily() == null))
                {
                    target.getPrice().setLowDeposit(lowDeposit.getPerPerson().toString());
                    target.getPrice().setDepositAmount(
                            new BigDecimal(lowDeposit.getPerPerson().intValue() * chargeablePersons).setScale(0, RoundingMode.UP)
                                    .toString());
                    target.getPrice().setDepositAmountPP(lowDeposit.getPerPerson().toString());
                }
                else
                {
                    target.getPrice().setLowDeposit(getCalculatedPrice(lowDeposit.getPerFamily(), partyCompositionCriteria));
                    target.getPrice().setDepositAmount(lowDeposit.getPerFamily().toString());
                    target.getPrice().setDepositAmountPP(getCalculatedPrice(lowDeposit.getPerFamily(), partyCompositionCriteria));
                }
            }


            if (lowDeposit == null)
            {
                target.getPrice().setDepositExists(true);
                target.getPrice().setDepositAmountPP(
                        new BigDecimal(holiday.getDepositAmount().doubleValue() / chargeablePersons).setScale(0, RoundingMode.UP)
                                .toString());
                target.getPrice().setDepositAmount(
                        new BigDecimal(holiday.getDepositAmount().doubleValue()).setScale(0, RoundingMode.UP).toString());
            }
        }

        LOG.debug("populateLowDeposit End");

    }

    /**
     * This method calculates the price per chargeable person
     *
     * @param perFamilyPrice
     * @param partyCompositionCriteria
     * @return calculatedPrice
     */
    private String getCalculatedPrice(final Double perFamilyPrice, final PartyCompositionCriteria partyCompositionCriteria)
    {
        String calculatedPrice = null;

        final int chargeablePersons = partyCompositionCriteria.getNoOfPassengers() - partyCompositionCriteria.getInfantCount();


        LOG.debug("Chargeble persons " + chargeablePersons);

        if (chargeablePersons != 0)
        {
            final BigDecimal bd = new BigDecimal(perFamilyPrice / chargeablePersons);
            calculatedPrice = bd.round(new MathContext(bd.precision() - bd.scale(), RoundingMode.UP)).toString();
        }

        LOG.debug("getCalculatedPrice " + calculatedPrice);

        return calculatedPrice;
    }

    /**
     * VisionBrochureCriteria is filled with Product code, SubProduct code and Flight type
     *
     * @param brochureCriteria
     * @param holiday
     */
    private void getBrochureCriteria(final VisionBrochureCriteria brochureCriteria, final Holiday holiday)
    {

        LOG.debug("getBrochureCriteria start");

        // Get product code and Sub product code from Holiday
        brochureCriteria.setSubProductCode(holiday.getInventoryInfo().getSubProductCode());
        brochureCriteria.setProductCode(holiday.getInventoryInfo().getProductCode());
        final Date bookingdate = DateUtils.getSystemDate();
        final Date depdate = holiday.getItinerary().getOutbound().get(0).getSchedule().getDepartureDate().toDate();

        brochureCriteria.setDepartureDate(depdate);
        brochureCriteria.setBookingDate(bookingdate);
        brochureCriteria.setNoOfDays(bookingdate, depdate);

        LOG.debug("getBrochureCriteria End");

    }

}
