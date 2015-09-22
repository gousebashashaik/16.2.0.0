package uk.co.portaltech.tui.breadcrumb;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class BreadCrumbFilter extends OncePerRequestFilter
{

   @Resource
   private BrowseHistory browseHistory;

   private List<String> allowedURLMatchingStrings;

   /**
    * @return the allowedURLMatchingStrings
    */
   public List<String> getAllowedURLMatchingStrings()
   {
      return allowedURLMatchingStrings;
   }

   /**
    * @param allowedURLMatchingStrings the allowedURLMatchingStrings to set
    */
   public void setAllowedURLMatchingStrings(final List<String> allowedURLMatchingStrings)
   {
      this.allowedURLMatchingStrings = allowedURLMatchingStrings;
   }

   @Override
   public void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain) throws ServletException,
      IOException
   {

      if ("GET".equals(request.getMethod()) && isValidURLforBrowseHistory(request))
      {
         browseHistory.addBrowseHistoryEntry(new BrowseHistoryEntry(request.getRequestURI(), null));
      }
      filterChain.doFilter(request, response);
   }

   /**
    * @param request
    * @return true or false
    */
   private boolean isValidURLforBrowseHistory(final HttpServletRequest request)
   {
      final String[] reqStrings = StringUtils.split(request.getRequestURI(), "/");
      for (final String reqStr : reqStrings)
      {
         for (final String allowedUrlString : allowedURLMatchingStrings)
         {
            if (reqStr.equals(allowedUrlString))
            {
               return true;
            }
         }
      }
      return false;
   }
}
