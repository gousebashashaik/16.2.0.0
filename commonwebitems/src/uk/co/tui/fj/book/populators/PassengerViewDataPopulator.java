/**
 *
 */
package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.fj.book.view.data.BaggageExtraFacilityViewData;
import uk.co.tui.fj.book.view.data.ExtraFacilityViewData;
import uk.co.tui.fj.book.view.data.PassengerViewData;

/**
 * @author munisekhar.k
 *
 */
public class PassengerViewDataPopulator implements Populator<Passenger, PassengerViewData>{

    @Resource(name = "fjExtraFacilityViewDataPopulator")
    private ExtraFacilityViewDataPopulator extraFacilityViewDataPopulator;
    /* (non-Javadoc)
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("boxing")
    @Override
    public void populate(Passenger source, PassengerViewData target)
            throws ConversionException {
        target.setIdentifier(source.getId());
        target.setType(source.getType().toString());
        if (source.getAge() != null)
        {
           target.setAge(source.getAge());
        }
        if (CollectionUtils.isNotEmpty(source.getExtraFacilities())) {
            for (ExtraFacility extra : source.getExtraFacilities()) {
                populateBagAndMeal(target, extra);
            }

        }
    }
    /**
     * @param target
     * @param extra
     */
    private void populateBagAndMeal(PassengerViewData target,
            ExtraFacility extra) {
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
    private void populateBaggage(PassengerViewData target,
            ExtraFacility extra) {
        BaggageExtraFacilityViewData bagViewData = new BaggageExtraFacilityViewData();
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
    private void populateMeal(PassengerViewData target, ExtraFacility extra) {
        ExtraFacilityViewData extraViewData = new ExtraFacilityViewData();
        extraFacilityViewDataPopulator.populate(extra,
                extraViewData);
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
