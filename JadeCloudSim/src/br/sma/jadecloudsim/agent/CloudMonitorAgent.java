package br.sma.jadecloudsim.agent;

import br.sma.jadecloudsim.behaviour.CloudMonitorBehaviour;
import br.sma.jadecloudsim.util.CloudServiceData;
import br.sma.jadecloudsim.util.CloudServiceStandards;
import jade.core.Agent;


/**
 * 
 * Classe que implementa o agente CloudMonitor extendendo as funcionalidades 
 * da classe Agent do JADE.
 * 
 * @author thiago
 *
 */
public class CloudMonitorAgent extends Agent{


	private static final long serialVersionUID = 3894191046567605704L;

	private CloudServiceData cloudServiceData;
	private CloudServiceStandards cloudServiceStandards;
	private String brokerName;
	private int dataCenterId;
	private double baseRatingDataCenter;
	
	
	@Override
	protected void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if(args != null && args.length > 0){
			brokerName = (String) args[0];
			dataCenterId = (int) args[1];
			cloudServiceStandards = (CloudServiceStandards) args[2]; 
			baseRatingDataCenter = (double) args[3];
		}
		
		this.cloudServiceData = new CloudServiceData();
		this.cloudServiceData.setDataCenterId(dataCenterId);
		this.cloudServiceData.setRating(baseRatingDataCenter);
		
		addBehaviour(new CloudMonitorBehaviour(this, 1000));
	}

	public CloudServiceData getCloudServiceData() {
		return cloudServiceData;
	}

	public void setCloudServiceData(CloudServiceData cloudServiceData) {
		this.cloudServiceData = cloudServiceData;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public int getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public CloudServiceStandards getCloudServiceStandards() {
		return cloudServiceStandards;
	}

	public void setCloudServiceStandards(CloudServiceStandards cloudServiceStandards) {
		this.cloudServiceStandards = cloudServiceStandards;
	}

}
