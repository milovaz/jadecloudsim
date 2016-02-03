package br.sma.jadecloudsim.util;

import java.io.Serializable;

public class BrokerUserNeeds implements Serializable{


	private static final long serialVersionUID = 486715726709805379L;
	
	
	private int brokerId;
	private int mips;
	private long size; //image size (MB)
	private int ram; //vm memory (MB)
	private long bw;
	private int pesNumber; //number of cpus
	
	
	public int getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}
	public int getMips() {
		return mips;
	}
	public void setMips(int mips) {
		this.mips = mips;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public long getBw() {
		return bw;
	}
	public void setBw(long bw) {
		this.bw = bw;
	}
	public int getPesNumber() {
		return pesNumber;
	}
	public void setPesNumber(int pesNumber) {
		this.pesNumber = pesNumber;
	}
	
}
