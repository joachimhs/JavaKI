package org.javaki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
	private String name;
	private Map<String, StateAction> actionMap = new HashMap<String, StateAction>();
	private List<State> substates = new ArrayList<State>();
	private State parentState;
	private boolean substatesAreConcurrent = false;
	private String initialState;
	
	public State(State parentState, String stateName, StateAction enterStateAction, StateAction exitStateAction) {
		this.parentState = parentState;
		this.name = stateName;
		
		setEnterStateAction(enterStateAction);
		setExitStateAction(exitStateAction);
	}
	
	public State(State parentState, String stateName, StateAction enterStateAction, StateAction exitStateAction, List<State> substates, String initialSubstate, boolean substatesAreConcurrent) {
		this.parentState = parentState;
		this.name = stateName;
		this.substates = substates;
		this.initialState = initialSubstate;
		this.substatesAreConcurrent = substatesAreConcurrent;
		
		setEnterStateAction(enterStateAction);
		setExitStateAction(exitStateAction);
	}
	
	public State(String stateName, StateAction enterStateAction, StateAction exitStateAction) {
		this(null, stateName, enterStateAction, exitStateAction);
	}
	
	public State(State parentState, String stateName) {
		this(parentState, stateName, null, null);
	}
	
	public State(String stateName) {
		this(null, stateName, null, null);
	}
	
	public void setParentState(State parentState) {
		this.parentState = parentState;
	}
	
	public void setEnterStateAction(StateAction enterStateAction) {
		if (enterStateAction != null) {
			actionMap.put("enterState", enterStateAction);
		}
	}
	
	public void setExitStateAction(StateAction exitStateAction) {
		if (exitStateAction != null) {
			actionMap.put("exitState", exitStateAction);
		}
	}
	
	public void enterState() {
		sendEvent("enterState");
	}
	
	public void exitState() {
		sendEvent("exitState");
	}
	
	public void setSubstatesAreConcurrent(boolean substatesAreConcurrent) {
		this.substatesAreConcurrent = substatesAreConcurrent;
	}
	
	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}
	
	public String getInitialState() {
		if (initialState == null || initialState.length() == 0) {
			return null;
		}
		boolean hasInitialState = false;
		
		for (State substate : substates) {
			if (initialState.equals(substate.getName())) {
				hasInitialState = true;
				break;
			}
		}
		
		if (!hasInitialState) {
			initialState = null;
		}
		
		return initialState;
	}
	
	public State addState(State state) {
		state.setParentState(this);
		substates.add(state);
		return state;
	}
	
	public boolean hasSubstates() {
		return substates.size() > 0;
	}
 	
	public void addAction(String actionName, StateAction stateAction) {
		this.actionMap.put(actionName, stateAction);
	}
	
	public void sendEvent(String eventName) {
		if (actionMap.containsKey(eventName)) {
			actionMap.get(eventName).invokeAction();
		}
	}
	
	public State getSubstate(String stateName) {
		if (this.getName().equals(stateName)) {
			return this;
		}
		
		for (State substate : substates) {
			if (substate.getName().equals(stateName)) {
				return substate;
			}
			
			if (substate.hasSubstates()) {
				State s = substate.getSubstate(stateName);
				if (s != null) {
					return s;
				}
			}
		}
			
		return null;
	}
	
	public State getParentState(String stateName) {
		if (parentState != null) {
			if (parentState.getName().equals(stateName)) {
				return parentState;
			}
			
			return parentState.getParentState(stateName);
		}
			
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public State getParentState() {
		return parentState;
	}
	
	public List<State> getSubstates() {
		return substates;
	}
}
