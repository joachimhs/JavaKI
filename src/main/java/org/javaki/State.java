package org.javaki;

import java.util.ArrayList;
import java.util.Collections;
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
	private List<State> enteredSubstates = new ArrayList<State>();
	
	public State(String stateName, StateAction enterStateAction, StateAction exitStateAction) {
		this.name = stateName;
		
		setEnterStateAction(enterStateAction);
		setExitStateAction(exitStateAction);
	}
	
	public State(String stateName, StateAction enterStateAction, StateAction exitStateAction, List<State> substates, String initialSubstate, boolean substatesAreConcurrent) {
		this.name = stateName;
		this.substates = substates;
		this.initialState = initialSubstate;
		this.substatesAreConcurrent = substatesAreConcurrent;
		
		setEnterStateAction(enterStateAction);
		setExitStateAction(exitStateAction);
	}
	
	public State(State parentState, String stateName) {
		this(stateName, null, null);
	}
	
	public State(String stateName) {
		this(stateName, null, null);
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
		if (parentState != null) {
			parentState.addEnteredSubstate(this);
		}
	}
	
	public void exitState() {
		sendEvent("exitState");
		if (parentState != null) {
			parentState.removeEnteredSubstate(this);
		}
	}
	
	public void setSubstatesAreConcurrent(boolean substatesAreConcurrent) {
		this.substatesAreConcurrent = substatesAreConcurrent;
	}
	
	public boolean isSubstatesAreConcurrent() {
		return substatesAreConcurrent;
	}
	
	public boolean hasEnteredSubstates() {
		return enteredSubstates.size() > 0;
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
	
	public void addEnteredSubstate(State enteredSubstate) {
		if (substates.contains(enteredSubstate) && !enteredSubstates.contains(enteredSubstate)) {
			enteredSubstates.add(enteredSubstate);
		}
	}
	
	public void removeEnteredSubstate(State stateToExit) {
		if (substates.contains(stateToExit) && enteredSubstates.contains(stateToExit)) {
			enteredSubstates.remove(stateToExit);
		}
	}
	
	public boolean hasSubstates() {
		return substates.size() > 0;
	}
	
	public List<State> getAllEnteredSubstates() {
		List<State> allEnteredSubstates = new ArrayList<State>();
		
		performGetAllEnteredSubstates(allEnteredSubstates, this);
		Collections.reverse(allEnteredSubstates);
		return allEnteredSubstates;
	}
	
	private void performGetAllEnteredSubstates(List<State> allEnteredSubstates, State sourceState) {
		if (sourceState.hasEnteredSubstates()) {
			for (State enteredSubstate : sourceState.getEnteredSubstates()) {
				if (!allEnteredSubstates.contains(enteredSubstate)) {
					allEnteredSubstates.add(enteredSubstate);
				}
				performGetAllEnteredSubstates(allEnteredSubstates, enteredSubstate);
			}
		} else {
			return;
		}
	}
	
	public List<State> getAllInitialSubstates() {
		List<State> allInitialSubstates = new ArrayList<State>();
		
		performGetAllInitialSubstates(allInitialSubstates, this);
		
		return allInitialSubstates;
	}
	
	private void performGetAllInitialSubstates(List<State> allInitialSubstates, State sourceState) {
		State initialState = sourceState.getSubstate(sourceState.getInitialState());
		if (initialState != null) {
			allInitialSubstates.add(initialState);
			performGetAllInitialSubstates(allInitialSubstates, initialState);
		} else if (sourceState.isSubstatesAreConcurrent()) { 
			for (State concurrentSubstate : sourceState.getSubstates()) {
				allInitialSubstates.add(concurrentSubstate);
				performGetAllEnteredSubstates(allInitialSubstates, concurrentSubstate);
			}
		}
	}
	
	public List<State> getEnteredSubstates() {
		return enteredSubstates;
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
	
	@Override
	public String toString() {
		return name;
	}
}
