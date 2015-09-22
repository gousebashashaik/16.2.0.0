/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.portaltech.tui.web.view.data.GenericContentViewData;


import uk.co.portaltech.travel.model.ContentValueModel;
/**
 * GenericContentPopulator.java
 * <p>
 * Represents to populate generic data from the generic ContentValueModel to GenericContentViewData.
 *
 * @author narendra.bm
 *
 */
public class GenericContentPopulator implements Populator<ContentValueModel, GenericContentViewData>
{



    /**
     * (non-Javadoc)
     *
     * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
     *
     * @param contentValueModel
     * @param genericContentViewData
     */
    @Override
    public void populate(ContentValueModel contentValueModel, final GenericContentViewData genericContentViewData)
            throws ConversionException
    {
        genericContentViewData.setContent((String) contentValueModel.getValue());
    }


}
