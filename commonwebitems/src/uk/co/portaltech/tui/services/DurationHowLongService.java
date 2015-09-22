/**
 *
 */
package uk.co.portaltech.tui.services;

import java.util.List;

import uk.co.portaltech.travel.model.HowLongDurationModel;


/**
 * @author kavita.na
 *
 */
public interface DurationHowLongService
{
    List<HowLongDurationModel> getDurationForCode(final int code, String brandType);

    List<HowLongDurationModel> getDurations(String brandType);


}
