package org.javaki;

import java.util.ArrayList;
import java.util.List;

public class Statechart {
	private State rootState = null;
	private String name;
	public boolean statechartIsInitialized = false;
	private State initialState = null;
	private State currentState = null;
	
	private List<State> createStateChain = new ArrayList<State>();
	private List<State> destroyStateChain = new ArrayList<State>();
	
	public Statechart(String name, State rootState) {
		this.name = name;
		this.rootState = rootState;
	}
	
	public Statechart(String name) {
		this.name = name;
	}
	
	public void sendEvent(String eventName) {
		
	}
	
	public void initializeStatechart() {
		if (!statechartIsInitialized) {
			statechartIsInitialized = true;
			if (rootState == null) {
				rootState = new State(null, "rootState", null, null);
				initialState = rootState;
			}
			
			rootState.enterState();
			
			currentState = initialState;
		}
	}
	
	public State gotoState(String stateName) {
		createStateChain.clear();
		destroyStateChain.clear();
		
		return rootState.getSubstate(stateName);
	}
}
