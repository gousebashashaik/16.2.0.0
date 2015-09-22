/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.ExtraFacilityRestrictions;
import uk.co.tui.book.passenger.utils.PassengerUtils;
import uk.co.tui.cr.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewDataContainer;

/**
 * The Class PackageExtrasContainerPopulator.
 *
 * @author pradeep.as
 */
public class CruiseExtrasContainerPopulator
        implements
            Populator<List<ExtraFacility>, ExtraFacilityViewDataContainer> {

    /** Package Cart Service. */
    @Resource
    private PackageCartService packageCartService;

    /**
     * The populator method to populate the ExtraFacilityViewDataContainer value
     * required for page.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     */
    @Override
    public void populate(final List<ExtraFacility> source,
            final ExtraFacilityViewDataContainer target) {
        // for getting the max age of child world care extra.
        String exFacilityCategoryCode = null;
        for (final ExtraFacility extras : source) {
            if (extras.getExtraFacilityCategory() != null) {
                exFacilityCategoryCode = extras.getExtraFacilityCategory()
                        .getCode();
            }
            populateClassActWorkshopExtrasOptions(extras, target,
                    exFacilityCategoryCode);
        }
    }

    /**
     * Populate class act workshop extras options.
     *
     * @param extras
     *            the extras
     * @param target
     *            the target
     * @param exFacilityCategoryCode
     *            the ex facility category code
     */
    private void populateClassActWorkshopExtrasOptions(
            final ExtraFacility extras,
            final ExtraFacilityViewDataContainer target,
            final String exFacilityCategoryCode) {
        if (StringUtils.equalsIgnoreCase(exFacilityCategoryCode,
                ExtraFacilityConstants.CLASS_ACT_WORKSHOP_OPTION_CATEGORY)) {
            populateClassActWorkshopExtras(extras, target);
        }

    }

    /**
     * Populate class act workshop extras.
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @param target
     *            the target
     */
    private void populateClassActWorkshopExtras(
            final ExtraFacility extraFacilityModel,
            final ExtraFacilityViewDataContainer target) {
        final BasePackage packageModel = getPackageModel();
        final int selectedQuantity = getQuantity(extraFacilityModel);

        final ExtraFacilityRestrictions restrictions = extraFacilityModel
                .getExtraFacilityRestrictions();
        final int paxCount = PassengerUtils.getPassengersWithAgeRang(
                restrictions.getMinAge(), restrictions.getMaxAge(),
                packageModel.getPassengers()).size();

        if (checkExtraFacility(extraFacilityModel, target.getWorkshopOptions(),
                packageModel)) {
            return;
        }

        populateClassActWorkshopViewData(extraFacilityModel, target,
                selectedQuantity, paxCount);

    }

    /**
     * Gets the quantity.
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @return the quantity
     */
    private int getQuantity(final ExtraFacility extraFacilityModel) {
        return extraFacilityModel.getPrices().get(0).getQuantity() != null
                ? extraFacilityModel.getPrices().get(0).getQuantity()
                        .intValue()
                : 0;
    }

    /**
     * Populate class act workshop view data.
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @param target
     *            the target
     * @param selectedQuantity
     *            the selected quantity
     * @param paxCount
     *            the pax count
     */
    private void populateClassActWorkshopViewData(
            final ExtraFacility extraFacilityModel,
            final ExtraFacilityViewDataContainer target,
            final int selectedQuantity, final int paxCount) {

        final int avlQuantity = extraFacilityModel.getQuantity().intValue();

        final ExtraFacilityCategoryViewData workshopExtraFacilityViewData = target
                .getWorkshopOptions();
        workshopExtraFacilityViewData
                .setExtraFacilityCategoryCode(extraFacilityModel
                        .getExtraFacilityCategory().getCode());
        workshopExtraFacilityViewData
                .setExtraFacilityGroupCode(extraFacilityModel
                        .getExtraFacilityGroup().toString());

        final ExtraFacilityViewData extraFacilityViewData = new ExtraFacilityViewData();

        extraFacilityViewData
                .setCode(extraFacilityModel.getExtraFacilityCode());
        extraFacilityViewData.setGroupCode(extraFacilityModel
                .getExtraFacilityGroup().toString());

        if (avlQuantity <= paxCount) {
            extraFacilityViewData.setQuantity(avlQuantity);
        } else {
            extraFacilityViewData.setQuantity(paxCount);
        }

        extraFacilityViewData.setSelectedQuantity(selectedQuantity);
        final BigDecimal unitPrice = extraFacilityModel.getPrices().get(0)
                .getRate().getAmount();
        final Currency currency = extraFacilityModel.getPrices().get(0)
                .getRate().getCurrency();


        // final BigDecimal pricePerNight = unitPrice.divide(


        final BigDecimal noOfselectedQuantity = new BigDecimal(selectedQuantity);
        final BigDecimal totalPrice = unitPrice.multiply(noOfselectedQuantity);

        extraFacilityViewData
                .setCurrencyAppendedPerPersonPrice(getCurrencyAppendedPrice(
                        unitPrice, currency));

        extraFacilityViewData
                .setCurrencyAppendedPrice(getCurrencyAppendedPrice(totalPrice,
                        currency));

        extraFacilityViewData
                .setCurrencyAppendedPricePerClass(getCurrencyAppendedPrice(
                        unitPrice, currency));

        extraFacilityViewData.setDescription(extraFacilityModel
                .getDescription());

        extraFacilityViewData.setSelected(extraFacilityModel.isSelected());
        if (!extraFacilityModel.isSelected()) {
            extraFacilityViewData.setSelectedQuantity(0);
        }

        workshopExtraFacilityViewData.getExtraFacilityViewData().add(
                extraFacilityViewData);
        workshopExtraFacilityViewData.setAvailable(true);
        target.setWorkshopOptions(workshopExtraFacilityViewData);

    }

    /**
     * To get the currency appended price.
     *
     * @param price
     *            the price
     * @param currency
     *            the currency
     * @return the currency appended price
     */
    private String getCurrencyAppendedPrice(final BigDecimal price,
            final Currency currency) {
        return currency.getSymbol() + price.toString();
    }

    /**
     * The method fetches the package model from the cart.
     *
     * @return BasePackage
     */
    private BasePackage getPackageModel() {
        return packageCartService.getBasePackage();
    }

    /**
     * Checks whether the extra facility is present with the same quantity in
     * package model or not. If yes it returns true otherwise false
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @param extraFacilityCategoryViewData
     *            the target
     * @param packageModel
     *            the package model
     * @return true, if successful
     */
    private boolean checkExtraFacility(final ExtraFacility extraFacilityModel,
            final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
            final BasePackage packageModel) {
        boolean isExtraFacilityPresent = false;
        for (final ExtraFacilityCategory extraFacilityCategoryModel : packageModel
                .getExtraFacilityCategories()) {
            if (StringUtils.equalsIgnoreCase(extraFacilityCategoryModel
                    .getCode(), extraFacilityCategoryViewData
                    .getExtraFacilityCategoryCode())) {
                isExtraFacilityPresent = isExtraFacilityPresent(
                        extraFacilityModel, extraFacilityCategoryViewData,
                        extraFacilityCategoryModel);
            }

        }
        return isExtraFacilityPresent;
    }

    /**
     * Checks if is extra facility present.
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @param extraFacilityCategoryViewData
     *            the extra facility category view data
     * @param extraFacilityCategoryModel
     *            the extra facility category model
     * @return true, if is extra facility present
     */
    private boolean isExtraFacilityPresent(
            final ExtraFacility extraFacilityModel,
            final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
            final ExtraFacilityCategory extraFacilityCategoryModel) {
        boolean isExtraFacilityPresent = false;
        for (final ExtraFacility extra : extraFacilityCategoryModel
                .getExtraFacilities()) {
            for (final ExtraFacilityViewData extraFacilityViewData : extraFacilityCategoryViewData
                    .getExtraFacilityViewData()) {
                if (checkIfCodeAndQuantityMatches(extraFacilityModel, extra,
                        extraFacilityViewData)) {
                    isExtraFacilityPresent = true;
                }

            }

        }
        return isExtraFacilityPresent;
    }

    /**
     * Check if code and quantity matches.
     *
     * @param extraFacilityModel
     *            the extra facility model
     * @param extra
     *            the extra
     * @param extraFacilityViewData
     *            the extra facility view data
     * @return true, if successful
     */
    private boolean checkIfCodeAndQuantityMatches(
            final ExtraFacility extraFacilityModel, final ExtraFacility extra,
            final ExtraFacilityViewData extraFacilityViewData) {
        return StringUtils.equalsIgnoreCase(extraFacilityViewData.getCode(),
                extraFacilityModel.getExtraFacilityCode())
                && StringUtils.equalsIgnoreCase(
                        extraFacilityViewData.getCode(),
                        extra.getExtraFacilityCode())
                && extra.getPrices().get(0).getQuantity().intValue() == extraFacilityViewData
                        .getSelectedQuantity();
    }

}
