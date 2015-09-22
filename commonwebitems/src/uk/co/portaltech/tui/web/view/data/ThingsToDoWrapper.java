/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Abi
 *
 */
public class ThingsToDoWrapper {

    private List<AttractionViewData> events;
    private List<AttractionViewData> sights;
    private List<ExcursionViewData>  excursions;
    private String heading;

    public ThingsToDoWrapper() {
        events = new ArrayList<AttractionViewData>();
        sights = new ArrayList<AttractionViewData>();
        excursions = new ArrayList<ExcursionViewData>();

    }

    /**
     * @return the events
     */
    public List<AttractionViewData> getEvents() {
        return events;
    }

    /**
     * @param events
     *            the events to set
     */
    public void setEvents(List<AttractionViewData> events) {
        this.events = events;
    }

    /**
     * @return the sights
     */
    public List<AttractionViewData> getSights() {
        return sights;
    }

    /**
     * @param sights
     *            the sights to set
     */
    public void setSights(List<AttractionViewData> sights) {
        this.sights = sights;
    }

    /**
     * @return the excursions
     */
    public List<ExcursionViewData> getExcursions() {
        return excursions;
    }

    /**
     * @param excursions
     *            the excursions to set
     */
    public void setExcursions(List<ExcursionViewData> excursions) {
        this.excursions = excursions;
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param heading the heading to set
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }



}
