/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.Collection;

/**
 * @author pooja.ps
 *
 */
public class LocationCategoryResult
{

   private Collection<UnitData> countries;

   private Collection<UnitData> destinations;

   private Collection<UnitData> hotels;

   private Collection<UnitData> safari;

   private Collection<UnitData> nileOrGuletCruise;

   private Collection<UnitData> lapland;

   private Collection<UnitData> twinOrMultiCenter;

   private Collection<ProductData> collections;

   /** Added as non core product range should be displayed under other */
   private Collection<ProductData> other;

   private boolean nomatch;

   private Collection<UnitData> tourAndStay;

   /**
    * @return the tourAndStay
    */
   public Collection<UnitData> getTourAndStay()
   {
      return tourAndStay;
   }

   /**
    * @param tourAndStay the tourAndStay to set
    */
   public void setTourAndStay(final Collection<UnitData> tourAndStay)
   {
      this.tourAndStay = tourAndStay;
   }

   /**
    * @return the countries
    */
   public Collection<UnitData> getCountries()
   {
      return countries;
   }

   /**
    * @return the safari
    */
   public Collection<UnitData> getSafari()
   {
      return safari;
   }

   /**
    * @param safari the safari to set
    */
   public void setSafari(final Collection<UnitData> safari)
   {
      this.safari = safari;
   }

   /**
    * @return the nileOrGuletCruise
    */
   public Collection<UnitData> getNileOrGuletCruise()
   {
      return nileOrGuletCruise;
   }

   /**
    * @param nileOrGuletCruise the nileOrGuletCruise to set
    */
   public void setNileOrGuletCruise(final Collection<UnitData> nileOrGuletCruise)
   {
      this.nileOrGuletCruise = nileOrGuletCruise;
   }

   /**
    * @return the lapland
    */
   public Collection<UnitData> getLapland()
   {
      return lapland;
   }

   /**
    * @param lapland the lapland to set
    */
   public void setLapland(final Collection<UnitData> lapland)
   {
      this.lapland = lapland;
   }

   /**
    * @return the twinOrMultiCenter
    */
   public Collection<UnitData> getTwinOrMultiCenter()
   {
      return twinOrMultiCenter;
   }

   /**
    * @param twinOrMultiCenter the twinOrMultiCenter to set
    */
   public void setTwinOrMultiCenter(final Collection<UnitData> twinOrMultiCenter)
   {
      this.twinOrMultiCenter = twinOrMultiCenter;
   }

   /**
    * @param countries the countries to set
    */
   public void setCountries(final Collection<UnitData> countries)
   {
      this.countries = countries;
   }

   /**
    * @return the destinations
    */
   public Collection<UnitData> getDestinations()
   {
      return destinations;
   }

   /**
    * @param destinations the destinations to set
    */
   public void setDestinations(final Collection<UnitData> destinations)
   {
      this.destinations = destinations;
   }

   /**
    * @return the hotels
    */
   public Collection<UnitData> getHotels()
   {
      return hotels;
   }

   /**
    * @return the collections
    */
   public Collection<ProductData> getCollections()
   {
      return collections;
   }

   /**
    * @param collections the collections to set
    */
   public void setCollections(final Collection<ProductData> collections)
   {
      this.collections = collections;
   }

   /**
    * @param hotels the hotels to set
    */
   public void setHotels(final Collection<UnitData> hotels)
   {
      this.hotels = hotels;
   }

   /**
    * @return the nomatch
    */
   public boolean isNomatch()
   {
      return nomatch;
   }

   /**
    * @param nomatch the nomatch to set
    */
   public void setNomatch(final boolean nomatch)
   {
      this.nomatch = nomatch;
   }

   /**
    * @return the other
    */
   public Collection<ProductData> getOther()
   {
      return other;
   }

   /**
    * @param other the other to set
    */
   public void setOther(final Collection<ProductData> other)
   {
      this.other = other;
   }

}
