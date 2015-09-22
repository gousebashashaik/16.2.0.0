package uk.co.portaltech.tui.breadcrumb.impl;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.tui.breadcrumb.BrowseHistory;
import uk.co.portaltech.tui.breadcrumb.BrowseHistoryEntry;

/**
 */
public class DefaultBrowseHistory implements BrowseHistory
{
   private static final String SESSION_USER_BROWSE_HISTORY_KEY = "sessionUserBrowseHistory";

   private static final int CAPACITYVALUE = 10;

   @Resource
   private SessionService sessionService;

   private int capacity = CAPACITYVALUE;

   protected int getCapacity()
   {
      return capacity;
   }

   @Required
   public void setCapacity(final int capacity)
   {
      this.capacity = capacity;
   }

   @Override
   public void addBrowseHistoryEntry(final BrowseHistoryEntry browseHistoryEntry)
   {
      final List<BrowseHistoryEntry> browseHistoryEntries = getBrowseHistoryEntries();
      browseHistoryEntries.add(0, browseHistoryEntry);
      trimHistory(browseHistoryEntries);
      saveHistory(browseHistoryEntries);
   }

   protected List<BrowseHistoryEntry> getBrowseHistoryEntries()
   {
      final List<BrowseHistoryEntry> browseHistoryEntries = new LinkedList<BrowseHistoryEntry>();
      final Object history = sessionService.getAttribute(SESSION_USER_BROWSE_HISTORY_KEY);
      if (history instanceof List)
      {
         browseHistoryEntries.addAll((List) history);
      }
      return browseHistoryEntries;
   }

   protected void trimHistory(final List<BrowseHistoryEntry> browseHistoryEntries)
   {
      while (browseHistoryEntries.size() > getCapacity())
      {
         ((LinkedList) browseHistoryEntries).removeLast();
      }
   }

   protected void saveHistory(final List<BrowseHistoryEntry> browseHistoryEntries)
   {
      sessionService.setAttribute(SESSION_USER_BROWSE_HISTORY_KEY, browseHistoryEntries);
   }

   @Override
   public BrowseHistoryEntry findUrlInHistory(final String match)
   {
      for (final BrowseHistoryEntry entry : getBrowseHistoryEntries())
      {
         final String[] parts = entry.getUrl().split("/");
         for (final String part : parts)
         {
            if (part.equals(match))
            {
               return entry;
            }
         }
      }
      return null;
   }
}