/**
 *
 */
package uk.co.tui.manage.viewdata;

import java.util.Collection;

import uk.co.tui.book.domain.lite.Room;



/**
 * @author lakshmipathi.d
 *
 */
public class PassengerViewData
{
    private Integer identifier;


    private String type;

    private Integer age;

    private boolean isLead;

    private String title;

    private PassengerAddressViewData address;

    private Collection<Room> rooms;

    private boolean isInsured;
    private boolean isWelfare;

    private PaxFlightExtraMappingViewData paxExtraMappingData = new PaxFlightExtraMappingViewData();

    private int noOfAdults;

    private int noOfChildren;



    /**
     * @return the identifier
     */
    public Integer getIdentifier()
    {
        return identifier;
    }

    /**
     * @param identifier
     *           the identifier to set
     */
    public void setIdentifier(final Integer identifier)
    {
        this.identifier = identifier;
    }

    /**
     * @return the isWelfare
     */
    public boolean isWelfare()
    {
        return isWelfare;
    }

    /**
     * @param isWelfare
     *           the isWelfare to set
     */
    public void setWelfare(final boolean isWelfare)
    {
        this.isWelfare = isWelfare;
    }

    /**
     * @return the isInsured
     */
    public boolean isInsured()
    {
        return isInsured;
    }

    /**
     * @param isInsured
     *           the isInsured to set
     */
    public void setInsured(final boolean isInsured)
    {
        this.isInsured = isInsured;
    }

    /**
     * @return the rooms
     */
    public Collection<Room> getRooms()
    {
        return rooms;
    }

    /**
     * @param rooms
     *           the rooms to set
     */
    public void setRooms(final Collection<Room> rooms)
    {
        this.rooms = rooms;
    }



    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     *           the type to set
     */
    public void setType(final String type)
    {
        this.type = type;
    }

    /**
     * @return the age
     */
    public Integer getAge()
    {
        return age;
    }

    /**
     * @param age
     *           the age to set
     */
    public void setAge(final Integer age)
    {
        this.age = age;
    }

    /**
     * @return the isLead
     */
    public boolean isLead()
    {
        return isLead;
    }

    /**
     * @param isLead
     *           the isLead to set
     */
    public void setLead(final boolean isLead)
    {
        this.isLead = isLead;
    }

    /**
     * @return the address
     */
    public PassengerAddressViewData getAddress()
    {
        return address;
    }

    /**
     * @param address
     *           the address to set
     */
    public void setAddress(final PassengerAddressViewData address)
    {
        this.address = address;
    }

    /**
     * @param title
     *           the title to set
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return the noOfAdults
     */
    public int getNoOfAdults()
    {
        return noOfAdults;
    }

    /**
     * @param noOfAdults
     *           the noOfAdults to set
     */
    public void setNoOfAdults(final int noOfAdults)
    {
        this.noOfAdults = noOfAdults;
    }

    /**
     * @return the noOfChildren
     */
    public int getNoOfChildren()
    {
        return noOfChildren;
    }

    /**
     * @param noOfChildren
     *           the noOfChildren to set
     */
    public void setNoOfChildren(final int noOfChildren)
    {
        this.noOfChildren = noOfChildren;
    }

    /**
     * @return the paxExtraMappingData
     */
    public PaxFlightExtraMappingViewData getPaxExtraMappingData()
    {
        return paxExtraMappingData;
    }

    /**
     * @param paxExtraMappingData
     *           the paxExtraMappingData to set
     */
    public void setPaxExtraMappingData(final PaxFlightExtraMappingViewData paxExtraMappingData)
    {
        this.paxExtraMappingData = paxExtraMappingData;
    }




}
