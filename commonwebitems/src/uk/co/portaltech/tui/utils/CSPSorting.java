/**
 *
 */
package uk.co.portaltech.tui.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author geethanjali.k
 *
 */
public final class CSPSorting
{

    private CSPSorting()
    {

    }

    public static List<AccommodationViewData> getCSPSortingOrder(final List<AccommodationViewData> accomViewData)
    {

        class CSPComparator implements Comparator<AccommodationViewData>
        {
            @Override
            public int compare(final AccommodationViewData accomViewData1, final AccommodationViewData accomViewData2)
            {
                final int cspSoring1 = accomViewData1.getComPriority();
                final int cspSoring2 = accomViewData2.getComPriority();

                if (cspSoring1 == cspSoring2)
                {
                    return 0;
                }
                else if (cspSoring1 > cspSoring2)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
        }

        Collections.sort(accomViewData, Collections.reverseOrder(new CSPComparator()));

        return accomViewData;
    }
}
