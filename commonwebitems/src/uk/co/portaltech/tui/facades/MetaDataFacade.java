/**
 *
 */
package uk.co.portaltech.tui.facades;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;

import uk.co.portaltech.tui.web.view.data.MetaDataViewData;

/**
 * @author cxw
 *
 */
public interface MetaDataFacade {
     MetaDataViewData getMetaDataForItem(ItemModel item,AbstractPageModel pageModel);
}
