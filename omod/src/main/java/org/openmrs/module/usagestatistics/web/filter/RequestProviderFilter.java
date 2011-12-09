package org.openmrs.module.usagestatistics.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;
//import org.openmrs.module.logmanager.Constants;
import org.springframework.web.filter.RequestContextFilter;

/**
 * Filter to catch each new request and store the request object
 */
public class RequestProviderFilter extends RequestContextFilter {

	protected static final Log log = LogFactory.getLog("org.openmrs.api");
	
	protected final static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	
	/**
	 * @see org.springframework.web.filter.RequestContextFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		requests.set(request);
		
		super.doFilterInternal(request, response, filterChain);
		
		requests.remove();
	}
	
	/**
	 * Gets the request object for the calling thread
	 * @return the request object
	 */
	public static HttpServletRequest getRequest() {
		return requests.get();
	}
}
