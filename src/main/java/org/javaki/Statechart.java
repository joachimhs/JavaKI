package org.javaki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statechart {
	private State rootState = null;
	private String name;
	public boolean statechartIsInitialized = false;
	private State initialState = null;
	State currentState = null;
	
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
			
			while (currentState.getInitialState() != null) {
				gotoState(currentState.getInitialState());
			}
		}
	}
	
	public State gotoState(String stateName) {
		createStateChain.clear();
		destroyStateChain.clear();
		
		currentState = performGotoState(stateName);
		
		for (State enterState : createStateChain) {
			enterState.enterState();
		}
		
		for (State exitState : destroyStateChain) {
			exitState.exitState();
		}
		
		return currentState;
	}

	private State performGotoState(String stateName) {
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
			List<State> statesToDestroy = parentOfCurrent.getAllEnteredSubstates();
			for (State stateToDestroy : statesToDestroy) {
				destroyStateChain.add(stateToDestroy);
			} 
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
		
		while (currentState.isSubstatesAreConcurrent() && currentState.getSubstates().size() > 0) {
			List<State> concurrentStates = currentState.getSubstates();
			//Traverse backwards, so that the left-most substate will be the active state
			for (int i = concurrentStates.size() - 1; i >= 0; i--) {
				State concurrentSubstate = concurrentStates.get(i);
				currentState = concurrentSubstate;
				createStateChain.add(currentState);
				performGotoState(concurrentSubstate.getName());
			}
		}
		
		while (currentState.getInitialState() != null) {
			performGotoState(currentState.getInitialState());
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
		}
		
		State nodeToExitFrom = commonRootState;
		if (commonRootState == null) {
			commonRootState = rootState;
			//IF the common root state is the root state, we want to exit all of the states from the branch we ae leaving
			//bot not the rootstate itself
			nodeToExitFrom = currentToRootPath.get(currentToRootPath.size() - 1);
		}
		
		for (State stateToDestroy : nodeToExitFrom.getAllEnteredSubstates()) {
			destroyStateChain.add(stateToDestroy);
		}
		
		//If the common root state is the root state, we want to also exit the last node form the currentToRootPath list
		if (commonRootState == rootState) {
			destroyStateChain.add(nodeToExitFrom);
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
