package br.sma.jadecloudsim.util;

import java.io.Serializable;

public class DataCenterProposal implements Serializable{

	
	private static final long serialVersionUID = -3404046053776071303L;

	public static final int MEMORIA = 0;
	public static final int STORAGE = 1;
	public static final int BW = 2;
	public static final int MI = 3;
	
	/** The datacenter id */
	private int dataCenterId;
	
	/** The cost per mem. */
    private double costPerMem;

    /** The cost per storage. */
    private double costPerStorage;

    /** The cost per bw. */
    private double costPerBw;
    
    /** Price/CPU-unit if unit = sec., then G$/CPU-sec. */
    private double costPerMi;

    private double [] costs = new double[4];
    
    
    public int getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public double getTotalCost() {
    	return costPerBw + costPerMem + costPerMi + costPerStorage;
    }
    
	public double getCostPerMem() {
		return costPerMem;
	}

	public void setCostPerMem(double costPerMem) {
		this.costPerMem = costPerMem;
		this.costs[MEMORIA] = costPerMem;
	}

	public double getCostPerStorage() {
		return costPerStorage;
	}

	public void setCostPerStorage(double costPerStorage) {
		this.costPerStorage = costPerStorage;
		this.costs[STORAGE] = costPerStorage;
	}

	public double getCostPerBw() {
		return costPerBw;
	}

	public void setCostPerBw(double costPerBw) {
		this.costPerBw = costPerBw;
		this.costs[BW] = costPerBw;
	}

	public double getCostPerMi() {
		return costPerMi;
	}

	public void setCostPerMi(double costPerMi) {
		this.costPerMi = costPerMi;
		this.costs[MI] = costPerMi;
	}

	public double[] getCosts() {
		return costs;
	}

	public void setCosts(double[] costs) {
		this.costs = costs;
	}

}
