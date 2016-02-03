package br.sma.jadecloudsim.util;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class CloudServiceStandards implements Serializable{


	private static final long serialVersionUID = 6014532432149444219L;

	public static final int DEFAULT_ACCEPT_INDIS_EVENTS = 5;
	public static final double DEFAULT_ALLOWED_HOURS_OF_OUTAGE = 2.0; 
	public static final double DEFAULT_MAX_RATING_FOR_BLACKLIST = 10;
	
	/** Quantidade de horas permitidas de indisponibilidade */
	private double allowedHoursOfOutagePerMonth;
	
	/** Quantidade aceitável de eventos de indisponibilidade */
	private int acceptableIndisEvents;
	
	/** Quantidade aceitável de eventos de performance */
	private int acceptablePerformanceEvents;

	
	public CloudServiceStandards() {
		this.acceptableIndisEvents = CloudServiceStandards.DEFAULT_ACCEPT_INDIS_EVENTS;
		this.allowedHoursOfOutagePerMonth = CloudServiceStandards.DEFAULT_ALLOWED_HOURS_OF_OUTAGE;
	}
	
	public int getAcceptableIndisEvents() {
		return acceptableIndisEvents;
	}

	public void setAcceptableIndisEvents(int acceptableIndisEvents) {
		this.acceptableIndisEvents = acceptableIndisEvents;
	}

	public int getAcceptablePerformanceEvents() {
		return acceptablePerformanceEvents;
	}

	public void setAcceptablePerformanceEvents(int acceptablePerformanceEvents) {
		this.acceptablePerformanceEvents = acceptablePerformanceEvents;
	}

	public double getAllowedHoursOfOutagePerMonth() {
		return allowedHoursOfOutagePerMonth;
	}

	public void setAllowedHoursOfOutagePerMonth(double allowedHoursOfOutagePerMonth) {
		this.allowedHoursOfOutagePerMonth = allowedHoursOfOutagePerMonth;
	}


}
