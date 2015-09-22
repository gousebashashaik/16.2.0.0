/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.components.model.AccommodationStandardComponentModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.SearchFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.tui.web.view.data.wrapper.AccommodationStandardWrapper;


/**
 * @author gaurav.b
 *
 */
public class AccommodationStandardFacade
{

    @Resource
    private SearchFacade searchFacade;

    @Resource
    private AccommodationFacade accomodationFacade;

    private static final int NUMBER_SIX = 6;
    private static final String BUDGET_HOTEL = "Budget";
    private static final String STANDARD_HOTEL = "Standard";
    private static final String SUPERIOR_HOTEL = "Superior";

    public AccommodationStandardWrapper getAccommodationStandardData(final String locationCode, final String pageType,
            final String seoPageType, final AccommodationStandardComponentModel item)
    {

        final AccommodationStandardWrapper accommodationStandardWrapper = new AccommodationStandardWrapper();

        final SearchResultData searchResult = searchFacade.getAccommodationStandardData(locationCode, pageType, seoPageType, item);

        processResult(searchResult, accommodationStandardWrapper);

        return accommodationStandardWrapper;
    }

    private void processResult(final SearchResultData searchResult, final AccommodationStandardWrapper accommodationStandardWrapper)
    {
        final Map<String, List<AccommodationViewData>> hotelTypeAccomViewDataListMap = new LinkedHashMap<String, List<AccommodationViewData>>();
        final List<AccommodationViewData> budgetAccommodationViewDatas = new ArrayList<AccommodationViewData>();
        final List<AccommodationViewData> standardAccommodationViewDatas = new ArrayList<AccommodationViewData>();
        final List<AccommodationViewData> superiorAccommodationViewDatas = new ArrayList<AccommodationViewData>();

        final List<ResultData> resultDatas = searchResult.getResults();
        for (final ResultData resultData : resultDatas)
        {
            final AccommodationViewData accommodationViewData = accomodationFacade.getAccommodationEditorialInfo(StringUtils
                    .leftPad(resultData.getCode(), NUMBER_SIX, '0'));

            final String hotelType = resultData.getHotelType().trim();
            final String price = resultData.getPriceFrom().trim();
            final double endecaPrice = Double.parseDouble(price);

            accommodationViewData.setHotelType(hotelType);
            accommodationViewData.setPriceFrom(price);

            setPrices(accommodationStandardWrapper, budgetAccommodationViewDatas, standardAccommodationViewDatas,
                    superiorAccommodationViewDatas, accommodationViewData, hotelType, endecaPrice);
        }

        hotelTypeAccomViewDataListMap.put(BUDGET_HOTEL, budgetAccommodationViewDatas);
        hotelTypeAccomViewDataListMap.put(STANDARD_HOTEL, standardAccommodationViewDatas);
        hotelTypeAccomViewDataListMap.put(SUPERIOR_HOTEL, superiorAccommodationViewDatas);

        accommodationStandardWrapper.setBudgetFromPrice(doubleToString(accommodationStandardWrapper.getBudgetPriceFrom()));
        accommodationStandardWrapper.setStandardFromPrice(doubleToString(accommodationStandardWrapper.getStandardPriceFrom()));
        accommodationStandardWrapper.setSuperiorFromPrice(doubleToString(accommodationStandardWrapper.getSuperiorPriceFrom()));
        accommodationStandardWrapper.setHotelTypeAccomViewDataListMap(hotelTypeAccomViewDataListMap);

    }

    /**
     * @param accommodationStandardWrapperParam
     * @param budgetAccommodationViewDatasParam
     * @param standardAccommodationViewDatas
     * @param superiorAccommodationViewDatas
     * @param accommodationViewData
     * @param hotelType
     * @param endecaPrice
     */
    private void setPrices(final AccommodationStandardWrapper accommodationStandardWrapperParam,
            final List<AccommodationViewData> budgetAccommodationViewDatasParam,
            final List<AccommodationViewData> standardAccommodationViewDatas,
            final List<AccommodationViewData> superiorAccommodationViewDatas, final AccommodationViewData accommodationViewData,
            final String hotelType, final double endecaPrice)
    {
        final AccommodationStandardWrapper accommodationStandardWrapper = accommodationStandardWrapperParam;
        final List<AccommodationViewData> budgetAccommodationViewDatas = budgetAccommodationViewDatasParam;

        if (StringUtils.equalsIgnoreCase(hotelType, BUDGET_HOTEL))
        {

            accommodationStandardWrapper.setBudgetPriceFrom(doubleCompare(endecaPrice,
                    accommodationStandardWrapper.getBudgetPriceFrom()));
            budgetAccommodationViewDatas.add(accommodationViewData);
        }
        else if (StringUtils.equalsIgnoreCase(hotelType, STANDARD_HOTEL))
        {
            accommodationStandardWrapper.setStandardPriceFrom(doubleCompare(endecaPrice,
                    accommodationStandardWrapper.getStandardPriceFrom()));
            standardAccommodationViewDatas.add(accommodationViewData);
        }
        else if (StringUtils.equalsIgnoreCase(hotelType, SUPERIOR_HOTEL))
        {
            accommodationStandardWrapper.setSuperiorPriceFrom(doubleCompare(endecaPrice,
                    accommodationStandardWrapper.getSuperiorPriceFrom()));
            superiorAccommodationViewDatas.add(accommodationViewData);
        }
    }

    /**
     * Assuming d1 cannot be zero at any given point
     *
     * @param d1
     * @param d2
     * @return returns always smallest value
     */
    private double doubleCompare(final double d1, final double d2)
    {
        if (Double.compare(d2, BigDecimal.ZERO.doubleValue()) == 0)
        {
            return d1;
        }
        if (Double.compare(d1, d2) < 0)
        {
            return d1;
        }
        else
        {
            return d2;
        }
    }

    private String doubleToString(final double d)
    {
        final BigDecimal bd = new BigDecimal(d).setScale(0, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**
     * @param searchFacade
     *           the searchFacade to set
     */
    public void setSearchFacade(final SearchFacade searchFacade)
    {
        this.searchFacade = searchFacade;
    }

    /**
     * @param accomodationFacade
     *           the accomodationFacade to set
     */
    public void setAccomodationFacade(final AccommodationFacade accomodationFacade)
    {
        this.accomodationFacade = accomodationFacade;
    }


}
