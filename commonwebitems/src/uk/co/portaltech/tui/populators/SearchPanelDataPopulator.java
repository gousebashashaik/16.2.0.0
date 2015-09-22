package uk.co.portaltech.tui.populators;

/*
 * Originating Unit: Portal Technology Systems Ltd
 * http://www.portaltech.co.uk
 *
 * Copyright Portal Technology Systems Ltd.
 *
 * $Id: $
 */

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.SearchPanelViewData;


/**
 * A basic class to convert a SearchPanelComponentModel into our SearchPanelViewData.
 *
 * @author Manju
 */
public class SearchPanelDataPopulator implements Populator<SearchPanelComponentModel, SearchPanelViewData>
{


    /*
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final SearchPanelComponentModel sourceModel, final SearchPanelViewData targetData)
            throws ConversionException
    {
        Assert.notNull(sourceModel, "Converter source must not be null");
        Assert.notNull(targetData, "Converter target must not be null");

        targetData.setMaxAirportsSelectable(sourceModel.getMaxAirportsSelectable().intValue());
        targetData.setMinNoOfAdult(sourceModel.getMinNoOfAdult().intValue());
        targetData.setFlexibleDays(sourceModel.getFlexibleDays().intValue());
        targetData.setFirst(sourceModel.getFirst().intValue());
        targetData.setLast(sourceModel.getLast().intValue());
        targetData.setInfantAge(sourceModel.getInfantAge().intValue());
        targetData.setOffset(sourceModel.getOffset().intValue());
        targetData.setPersistedSearchPeriod(sourceModel.getPersistedSearchPeriod().intValue());
        targetData.setSeasonLength(sourceModel.getSeasonLength().intValue());
        targetData.setMaxAirportsSelectable(sourceModel.getMaxAirportsSelectable().intValue());
        targetData.setMaxNoOfAdult(sourceModel.getMaxNoOfAdult().intValue());

    }
}
