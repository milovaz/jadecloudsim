package br.sma.jadecloudsim.agent;


import br.sma.jadecloudsim.behaviour.DataCenterBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


/**
 * Classe que implementa o agente DataCenter extendendo as funcionalidades 
 * da classe Agent do JADE.
 * 
 * 
 * @author thiago
 *
 */
public class DataCenterAgent extends Agent{

	private static final long serialVersionUID = -5435892753390289118L;

	private int dataCenterId;
	
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		Object[] args = getArguments();
		
		if(args != null && args.length > 0){
			dataCenterId = (int) args[0];
		}
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("datacenteragent");
		sd.setName(getLocalName());
		sd.setOwnership(getHap());
		
		if(!register(sd)) doDelete();
		addBehaviour(new DataCenterBehaviour(this, DataCenterBehaviour.mt, dataCenterId));
	}
	
	protected boolean register( ServiceDescription sd) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
//		dfd.addServices(sd);
	    try {
	    	DFAgentDescription list[] = DFService.search( this, dfd );
			if ( list.length>0 ) 
	        	DFService.deregister(this);
	        	
	        dfd.addServices(sd);
			DFService.register(this,dfd);
			return true;
		}
	    catch (FIPAException fe) {
	    	fe.printStackTrace();
	    	return false;
	    }
	}

	public int getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

}
