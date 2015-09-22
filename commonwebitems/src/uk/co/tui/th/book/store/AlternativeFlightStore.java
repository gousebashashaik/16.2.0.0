/**
 *
 */
package uk.co.tui.th.book.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import uk.co.tui.book.domain.lite.BasePackage;

/**
 * The Class AlternativeFlightStore.
 *
 * @author samantha.gd
 */
public class AlternativeFlightStore
{

   private List<BasePackage> packageHolidays = new ArrayList<BasePackage>();

    /** The durations. */
    private List<String> durations;

    /**
    * Flushes the data stored in the store.
    */
   public void flush() {
        this.durations = null;
        this.packageHolidays = null;
   }


   /**
     * Gets the packages.
     *
     * @return the packages
     */
   public List<BasePackage> getPackageHoliday()
    {
      if (packageHolidays.isEmpty())
        {
         return new ArrayList<BasePackage>();
        }
      return packageHolidays;
    }

    /**
     * Gets the durations.
     *
     * @return the durations
     */
    public List<String> getDurations()
    {
        if (durations.isEmpty())
        {
            return new ArrayList<String>();
        }
        return durations;
    }


    /**
     * Adds the alternate packages.
     *
    * @param altPackages the alt packages
     */
   public void addAlternatePackagesHoliday(List<BasePackage> altPackages)
    {
      this.packageHolidays.addAll(altPackages);
    }

    /**
     * Gets the package.
     *
    * @param packageId the package id
     * @return the package
     */
   public BasePackage getPackageHoliday(String packageId)
    {
      for (BasePackage pkg : this.packageHolidays)
        {
            if (pkg.getId().equals(packageId))
            {
                return pkg;
            }
        }
        return null;
    }

    /**
     * Appends the duration to the existing durations in the store.
     *
    * @param duration the duration
     */
   public void addDuration(String duration)
    {
        this.durations.add(duration);
    }


    /**
     * Appends the list of durations to the existing durations in the store.
     *
     * @param durations
     *           the durations
     */
    public void addDurations(List<String> durations) {
        if (CollectionUtils.isEmpty(this.durations)) {
            this.durations = new ArrayList<String>();
        }
        this.durations.addAll(durations);
    }

    /**
     * Checks if is duration available.
     *
    * @param duration the duration
     * @return true, if is duration available
     */
   public boolean isDurationAvailable(String duration)
    {
      for (String dur : this.durations)
        {
            if (dur.equals(duration))
            {
                return true;
            }
        }
        return false;
    }

}
