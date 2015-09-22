/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.th.book.view.data.GenericStaticContentViewData;

import uk.co.tui.th.book.view.data.RoomOptionsStaticContentViewData;



/**
 * Note : Currently there is no valid source to pass as parameter, hence passing it as Object
 * @author thyagaraju.e
 *
 */
public class RoomStaticContentViewDataPopulator implements Populator<Object, RoomOptionsStaticContentViewData>{

    @Resource
    private DefaultStaticContentWrapper staticContentServ;

    @Resource(name = "thPageContentViewDataPopulator")
    private PageContentViewDataPopulator pageContentViewDataPopulator;

    /**
     * Populate room content.
     *
     * @param roomViewData the room view data
     */
    private void populateRoomContent(
            final RoomOptionsStaticContentViewData roomViewData) {
        final GenericStaticContentViewData genericViewData = new GenericStaticContentViewData();


        roomViewData.setRoomContentMap(staticContentServ
                    .getRoomContents());

        pageContentViewDataPopulator.populateGenericContent(genericViewData);


        roomViewData.setGenericContentViewData(genericViewData);

    }

    /**
     * Calls the room content population
     */
    @Override
    public void populate(final Object source, final RoomOptionsStaticContentViewData target)
            throws ConversionException {
        populateRoomContent(target);

    }

}
