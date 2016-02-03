package br.sma.jadecloudsim.agent;

import br.sma.jadecloudsim.behaviour.PlataformBehaviour;
import jade.core.Agent;

public class PlataformAgent extends Agent{


	private static final long serialVersionUID = 7899709245326327858L;

	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		addBehaviour(new PlataformBehaviour());
	}
}
