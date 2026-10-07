/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.runnable;


/**
 * @author poltergeist0
 *
 * Class that... {@link Runnable} 
 * 
 */
public class RunnableStepByStepStatistics{
	
	/****************************
	 * status variables
	 ***************************/
	
	/**
	 * variable that holds the total number of items being processed
	 */
	private final Long ti;//totalItems

	/**
	 * variable that holds the total size being processed
	 */
	private final Long ts;//totalSize
	
	/**
	 * variable that holds the total number of items already processed
	 */
	private final Long pi;//processedItems

	/**
	 * variable that holds the amount already processed
	 */
	private final Long ps;//processedSize

	/**
	 * variable that holds the current action being executed
	 */
	private final String ac;//action
		
	/**
	 * variable that holds the result of calculating the processed percentage.
	 * It is used only to avoid excessive calculations.
	 */
	private final Double pip;//processedItemsPercentage

	/**
	 * variable that holds the result of calculating the processed percentage.
	 * It is used only to avoid excessive calculations.
	 */
	private final Double psp;//processedSizePercentage
	
	/****************************
	 * status methods
	 ***************************/
	
	/**
	 * method to be used to get the totalSize from the step method
	 * @return the totalSize
	 */
	public Long getTotalItems(){return ti;}
	
	/**
	 * Public method to get the processedSize
	 */
	public Long getProcessedItems(){return pi;}
	
	/**
	 * @return the processed percentage
	 */
	public Double getProcessedItemsPercentage(){return pip;}
	
	/**
	 * method to be used to get the totalSize from the step method
	 * @return the totalSize
	 */
	public Long getTotalSize(){return ts;}
	
	/**
	 * Public method to get the processedSize
	 */
	public Long getProcessedSize(){return ps;}
	
	/**
	 * @return the processed percentage
	 */
	public Double getProcessedSizePercentage(){return psp;}
	
	/**
	 * Public method to get the current action
	 */
	public String getAction(){return ac;}
	
	/**
	 * Public method to get the totalSize, processedSize and current action in a single string
	 */
	public String getStatus(){
		return pi+" / "+ti+" ( "+pip+" %) @ "+ps+" / "+ts+" ( "+psp+" %) -> "+ac;
	}
	
	public String toString(){
		return super.toString()+":"+getStatus();
	}
	
	private RunnableStepByStepStatistics evaluate(RunnableStepByStepStatistics original) {
		return (original==null)?new RunnableStepByStepStatistics():original;
	}
	
	private static Long evaluateValue(Long totalItems) {
		return ((totalItems<=0)?0:totalItems);
	}
	
	private static String evaluateAction(String action) {
		if(action==null) return "";
		else return action;
	}
	
	private static Double calculatePercentage(Long processed,Long total) {
		return (total<=0)?0.0:processed*100.0/(1.0*total);
	}
	
	/**
	 * Instantiate the object with the default values of zero
	 */
	public RunnableStepByStepStatistics(){
		ti=(long) 0;
		pi=(long) 0;
		pip=0.0;
		ts=(long) 0;
		ps=(long) 0;
		psp=0.0;
		ac="";
	}
	
	public RunnableStepByStepStatistics(RunnableStepByStepStatistics original){
		ti=evaluate(original).ti;
		pi=evaluate(original).pi;
		pip=evaluate(original).pip;
		ts=evaluate(original).ts;
		ps=evaluate(original).ps;
		psp=evaluate(original).psp;
		ac=evaluate(original).ac;
	}
	
	/**
	 * Instantiate with some values
	 * A negative value initializes with zero.
	 * A null action keeps the previous value.
	 * 
	 * @param totalItems
	 * @param totalSize
	 * @param action
	 */
	public RunnableStepByStepStatistics(
			long totalItems,
			long totalSize,
			final String action
			){
		this(evaluateValue(totalItems),evaluateValue(totalSize),(long) 0,(long) 0,evaluateAction(action));
	}
	
	/**
	 * Instantiate with some initial values.
	 * 
	 * @param totalItems
	 * @param totalSize
	 * @param action
	 */
	private RunnableStepByStepStatistics(
			long totalItems,
			long totalSize,
			long processedItems,
			long processedSize,
			String action
			){
		ti=totalItems;
		pi=processedItems;
		ts=totalSize;
		ps=processedSize;
		psp=calculatePercentage(ps,ts);
		pip=calculatePercentage(pi,ti);
		ac=action;
	}
	
	public static enum FIELD {NONE,TOTALSIZE,TOTALITEMS,PROCESSEDSIZE, PROCESSEDITEMS}
	
