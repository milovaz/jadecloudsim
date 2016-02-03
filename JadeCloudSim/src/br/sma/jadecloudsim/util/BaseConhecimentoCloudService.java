package br.sma.jadecloudsim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseConhecimentoCloudService {

	
	private Map<Integer, Double> dataCenterRatings;
	
	/**  */
	private CloudServiceStandards cloudServiceStandards;
	
	/** Lista de datacenters que já tiveram violações significativos de QoS */
	private List<Integer> dataCenterBlackList;
	

	public BaseConhecimentoCloudService() {
		this.dataCenterRatings = new HashMap<Integer, Double>();
		this.dataCenterBlackList = new ArrayList<>();
	}
	
	public void addDataCenterRatings(Integer key, Double value) {
		if(this.dataCenterRatings.containsKey(key)){
			this.dataCenterRatings.put(key, this.dataCenterRatings.get(key) + value);
		}else{
			this.dataCenterRatings.put(key, value);
		}
	}

	public Map<Integer, Double> getDataCenterRatings() {
		return dataCenterRatings;
	}

	public void setDataCenterRatings(Map<Integer, Double> dataCenterRatings) {
		this.dataCenterRatings = dataCenterRatings;
	}

	public CloudServiceStandards getCloudServiceStandards() {
		return cloudServiceStandards;
	}

	public void setCloudServiceStandards(CloudServiceStandards cloudServiceStandards) {
		this.cloudServiceStandards = cloudServiceStandards;
	}

	public List<Integer> getDataCenterBlackList() {
		return dataCenterBlackList;
	}

	public void setDataCenterBlackList(List<Integer> dataCenterBlackList) {
		this.dataCenterBlackList = dataCenterBlackList;
	}

}
