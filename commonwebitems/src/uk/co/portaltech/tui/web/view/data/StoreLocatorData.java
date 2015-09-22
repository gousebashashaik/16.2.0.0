/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.HashMap;
import java.util.Map;


/**
 * @author akhileshvarma.d
 *
 */
public class StoreLocatorData
{
    private String name;
    private String street;
    private String building;
    private String zip;
    private String city;
    private String town;
    private String countryCode;
    private Map<String, String> timings = new HashMap<String, String>();
    private String mapIconUrl;
    private double distance;
    private String latitude;
    private String longitude;
    private String email;
    private String phone;


    /**
     * @return the latitude
     */
    public String getLatitude()
    {
        return latitude;
    }

    /**
     * @param latitude
     *           the latitude to set
     */
    public void setLatitude(final String latitude)
    {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude()
    {
        return longitude;
    }

    /**
     * @param longitude
     *           the longitude to set
     */
    public void setLongitude(final String longitude)
    {
        this.longitude = longitude;
    }

    /**
     * @return the mapIconUrl
     */
    public String getMapIconUrl()
    {
        return mapIconUrl;
    }

    /**
     * @param mapIconUrl
     *           the mapIconUrl to set
     */
    public void setMapIconUrl(final String mapIconUrl)
    {
        this.mapIconUrl = mapIconUrl;
    }

    /**
     * @return the distance
     */
    public double getDistance()
    {
        return distance;
    }

    /**
     * @param distance
     *           the distance to set
     */
    public void setDistance(final double distance)
    {
        this.distance = distance;
    }

    /**
     * @return the timings
     */
    public Map<String, String> getTimings()
    {
        return timings;
    }

    /**
     * @param timings
     *           the timings to set
     */
    public void setTimings(final Map<String, String> timings)
    {
        this.timings = timings;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the street
     */
    public String getStreet()
    {
        return street;
    }

    /**
     * @param street
     *           the street to set
     */
    public void setStreet(final String street)
    {
        this.street = street;
    }

    /**
     * @return the building
     */
    public String getBuilding()
    {
        return building;
    }

    /**
     * @param building
     *           the building to set
     */
    public void setBuilding(final String building)
    {
        this.building = building;
    }

    /**
     * @return the zip
     */
    public String getZip()
    {
        return zip;
    }

    /**
     * @param zip
     *           the zip to set
     */
    public void setZip(final String zip)
    {
        this.zip = zip;
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city
     *           the city to set
     */
    public void setCity(final String city)
    {
        this.city = city;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode()
    {
        return countryCode;
    }

    /**
     * @param countryCode
     *           the countryCode to set
     */
    public void setCountryCode(final String countryCode)
    {
        this.countryCode = countryCode;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email
     *           the email to set
     */
    public void setEmail(final String email)
    {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * @param phone
     *           the phone to set
     */
    public void setPhone(final String phone)
    {
        this.phone = phone;
    }

    /**
     * @return the town
     */
    public String getTown()
    {
        return town;
    }

    /**
     * @param town
     *           the town to set
     */
    public void setTown(final String town)
    {
        this.town = town;
    }
}
