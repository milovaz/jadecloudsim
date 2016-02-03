package br.sma.jadecloudsim.util;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;

public class UserServiceNeeds {

	private int brokerId;
	private List<Cloudlet> cloudlets;

	
	public List<Cloudlet> getCloudlets() {
		return cloudlets;
	}

	public void setCloudlets(List<Cloudlet> cloudlets) {
		this.cloudlets = cloudlets;
	}

	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}
	
}