	/**
	 * Modify value and update action.
	 * A negative value keeps the previous value.
	 * A null action keeps the previous value.
	 * 
	 * @param original
	 * @param value
	 * @param field
	 * @param setOrIncrement if true sets the value. If false increments the value
	 * @param action
	 */
	public RunnableStepByStepStatistics(
			final RunnableStepByStepStatistics original,
			final long value,
			final FIELD field,
			final boolean setOrIncrement,
			final String action
			){
		if(field==FIELD.TOTALITEMS) {
			if(setOrIncrement)ti=evaluateValue(value);
			else ti=evaluate(original).ti+evaluateValue(value);
		}
		else ti=evaluate(original).ti;
		if(field==FIELD.TOTALSIZE){
			if(setOrIncrement)ts=evaluateValue(value);
			else ts=evaluate(original).ts+evaluateValue(value);
		}
		else ts=evaluate(original).ts;
		if(field==FIELD.PROCESSEDSIZE){
			if(setOrIncrement)ps=evaluateValue(value);
			else ps=evaluate(original).ps+evaluateValue(value);
		}
		else ps=evaluate(original).ps;
		if(field==FIELD.PROCESSEDITEMS){
			if(setOrIncrement)pi=evaluateValue(value);
			else pi=evaluate(original).pi+evaluateValue(value);
		}
		else pi=evaluate(original).pi;
		psp=calculatePercentage(ps,ts);
		pip=calculatePercentage(pi,ti);
		if(action==null) ac=evaluate(original).ac;
		else ac=action;
	}

	/**
	 * Modify value and update action.
	 * Adds differences to original
	 * 
	 * @param original
	 * @param value
	 * @param field
	 * @param setOrIncrement if true sets the value. If false increments the value
	 * @param action
	 */
	public RunnableStepByStepStatistics(
			final RunnableStepByStepStatistics original,
			final RunnableStepByStepStatistics differences
			){
		ti=evaluate(original).ti+evaluate(differences).ti;
		ts=evaluate(original).ts+evaluate(differences).ts;
		ps=evaluate(original).ps+evaluate(differences).ps;
		pi=evaluate(original).pi+evaluate(differences).pi;
		psp=calculatePercentage(ps,ts);
		pip=calculatePercentage(pi,ti);
		if(evaluate(differences).ac==null) ac=evaluate(original).ac;
		else ac=evaluate(differences).ac;
	}

	/**
	 * @param value
	 * @param field
	 * @param setOrIncrement
	 * @param action
	 * @return a new RunnableStepByStepStatistics with the requested modifications
	 */
	public RunnableStepByStepStatistics modify(
			final long value,
			final FIELD field,
			final boolean setOrIncrement,
			final String action
			){
		if(field==FIELD.NONE) {
			if(action==null || ac.equals(action)) {
				//there are no changes
				return new RunnableStepByStepStatistics(this);
			}
		}
		else if(setOrIncrement) {//set
			if(value<0){//no value to set
				if(action==null || ac.equals(action)) {
					//there are no changes
					return new RunnableStepByStepStatistics(this);
				}
			}
		}
		else {//increment
			if(value<=0){//there is no increment
				if(action==null || ac.equals(action)) {
					//there are no changes
					return new RunnableStepByStepStatistics(this);
				}
			}
		}
		return new RunnableStepByStepStatistics(this,value,field,setOrIncrement,action);
	}
	
	/**
	 * @param value
	 * @param field
	 * @param setOrIncrement
	 * @param action
	 * @return a new RunnableStepByStepStatistics with the requested modifications
	 */
	public RunnableStepByStepStatistics differences(RunnableStepByStepStatistics previous){
		String a=null;
		if(previous.ac==null) {
			if(ac!=null) a=ac;
		}
		else {
			if(ac==null) a=previous.ac;
			else {
				if(!ac.equals(previous.ac)) a=ac;
			}
		}
		return new RunnableStepByStepStatistics(ti-previous.ti,ts-previous.ts,pi-previous.pi,ps-previous.ps,a);
	}

	public RunnableStepByStepStatistics add(
			final RunnableStepByStepStatistics differences
			){
		if(differences.ti==0 && differences.ts==0 && differences.pi==0 && differences.ps==0 && (differences.ac==null || ac.equals(differences.ac))) return new RunnableStepByStepStatistics(this);
		return new RunnableStepByStepStatistics(this,differences);
	}

	public RunnableStepByStepStatistics copy(){
		return new RunnableStepByStepStatistics(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ti == null) ? 0 : ti.hashCode());
		result = prime * result + ((ts == null) ? 0 : ts.hashCode());
		result = prime * result + ((pi == null) ? 0 : pi.hashCode());
		result = prime * result + ((ps == null) ? 0 : ps.hashCode());
		result = prime * result + ((ac == null) ? 0 : ac.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RunnableStepByStepStatistics other = (RunnableStepByStepStatistics) obj;
		if (ti == null) {
			if (other.ti != null)
				return false;
		} else if (!ti.equals(other.ti))
			return false;
		if (ts == null) {
			if (other.ts != null)
				return false;
		} else if (!ts.equals(other.ts))
			return false;
		if (pi == null) {
			if (other.pi != null)
				return false;
		} else if (!pi.equals(other.pi))
			return false;
		if (ps == null) {
			if (other.ps != null)
				return false;
		} else if (!ps.equals(other.ps))
			return false;
		if (ac == null) {
			if (other.ac != null)
				return false;
		} else if (!ac.equals(other.ac))
			return false;
		return true;
	}
	
}
