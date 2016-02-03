package br.sma.jadecloudsim.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;

import br.sma.jadecloudsim.main.CloudSimEnv;


public class CloudElements {
	
	private static Map<String, Datacenter> datacenterBrokerSelection = new HashMap<String, Datacenter>();

	private static CustomDataCenterBroker dataCenterBroker;
	
	private static List<Datacenter> dataCenterList = new ArrayList<>(); 
	
	private static Map<Integer, Datacenter> mapVmDatacenter;
	
	private static Map<Integer, Vm> createdVms = new HashMap<>();
	
	private static List<Cloudlet> cloudlets;
	
	private static boolean falhaIndisponilidadeTeste;
	
	private static boolean cloudSimOn = false;
	
	public static void addLigacaoVmDatacenter(Integer vmId, Integer datacenterId) {
		if(mapVmDatacenter == null){
			mapVmDatacenter = new HashMap<Integer, Datacenter>();
		}
		
		
		mapVmDatacenter.put(vmId, (Datacenter) CloudSim.getEntity(datacenterId));
	}
	
	public static void removeLigacaoVmDatacenter(Integer vmId) {
		if(mapVmDatacenter.containsKey(vmId)){
			mapVmDatacenter.remove(vmId);
		}
	}
	
	public static void addCreatedVm(Vm vm) {
		if(createdVms == null){
			createdVms = new HashMap<>();
		}
		
		createdVms.put(vm.getId(), vm);
	}
	
	public static Map<Integer, Vm> getCreatedVms() {
		return createdVms;
	}

	public static void setCreatedVms(Map<Integer, Vm> createdVms) {
		CloudElements.createdVms = createdVms;
	}

	public static Map<Integer, Datacenter> getMapVmDatacenter() {
		return mapVmDatacenter;
	}

	public static void setMapVmDatacenter(Map<Integer, Datacenter> mapVmDatacenter) {
		CloudElements.mapVmDatacenter = mapVmDatacenter;
	}

	public static Map<String, Datacenter> getDatacenterBrokerSelection() {
		return datacenterBrokerSelection;
	}

	public static void setDatacenterBrokerSelection(
			Map<String, Datacenter> datacenterBrokerSelection) {
		CloudElements.datacenterBrokerSelection = datacenterBrokerSelection;
	}

	public static CustomDataCenterBroker getDataCenterBroker() {
		return dataCenterBroker;
	}

	public static void setDataCenterBroker(CustomDataCenterBroker dataCenterBroker) {
		CloudElements.dataCenterBroker = dataCenterBroker;
	}

	public static List<Datacenter> getDataCenterList() {
		return dataCenterList;
	}

	public static void setDataCenterList(List<Datacenter> dataCenterList) {
		CloudElements.dataCenterList = dataCenterList;
	}

	public static List<Cloudlet> getCloudlets() {
		return cloudlets;
	}

	public static void setCloudlets(List<Cloudlet> cloudlets) {
		CloudElements.cloudlets = cloudlets;
	}

	public static boolean isFalhaIndisponilidadeTeste() {
		return falhaIndisponilidadeTeste;
	}

	public static void setFalhaIndisponilidadeTeste(
			boolean falhaIndisponilidadeTeste) {
		CloudElements.falhaIndisponilidadeTeste = falhaIndisponilidadeTeste;
	}

	public static boolean isCloudSimOn() {
		return cloudSimOn;
	}

	public static void setCloudSimOn(boolean cloudSimOn) {
		CloudElements.cloudSimOn = cloudSimOn;
	}

}
