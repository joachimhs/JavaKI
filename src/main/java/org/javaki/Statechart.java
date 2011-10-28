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
		List<State> allEnteredStates = rootState.getAllEnteredSubstates();
		for (State enteredState : allEnteredStates) {
			enteredState.sendEvent(eventName);
		}
	}

	public void initializeStatechart() {
		if (!statechartIsInitialized) {
			statechartIsInitialized = true;
			if (rootState == null) {
				rootState = new State("rootState", null, null);
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
			if (destroyStateChain.contains(enterState)) {
				destroyStateChain.remove(enterState);
			}
		}

		for (State exitState : destroyStateChain) {
			exitState.exitState();
		}

		return currentState;
	}

	private State performGotoState(String stateName) {
		if (currentState.getName().equals(stateName)) {
			// No transitions, return currentState
			return currentState;
		}

		//State substateOfCurrent = currentState.getSubstate(stateName);
		//State parentOfCurrent = currentState.getParentState(stateName);
		State differentBranchState = rootState.getSubstate(stateName);

		if (differentBranchState != null) {
			traverseBetweenTreeBranches(stateName, differentBranchState);
			currentState = differentBranchState;
		}
		
		/*if (substateOfCurrent != null) {
			// stateName is a substate of currentState, no destroy needed
			List<State> reversedOrderStates = getNodesFromSourceToParent(stateName, substateOfCurrent, currentState);
			Collections.reverse(reversedOrderStates);

			recursielyAddInitialOrConcurrentStates(reversedOrderStates);

			currentState = substateOfCurrent;
		} else if (parentOfCurrent != null) {
			// stateName is a parent of currentState, no create needed
			// Add path from current to parentOfCurrent destroyStateChain
			State endingUpState = parentOfCurrent.getSubstate(parentOfCurrent.getInitialState());
			while (endingUpState != null) {
				endingUpState = endingUpState.getSubstate(endingUpState.getInitialState());
			}

			if (endingUpState != currentState) {
				// We wont end up in the same state, so perform state transition

				List<State> statesToDestroy = parentOfCurrent.getAllEnteredSubstates();

				for (State stateToDestroy : statesToDestroy) {
					addToDestroyStateChain(stateToDestroy);
				}

				currentState = parentOfCurrent;
			}
		} else if (differentBranchState != null) {
			traverseBetweenTreeBranches(stateName, differentBranchState);
			currentState = differentBranchState;

			// enter all states from common root state to
			// differentBranchState(targetState)

		} else {
			return null;
		}*/

		while (currentState.isSubstatesAreConcurrent() && currentState.getSubstates().size() > 0) {
			List<State> concurrentStates = currentState.getSubstates();
			// Traverse backwards, so that the left-most substate will be the
			// active state
			for (int i = concurrentStates.size() - 1; i >= 0; i--) {
				State concurrentSubstate = concurrentStates.get(i);
				currentState = concurrentSubstate;
				if (!createStateChain.contains(currentState)) {
					addToCreateStateChain(currentState);
				}
				performGotoState(concurrentSubstate.getName());
			}
		}

		while (currentState.getInitialState() != null) {
			performGotoState(currentState.getInitialState());
		}

		return currentState;
	}

	private void addToDestroyStateChain(State stateToAdd) {
		if (!destroyStateChain.contains(stateToAdd)) {
			destroyStateChain.add(stateToAdd);
		}
	}

	private void addToCreateStateChain(State stateToAdd) {
		if (!createStateChain.contains(stateToAdd)) {
			createStateChain.add(stateToAdd);
		}
	}

	private void traverseBetweenTreeBranches(String stateName, State differentBranchState) {
		// Get path from current to root
		List<State> currentToRootPath = getNodesFromSourceToParent(rootState.getName(), currentState, rootState);
		// Get path from target to root
		List<State> targetToRootPath = getNodesFromSourceToParent(rootState.getName(), differentBranchState, rootState);

		State commonRootState = findCommonRootNode(currentToRootPath, targetToRootPath);
		
		List<State> reversedOrderStates = getNodesFromSourceToParent(stateName, differentBranchState, commonRootState);
		Collections.reverse(reversedOrderStates);
		recursielyAddInitialOrConcurrentStates(reversedOrderStates);
		
		
		State endingUpState = null;
		if (createStateChain.size() > 0) {
			endingUpState = createStateChain.get(createStateChain.size() - 1 );
		}
		
		if (currentState != rootState && currentState != endingUpState) {
			// exit all states from current state to common root state
			for (State stateToDestroy : commonRootState.getAllEnteredSubstates()) {
				addToDestroyStateChain(stateToDestroy);
			}
	
			// If the common root state is the not root state, we want to also exit
			// the last node form the currentToRootPath list
			if (commonRootState == rootState) {
				addToDestroyStateChain(currentToRootPath.get(currentToRootPath.size() - 1));
			}
		}

		
	}

	private void recursielyAddInitialOrConcurrentStates(List<State> statesToAddToCreateChain) {
		for (State stateToCreate : statesToAddToCreateChain) {
			if (!createStateChain.contains(stateToCreate)) {
				addToCreateStateChain(stateToCreate);
				for (State initialOrConcurrentSubstateToCreate : stateToCreate.getAllInitialSubstates()) {
					if (!createStateChain.contains(initialOrConcurrentSubstateToCreate)) {
						addToCreateStateChain(initialOrConcurrentSubstateToCreate);
					}
				}
			}
		}
	}

	private State findCommonRootNode(List<State> currentToRootPath, List<State> targetToRootPath) {

		State commonRootState = null;
		for (State fromBranchState : currentToRootPath) {
			for (State toBranchState : targetToRootPath) {
				if (fromBranchState.getName().equals(toBranchState.getName())) {
					// We found our first common root node
					commonRootState = fromBranchState;
					break;
				}
			}

			// Add fromBranchState to destroyStateChain
			if (commonRootState != null) {
				break;
			}
		}

		if (commonRootState == null) {
			commonRootState = rootState;
		}

		return commonRootState;
	}

	private List<State> getNodesFromSourceToParent(String stateName, State sourceState, State targetState) {
		List<State> statesTraversed = new ArrayList<State>();
		State currState = sourceState;
		while (currState != null) {
			if (currState.getName().equals(targetState.getName())) {
				break;
			}

			statesTraversed.add(currState);

			currState = currState.getParentState();
		}
		return statesTraversed;
	}
}
