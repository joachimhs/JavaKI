package org.javaki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statechart {
	private State rootState = null;
	private String name;
	public boolean statechartIsInitialized = false;
	private State initialState = null;
	private State currentState = null;
	
	List<State> createStateChain = new ArrayList<State>();
	List<State> destroyStateChain = new ArrayList<State>();
	
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
			}
			
			initialState = rootState;
			rootState.enterState();
			currentState = initialState;
		}
	}
	
	public State gotoState(String stateName) {
		createStateChain.clear();
		destroyStateChain.clear();
		
		if (currentState.getName().equals(stateName)) {
			//No transitions, return currentState
			return currentState;
		}
		
		State substateOfCurrent = currentState.getSubstate(stateName);
		State parentOfCurrent = currentState.getParentState(stateName);
		State differentBranchState = rootState.getSubstate(stateName);
		
		if (substateOfCurrent != null) {
			//stateName is a substate of currentState, no destroy needed
			
			//Go to the targetState (substateOfCurrent), and traverse back upwards the parent chain until the current node is reached. 
			List<State> reversedOrderStates = getNodesFromSourceToParent(stateName, substateOfCurrent, currentState);
			Collections.reverse(reversedOrderStates);
			createStateChain.addAll(reversedOrderStates);
			currentState = substateOfCurrent;
		} else if (parentOfCurrent != null) {
			//stateName is a parent of currentState, no create needed
			//Add path from current to parentOfCurrent destroyStateChain
			destroyStateChain.addAll(getNodesFromSourceToParent(stateName, currentState, parentOfCurrent));
			currentState = parentOfCurrent;
		} else if (differentBranchState != null) {
			//stateName is part of the tree, but not in the current branch.
			//Get path from current to root
			traverseBetweenTreeBranches(stateName, differentBranchState);
			//find first common root node
			//TODO: Add path to first common root node to destroyStateChain
			//TODO: Add path from first common root node to stateName to createStateChain
		} else {
			return null;
		}
		
		
		return currentState;
	}

	private void traverseBetweenTreeBranches(String stateName,
			State differentBranchState) {
		List<State> currentToRootPath = getNodesFromSourceToParent(rootState.getName(), currentState, rootState);
		//Get path from target to root
		List<State> targetToRootPath = getNodesFromSourceToParent(rootState.getName(), differentBranchState, rootState);
		
		State commonRootState = null;
		for (State fromBranchState : currentToRootPath) {
			for (State toBranchState : targetToRootPath) {
				if (fromBranchState.getName().equals(toBranchState.getName())) {
					//We found our first common root node
					commonRootState = fromBranchState;
					break;
				} 
			}
			
			//Add fromBranchState to destroyStateChain
			if (commonRootState != null) {
				break;
			}
			destroyStateChain.add(fromBranchState);
		}
		
		if (commonRootState == null) {
			commonRootState = rootState;
		}
		List<State> reversedOrderStates = getNodesFromSourceToParent(stateName, differentBranchState, commonRootState);
		Collections.reverse(reversedOrderStates);
		createStateChain.addAll(reversedOrderStates);
		currentState = differentBranchState;
	}

	private List<State> getNodesFromSourceToParent(String stateName, State sourceState, State targetState) {
		List<State> statesTraversed = new ArrayList<State>();
		State currState = sourceState;
		while (currState!= null) {
			if (currState.getName().equals(targetState.getName())) {
				break;
			}
			
			statesTraversed.add(currState);
			
			currState = currState.getParentState();
		}
		return statesTraversed;
	}
}
