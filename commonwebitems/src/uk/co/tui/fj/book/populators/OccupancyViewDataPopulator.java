package uk.co.tui.fj.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;

import uk.co.tui.book.domain.Occupancy;
import uk.co.tui.fj.book.view.data.OccupancyViewData;


/**
 * The Class OccupancyViewDataPopulator.
 */
public class OccupancyViewDataPopulator implements Populator<Occupancy, OccupancyViewData>
{

    /**
     * Gets the occupancy view data.
     *
     * @param roomOccupancy
     *           the occupancy
     * @param occupancyViewData
     *           the occupancy view data
     */
    @Override
    public void populate(final Occupancy roomOccupancy, final OccupancyViewData occupancyViewData)
    {
        occupancyViewData.setAdult(roomOccupancy.getAllocatedAdults());
        occupancyViewData.setChild(roomOccupancy.getAllocatedChild());
        occupancyViewData.setInfant(roomOccupancy.getAllocatedInfants());

    }
}