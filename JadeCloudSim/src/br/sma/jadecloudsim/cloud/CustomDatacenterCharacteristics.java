package br.sma.jadecloudsim.cloud;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;

public class CustomDatacenterCharacteristics extends DatacenterCharacteristics{

	public CustomDatacenterCharacteristics(String architecture, String os,
			String vmm, List<? extends Host> hostList, double timeZone,
			double costPerSec, double costPerMem, double costPerStorage,
			double costPerBw) {
		super(architecture, os, vmm, hostList, timeZone, costPerSec, costPerMem,
				costPerStorage, costPerBw);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected int getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

}
