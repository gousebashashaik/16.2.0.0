/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.ExtraFacilityConstants;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.ExtraFacilityCategory;
import uk.co.tui.book.domain.lite.FacilitySelectionType;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.th.book.view.data.CarHireExtraFacilityViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityCategoryViewData;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;

/**
 * @author samantha.gd
 *
 */
public class CarHireUpgradeViewDataPopulatorLite
        implements
            Populator<List<ExtraFacilityCategory>, List<ExtraFacilityCategoryViewData>> {

    /** Package Cart Service. */
    @Resource
    private PackageCartService packageCartService;

    /** The generic content service. */
    @Resource
    private GenericContentService genericContentService;

    @Resource
    private CurrencyResolver currencyResolver;

    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void populate(
            final List<ExtraFacilityCategory> extraFacilityCategoryList,
            final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewList)
            throws ConversionException {
        final Price selectCarPriceModel = getSelectedCarPriceModel();
        final BigDecimal selectedCarPrice = getSelectedCarPrice(selectCarPriceModel);
        CarHireExtraFacilityViewData basicCarHireViewData = null;
        for (final ExtraFacilityCategory extraFacilityCategory : extraFacilityCategoryList) {
            if (isBasicAlamoCarHire(extraFacilityCategoryList,
                    extraFacilityCategory)) {
                basicCarHireViewData = populateExtraFacilityViewData(
                        selectedCarPrice, extraFacilityCategory
                                .getExtraFacilities().get(0));
            } else {
                populateExtraFacilityCategoryViewData(
                        extraFacilityCategoryViewList, selectedCarPrice,
                        extraFacilityCategory);
            }
        }

        addBasicCarHireViewData(extraFacilityCategoryViewList,
                basicCarHireViewData);

    }

    /**
     * This method populates ExtraFacilityCategoryViewData
     *
     * @param extraFacilityCategoryViewList
     * @param selectedCarPrice
     * @param extraFacilityCategory
     */
    private void populateExtraFacilityCategoryViewData(
            final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewList,
            final BigDecimal selectedCarPrice,
            final ExtraFacilityCategory extraFacilityCategory) {

        ExtraFacilityCategoryViewData extraFacilityCategoryViewData = (ExtraFacilityCategoryViewData) CollectionUtils
                .find(extraFacilityCategoryViewList, new Predicate() {
                    @Override
                    public boolean evaluate(final Object object) {
                        return StringUtils.equalsIgnoreCase(
                                ((ExtraFacilityCategoryViewData) object)
                                        .getExtraFacilityCategoryCode(),
                                extraFacilityCategory.getCode());
                    }
                });

        final List<ExtraFacilityViewData> extraFacilities = populateExtraFacilities(
                extraFacilityCategory.getExtraFacilities(), selectedCarPrice);

        if (extraFacilityCategoryViewData == null) {
            extraFacilityCategoryViewData = new ExtraFacilityCategoryViewData();
            extraFacilityCategoryViewData.setAvailable(true);
            extraFacilityCategoryViewData
                    .setExtraFacilityCategoryCode(extraFacilityCategory
                            .getCode());
            extraFacilityCategoryViewData
                    .setSuperCategoryCode(extraFacilityCategory
                            .getSuperCategoryCode());
            extraFacilityCategoryViewData
                    .setExtraFacilityGroupCode(extraFacilityCategory
                            .getExtraFacilityGroup() != null
                            ? extraFacilityCategory.getExtraFacilityGroup()
                                    .name() : null);
            extraFacilityCategoryViewData
                    .setExtraFacilityViewData(extraFacilities);
            populateCarHireContent(extraFacilityCategoryViewData,
                    extraFacilityCategory);
            extraFacilityCategoryViewList.add(extraFacilityCategoryViewData);
        } else {
            extraFacilityCategoryViewData.getExtraFacilityViewData().addAll(
                    extraFacilities);
        }
        if (!extraFacilityCategoryViewData.isSelected()) {
            extraFacilityCategoryViewData
                    .setSelected(isExtraFacilitySelected(extraFacilities));
        }
    }

    /**
     * Populate car hire content.
     *
     * @param extraFacilityCategoryViewData
     *            the extra facility category view data
     * @param extraFacilityCategory
     *            the extra facility category model
     */
    private void populateCarHireContent(
            final ExtraFacilityCategoryViewData extraFacilityCategoryViewData,
            final ExtraFacilityCategory extraFacilityCategory) {
        final List<DynamicContentConfigModel> dynamicContents = genericContentService
                .getDynamicContentConfig(extraFacilityCategory.getCode(),
                        StringUtils.EMPTY, StringUtils.EMPTY);
        for (final DynamicContentConfigModel dynamicContent : dynamicContents) {
            extraFacilityCategoryViewData.getCarHireContentMap().putAll(
                    genericContentService
                            .getGenericDynamicContentValue(dynamicContent));
        }
    }

    /**
     * Get selected car price
     *
     * @param selectCarPriceModel
     * @return price
     */
    private BigDecimal getSelectedCarPrice(final Price selectCarPriceModel) {
        return selectCarPriceModel != null ? selectCarPriceModel.getAmount()
                .getAmount() : BigDecimal.ZERO;
    }

    /**
     * This method checks of if it is ALAMO basic car hire.
     *
     * @param extraFacilityCategoryList
     * @param extraFacilityCategory
     * @return boolean
     */
    private boolean isBasicAlamoCarHire(
            final List<ExtraFacilityCategory> extraFacilityCategoryList,
            final ExtraFacilityCategory extraFacilityCategory) {
        return StringUtils.equalsIgnoreCase("BASIC",
                extraFacilityCategory.getCode())
                && extraFacilityCategoryList.size() > 1
                && CollectionUtils.isNotEmpty(extraFacilityCategory
                        .getExtraFacilities())
                && StringUtils.equalsIgnoreCase(extraFacilityCategory
                        .getExtraFacilities().get(0).getExtraFacilityCode(),
                        ExtraFacilityConstants.BASIC_CARHIRE);
    }

    /**
     * Adds basic car hire view data
     *
     * @param extraFacilityCategoryViewList
     * @param basicCarHireViewData
     */
    private void addBasicCarHireViewData(
            final List<ExtraFacilityCategoryViewData> extraFacilityCategoryViewList,
            final CarHireExtraFacilityViewData basicCarHireViewData) {
        if (CollectionUtils.isNotEmpty(extraFacilityCategoryViewList)
                && (basicCarHireViewData != null)) {
            extraFacilityCategoryViewList.get(0).getExtraFacilityViewData()
                    .add(0, basicCarHireViewData);
        }
    }

    /**
     * Populate extra facilities.
     *
     * @param list
     *            the extra facilities
     * @param selectedCarPrice
     *            the selected car price
     * @return the list
     */
    private List<ExtraFacilityViewData> populateExtraFacilities(
            final List<ExtraFacility> list, final BigDecimal selectedCarPrice) {
        final List<ExtraFacilityViewData> extraFacilityViewDataList = new ArrayList<ExtraFacilityViewData>();
        CarHireExtraFacilityViewData extraFacilityViewData = null;
        for (final ExtraFacility extraFacilityModel : list) {
            extraFacilityViewData = populateExtraFacilityViewData(
                    selectedCarPrice, extraFacilityModel);
            extraFacilityViewDataList.add(extraFacilityViewData);

        }
        return extraFacilityViewDataList;
    }

    /**
     * Populate extra facility view data.
     *
     * @param selectedCarPrice
     *            the selected car price
     * @param extraFacility
     *            the extra facility model
     * @return the car hire extra facility view data
     */
    private CarHireExtraFacilityViewData populateExtraFacilityViewData(
            final BigDecimal selectedCarPrice, final ExtraFacility extraFacility) {
        CarHireExtraFacilityViewData extraFacilityViewData;
        extraFacilityViewData = new CarHireExtraFacilityViewData();
        final BigDecimal carUpgradePrice = extraFacility.getPrices().get(0)
                .getAmount().getAmount();
        extraFacilityViewData.setCode(extraFacility.getExtraFacilityCode());
        extraFacilityViewData.setType(extraFacility.getExtraFacilityCategory()
                .getAliasSuperCategoryCode());
        extraFacilityViewData.setCategoryCode(extraFacility
                .getExtraFacilityCategory().getCode());
        extraFacilityViewData.setSelected(false);
        extraFacilityViewData.setDescription(extraFacility.getDescription());
        extraFacilityViewData
                .setFree(extraFacility.getSelection() == FacilitySelectionType.FREE);
        extraFacilityViewData
                .setCurrencyAppendedPrice(populateCurrencyAppendedPrice(
                        selectedCarPrice, carUpgradePrice));
        extraFacilityViewData.setPrice(carUpgradePrice);
        extraFacilityViewData
                .setCurrencyAppendedPricePerNight(populateCurrencyAppendedPricePerDay(
                        selectedCarPrice, carUpgradePrice));
        return extraFacilityViewData;
    }

    /**
     * Gets the selected car price.
     *
     * @return the selected car price
     */
    private Price getSelectedCarPriceModel() {
        for (final ExtraFacilityCategory extraFacilityCategoryModel : getPackageModel()
                .getExtraFacilityCategories()) {
            if (StringUtils.equalsIgnoreCase(
                    extraFacilityCategoryModel.getSuperCategoryCode(),
                    ExtraFacilityConstants.CAR_HIRE)) {
                return extraFacilityCategoryModel.getExtraFacilities().get(0)
                        .getPrices().get(0);
            }
        }
        return null;
    }

    /**
     * Populate currency appended price.
     *
     * @param selectedCarPrice
     *            the selected car price
     * @param carUpgradePrice
     *            the car upgrade price
     * @return the string
     */
    private String populateCurrencyAppendedPrice(
            final BigDecimal selectedCarPrice, final BigDecimal carUpgradePrice) {
        return getCurrencyAppendedPrice(carUpgradePrice
                .subtract(selectedCarPrice),currencyResolver.getSiteCurrency());

    }

    /**
     * Gets the currency appended price.
     *
     * @param price the price
     * @param currencyCode the currency symbol
     * @return the currency appended price
     */
    private static String getCurrencyAppendedPrice(final BigDecimal price, final String currencyCode)
    {
        String formattedPrice = StringUtils.EMPTY;
        if (price.compareTo(BigDecimal.ZERO) < 0)
        {
            formattedPrice = StringUtil.append("-", CurrencyUtils.getCurrencySymbol(currencyCode), String.valueOf(price.negate()));
            }
        else
        {
            formattedPrice = StringUtil.append("+", CurrencyUtils.getCurrencySymbol(currencyCode), String.valueOf(price));
        }
        return formattedPrice;
    }

    /**
     * Populate currency appended price per day.
     *
     * @param selectedCarPrice
     *            the selected car price
     * @param carUpgradePrice
     *            the car upgrade price
     * @return the string
     */
    private String populateCurrencyAppendedPricePerDay(
            final BigDecimal selectedCarPrice, final BigDecimal carUpgradePrice) {
        final BigDecimal duration = new BigDecimal(getPackageModel()
                .getDuration().intValue());
        final BigDecimal perdayPrice = carUpgradePrice.subtract(
                selectedCarPrice).divide(duration, RoundingMode.HALF_UP);
        return getCurrencyAppendedPrice(perdayPrice,currencyResolver.getSiteCurrency());
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
     * Checks if is extra facility selected.
     *
     * @param extraFacilities
     *            the extra facilities
     * @return true, if is extra facility selected
     */
    private boolean isExtraFacilitySelected(
            final List<ExtraFacilityViewData> extraFacilities) {
        for (final ExtraFacilityViewData extraFacilityViewData : extraFacilities) {
            if (extraFacilityViewData.isSelected()) {
                return extraFacilityViewData.isSelected();
            }
        }
        return false;
    }

}
