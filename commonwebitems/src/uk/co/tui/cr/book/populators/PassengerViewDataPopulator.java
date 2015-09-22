/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.cr.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.ExtraFacilityViewData;
import uk.co.tui.cr.book.view.data.PassengerViewData;

/**
 * @author munisekhar.k
 *
 */
public class PassengerViewDataPopulator
        implements
            Populator<Passenger, PassengerViewData> {

    @Resource(name = "crExtraFacilityViewDataPopulator")
    private ExtraFacilityViewDataPopulator extraFacilityViewDataPopulator;
    /*
     * (non-Javadoc)
     *
     * @see
     * de.hybris.platform.commerceservices.converter.Populator#populate(java
     * .lang.Object, java.lang.Object)
     */
    @SuppressWarnings("boxing")
    @Override
    public void populate(final Passenger source, final PassengerViewData target)
            throws ConversionException {
        target.setIdentifier(source.getId());
        target.setType(source.getType().toString());
        if (source.getAge() != null) {
            target.setAge(source.getAge());
        }
        if (CollectionUtils.isNotEmpty(source.getExtraFacilities())) {
            for (final ExtraFacility extra : source.getExtraFacilities()) {
                populateBagAndMeal(target, extra);
            }

        }
    }
    /**
     * @param target
     * @param extra
     */
    private void populateBagAndMeal(final PassengerViewData target,
            final ExtraFacility extra) {
        if (StringUtils.equalsIgnoreCase(extra.getExtraFacilityCategory()
                .getCode(), "BAG")) {
            populateBaggage(target, extra);
        } else if (StringUtils.equalsIgnoreCase(extra
                .getExtraFacilityCategory().getCode(), "MEAL")) {
            populateMeal(target, extra);

        }
    }
    /**
     * @param target
     * @param extra
     */
    private void populateBaggage(final PassengerViewData target,
            final ExtraFacility extra) {
        final BaggageExtraFacilityViewData bagViewData = new BaggageExtraFacilityViewData();
        extraFacilityViewDataPopulator.populate(extra, bagViewData);
        if (target.getSelectedBaggageOption() == null) {
            target.setSelectedBaggageOption(bagViewData);
        } else {
            target.getSelectedBaggageOption().setPrice(
                    target.getSelectedBaggageOption().getPrice()
                            .add(bagViewData.getPrice()));
        }
    }
    /**
     * @param target
     * @param extra
     */
    private void populateMeal(final PassengerViewData target,
            final ExtraFacility extra) {
        final ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
        extraFacilityViewDataPopulator.populate(extra, extraViewData);
        if (target.getSelectedMealOption() == null) {
            target.setSelectedMealOption(extraViewData);
        } else {
            target.getSelectedMealOption().setAdultPrice(
                    target.getSelectedMealOption().getAdultPrice()
                            .add(extraViewData.getAdultPrice()));
            target.getSelectedMealOption().setChildPrice(
                    target.getSelectedMealOption().getChildPrice()
                            .add(extraViewData.getChildPrice()));
        }
    }
}
