package org.openmrs.module.usagestatistics.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;
//import org.openmrs.module.logmanager.Constants;
import org.openmrs.api.context.UserContext;
import org.openmrs.web.WebConstants;
import org.springframework.web.filter.RequestContextFilter;

/**
 * Filter to catch each new request and store the request object
 */
public class UsageFilter extends RequestContextFilter {

	protected static final Log log = LogFactory.getLog("org.openmrs.api");
	
	/**
	 * Stores the current request attached to the current thread
	 */
	protected final static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	
	/**
	 * Stores the last request time for the given user (user id -> last request time)
	 */
	protected final static Map<Integer, Long> userLastRequests = new HashMap<Integer, Long>();
	
	/**
	 * @see org.springframework.web.filter.RequestContextFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Look in the session for a logged in user
		HttpSession session = request.getSession();
		UserContext userContext = (UserContext)session.getAttribute(WebConstants.OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR);
		if (userContext != null && userContext.getAuthenticatedUser() != null) {
			// Record this request time
			userLastRequests.put(userContext.getAuthenticatedUser().getUserId(), System.currentTimeMillis());
		}
		
		requests.set(request);
		
		super.doFilterInternal(request, response, filterChain);
		
		requests.remove();
	}
	
	/**
	 * Gets the number of active users
	 * @param from the time of last request to be considered active
	 * @return the count
	 */
	public static int getActiveUserCount(Date from) {
		int count = 0;
		for (Integer userId : userLastRequests.keySet()) {
			long lastRequest = userLastRequests.get(userId);
			if (lastRequest >= from.getTime())
				count++;
		}
		return count;
	}
	
	/**
	 * Gets the request object for the calling thread
	 * @return the request object
	 */
	public static HttpServletRequest getCurrentRequest() {
		return requests.get();
	}
}
