/**
 *
 */
package uk.co.tui.fj.book.filter.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import uk.co.tui.book.filter.Filter;
import uk.co.tui.book.filter.FilterInput;
import uk.co.tui.th.book.view.data.ExtraFacilityViewData;

/**
 * Filter method to filter the selected extra facilities from the offered extraFacilities.
 * @author madhumathi.m
 *
 */
public class SelectedFlightExtraFacilityFilter implements
        Filter<ExtraFacilityViewData, FilterInput<ExtraFacilityViewData>> {

   /**
    * Filters the selected Extras from the list of offered extrafacilities.
    *
    * @param filterInput
    * @return selectedExtras
    */
   @Override
    public List<ExtraFacilityViewData> filter(
            FilterInput<ExtraFacilityViewData> filterInput) {
        List<ExtraFacilityViewData> selectedExtras = null;

        selectedExtras = (List<ExtraFacilityViewData>) CollectionUtils.select(
                filterInput.getInputCollection(), new Predicate() {
                    @Override
                    public boolean evaluate(final Object input) {
                        return requiredExtraFacilityViewData(input);
                    }

                    private boolean requiredExtraFacilityViewData(
                            final Object input) {
                        ExtraFacilityViewData data = ExtraFacilityViewData.class
                                .cast(input);
                        return data.isSelected() || data.isIncluded();
                    }
                });
      return selectedExtras;
   }

       /**
        * additional support data required for filtering.
        *
        * @param input
        * @return boolean
        */
    @Override
    public boolean supportsAdditionalData(
            FilterInput<ExtraFacilityViewData> input) {
        return false;
    }

}
