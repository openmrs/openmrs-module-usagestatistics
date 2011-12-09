/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.usagestatistics.util;

/**
 * Simple class to hold information about paging within a result set
 */
public class PagingInfo {
	protected int pageOffset;
	protected int pageSize;
	protected int resultsTotal;
	
	/**
	 * Constructs a paging information object
	 * @param pageOffset the offset of the result page within the matching rows
	 * @param pageSize the size of the result page
	 */
	public PagingInfo(int pageOffset, int pageSize) {
		this.pageOffset = pageOffset;
		this.pageSize = pageSize;
	}
	
	/**
	 * Constructs a paging information object
	 * @param pageOffset the offset of the result page within the matching rows
	 * @param pageSize the size of the result page
	 * @param resultTotal the total number of matching rows
	 */
	public PagingInfo(int pageOffset, int pageSize, int resultTotal) {
		this.pageOffset = pageOffset;
		this.pageSize = pageSize;
		this.resultsTotal = resultTotal;
	}

	/**
	 * Get the offset of the result page within the matching rows
	 * @return the offset
	 */
	public int getPageOffset() {
		return pageOffset;
	}
	
	/**
	 * @param pageOffset the pageOffset to set
	 */
	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

	/**
	 * Gets the size of the result page
	 * @return the count
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Sets the size of the result page
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Get the total number of matching rows
	 * @return the total number
	 */
	public int getResultsTotal() {
		return resultsTotal;
	}

	/**
	 * Set the total number of matching rows
	 * @param resultTotal the total number of matching rows
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
}
