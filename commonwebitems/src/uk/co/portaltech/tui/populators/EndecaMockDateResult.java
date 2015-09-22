/**
 *
 */
package uk.co.portaltech.tui.populators;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.wrapper.SearchResultDateSelectionViewData;

/**
 * @author laxmibai.p
 *
 */
public final class EndecaMockDateResult {


    private EndecaMockDateResult()
    {

    }

    public static SearchResultDateSelectionViewData getEndecaMockResult()
    {
        SearchResultDateSelectionViewData viewdata1 = new SearchResultDateSelectionViewData();


         List<String> mockdate= new ArrayList<String>();

         mockdate.add("13-01-2013");
         mockdate.add("15-01-2013");
         mockdate.add("18-01-2013");
         mockdate.add("21-01-2013");
         mockdate.add("22-01-2013");
         mockdate.add("25-01-2013");
         mockdate.add("27-01-2013");


        viewdata1.setAvailableValues(mockdate);

        viewdata1.setMaxValue("13-01-2013");
        viewdata1.setMinValue("27-01-2013");




        return viewdata1;
    }

}
