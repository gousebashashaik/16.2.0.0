package uk.co.portaltech.tui.web.view.data;

import java.util.ArrayList;
import java.util.Collection;

import uk.co.portaltech.travel.model.airport.Airport;

public class AirportData
{
   private String id;

   private String name;

   private String synonym;

   private Collection<String> children;

   private Collection<String> group;

   private boolean available;

   public AirportData()
   {

   }

   public AirportData(final String id, final String name, final String synonym,
                      final Collection<String> children, final Collection<String> group)
   {
      this.id = id;
      this.name = name;
      this.synonym = synonym;
      this.children = children;
      this.group = group;
   }

   public AirportData(final String id, final String name)
   {
      this.id = id;
      this.name = name;
   }

   public AirportData(final String id, final String name, final String synonym,
                      final boolean available)
   {
      this.id = id;
      this.name = name;
      this.synonym = synonym;
      this.available = available;
   }

   /**
    * @param groups the groups to set
    */
   public void setGroups(final Collection<String> groups)
   {
      this.group = groups;
   }

   public String getId()
   {
      return id;
   }

   public void setId(final String id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public Collection<String> getChildren()
   {
      return children;
   }

   public void setChildren(final Collection<String> children)
   {
      this.children = children;
   }

   /**
    * @return the group
    */
   public Collection<String> getGroup()
   {
      return group;
   }

   public Collection<String> getGroups()
   {
      return group;
   }

   /**
    * @param group the group to set
    */
   public void setGroup(final Collection<String> group)
   {
      this.group = new ArrayList<String>();
      this.group.addAll(group);
   }

   @Override
   public boolean equals(final Object rhs)
   {
      if (this == rhs)
      {
         return true;
      }
      if (rhs == null || getClass() != rhs.getClass())
      {
         return false;
      }

      final AirportData airport = (AirportData) rhs;

      return this.id.equals(airport.id);
   }

   @Override
   public int hashCode()
   {
      return id.hashCode();
   }

   public static AirportData toAirportData(final Airport input)
   {
      final AirportData airportData =
         new AirportData(input.code(), input.name(), input.matchedSynonym(),
            input.isRouteAvailable());
      airportData.setGroup(input.parentAirportcodes());
      airportData.setChildren(input.childAirportCodes());
      return airportData;
   }

   public String getSynonym()
   {
      return synonym;
   }

   public void setSynonym(final String synonym)
   {
      this.synonym = synonym;
   }

   public boolean isAvailable()
   {
      return available;
   }

   public void setAvailable(final boolean available)
   {
      this.available = available;
   }
}
