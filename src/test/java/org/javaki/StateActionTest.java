package org.javaki;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class StateActionTest {
	private Statechart statechart;
	private State m;
	private State q;
	private static List<String> statesEntered = new ArrayList<String>();
	private static List<String> statesExited = new ArrayList<String>();
	
	@Before
	public void setup() {
		initializeStatechart();
	}
	
	private void initializeStatechart() {
		State rootState = new State(null, "ROOT");
		rootState.setInitialState("A");
		//First level
		State a = rootState.addState(new State("A", new RecordStateEntryAction("A"), new RecordStateExitAction("A")));
		a.setInitialState("F");
		State b = rootState.addState(new State("B", new RecordStateEntryAction("B"), new RecordStateExitAction("B")));
		b.setInitialState("I");
		State c = rootState.addState(new State("C", new RecordStateEntryAction("C"), new RecordStateExitAction("C")));
		c.setInitialState("M");
		State r = rootState.addState(new State("R", new RecordStateEntryAction("R"), new RecordStateExitAction("R")));
		
		//Second level
		State d = a.addState(new State("D", new RecordStateEntryAction("D"), new RecordStateExitAction("D")));
		d.setInitialState("E");
		State f = a.addState(new State("F", new RecordStateEntryAction("F"), new RecordStateExitAction("F")));
		f.setInitialState("G");
		State i = b.addState(new State("I", new RecordStateEntryAction("I"), new RecordStateExitAction("I")));
		i.setSubstatesAreConcurrent(true);
		m = c.addState(new State("M", new RecordStateEntryAction("M"), new RecordStateExitAction("M")));
		m.setInitialState("N");
		
		//Third Level
		State e = d.addState(new State("E", new RecordStateEntryAction("E"), new RecordStateExitAction("E")));
		State g = f.addState(new State("G", new RecordStateEntryAction("G"), new RecordStateExitAction("G")));
		g.setInitialState("H");
		State j = i.addState(new State("J", new RecordStateEntryAction("J"), new RecordStateExitAction("J")));
		j.setInitialState("L");
		State k = i.addState(new State("K", new RecordStateEntryAction("K"), new RecordStateExitAction("K")));
		State n = m.addState(new State("N", new RecordStateEntryAction("N"), new RecordStateExitAction("N")));
		n.setInitialState("O");
		
		//Fourth level
		State h = g.addState(new State("H", new RecordStateEntryAction("H"), new RecordStateExitAction("H")));
		State l = j.addState(new State("L", new RecordStateEntryAction("L"), new RecordStateExitAction("L")));
		State o = n.addState(new State("O", new RecordStateEntryAction("O"), new RecordStateExitAction("O")));
		o.setInitialState("Q");
		State p = n.addState(new State("P", new RecordStateEntryAction("P"), new RecordStateExitAction("P")));
		
		//Fifth level
		q = o.addState(new State("Q", new RecordStateEntryAction("Q"), new RecordStateExitAction("Q")));
		
		statechart = new Statechart("testchart", rootState);
		statechart.initializeStatechart();
	}
	
	@Test
	public void verify_that_enter_state_is_called_when_state_is_entered() {
		//initial state is A -> F -> G -> H
		Assert.assertEquals("Expecting four entered states", new Integer(4), new Integer(statesEntered.size()));
		Assert.assertEquals("Expecting that the 1st element in statesEntered is A", "A", statesEntered.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesEntered is F", "F", statesEntered.get(1));
		Assert.assertEquals("Expecting that the 3rd element in statesEntered is G", "G", statesEntered.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesEntered is H", "H", statesEntered.get(3));
		statesEntered.clear();
		
		statechart.gotoState("Q");
		Assert.assertEquals("Expecting 5 entered states", new Integer(5), new Integer(statesEntered.size()));
		Assert.assertEquals("Expecting that the 1st element in statesEntered is C", "C", statesEntered.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesEntered is M", "M", statesEntered.get(1));
		Assert.assertEquals("Expecting that the 3rd element in statesEntered is N", "N", statesEntered.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesEntered is O", "O", statesEntered.get(3));
		Assert.assertEquals("Expecting that the 5th element in statesEntered is Q", "Q", statesEntered.get(4));
		statesEntered.clear();
		
		statechart.gotoState("B");
		Assert.assertEquals("Expecting 5 entered states", new Integer(5), new Integer(statesEntered.size()));
		Assert.assertEquals("Expecting that the 1st element in statesEntered is B", "B", statesEntered.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesEntered is I", "I", statesEntered.get(1));
		Assert.assertEquals("Expecting that the 3rd element in statesEntered is J", "J", statesEntered.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesEntered is L", "L", statesEntered.get(3));
		Assert.assertEquals("Expecting that the 5th element in statesEntered is K", "K", statesEntered.get(4));
	}
	
	@Test
	public void verify_that_exit_state_is_called_when_state_is_exited() {
		statesEntered.clear();
		statesExited.clear();
		statechart.gotoState("L");
		Assert.assertEquals("Expecting 4 exited states", new Integer(4), new Integer(statesExited.size()));
		Assert.assertEquals("Expecting that the 1st element in statesExited is H", "H", statesExited.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesExited is G", "G", statesExited.get(1));
		Assert.assertEquals("Expecting that the 3rd element in statesExited is F", "F", statesExited.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesExited is A", "A", statesExited.get(3));
		statesEntered.clear();
		statesExited.clear();
		
		statechart.gotoState("Q");
		Assert.assertEquals("Expecting 5 exited states", new Integer(5), new Integer(statesExited.size()));
		Assert.assertEquals("Expecting that the 1st element in statesExited is K", "K", statesExited.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesExited is L", "L", statesExited.get(1));
		Assert.assertEquals("Expecting that the 3rd element in statesExited is J", "J", statesExited.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesExited is I", "I", statesExited.get(3));
		Assert.assertEquals("Expecting that the 5th element in statesExited is B", "B", statesExited.get(4));
		statesEntered.clear();
		statesExited.clear();
		
		statechart.gotoState("E");
		Assert.assertEquals("Expecting 5 exited states", new Integer(5), new Integer(statesExited.size()));
		Assert.assertEquals("Expecting that the 1st element in statesExited is Q", "Q", statesExited.get(0));
		Assert.assertEquals("Expecting that the 2nd element in statesExited is O", "O", statesExited.get(1));
		Assert.assertEquals("Expecting that the 3th element in statesExited is N", "N", statesExited.get(2));
		Assert.assertEquals("Expecting that the 4th element in statesExited is M", "M", statesExited.get(3));
		Assert.assertEquals("Expecting that the 5th element in statesExited is C", "C", statesExited.get(4));
		statesEntered.clear();
		statesExited.clear();
		
	}
	
	@Test
	public void verify_that_actions_are_called_when_event_is_sent_to_active_states() {
		CountCallsAction calls = new CountCallsAction();
		m.addAction("count", calls);
		
		statechart.gotoState("M");
		statechart.sendEvent("count");
		
		Assert.assertEquals("Expecting the 'count' action to be called exaclty once", new Integer(1), new Integer(calls.getNumCalls()));
		
		statechart.sendEvent("count");
		Assert.assertEquals("Expecting the 'count' action to be called exaclty twice", new Integer(2), new Integer(calls.getNumCalls()));
	}
	
	@Test
	public void verify_that_action_is_not_called_when_state_is_inactive() {
		CountCallsAction calls = new CountCallsAction();
		m.addAction("count", calls);
		
		statechart.sendEvent("count");
		
		Assert.assertEquals("Expecting the 'count' action to never have been called", new Integer(0), new Integer(calls.getNumCalls()));
	}
	
	public class RecordStateEntryAction implements StateAction {
		private String stateName;
		
		public RecordStateEntryAction(String stateName) {
			this.stateName = stateName;
		}
		
		public String invokeAction() {
			statesEntered.add(stateName);
			return null;
		}
	}
	
	public class RecordStateExitAction implements StateAction {
		private String stateName;
		
		public RecordStateExitAction(String stateName) {
			this.stateName = stateName;
		}

		public String invokeAction() {
			statesExited.add(stateName);
			return null;
		}
	}
	
	public class CountCallsAction implements StateAction {
		int numCalls = 0;
		
		public String invokeAction() {
			numCalls++;
			return null;
		}
		
		public int getNumCalls() {
			return numCalls;
		}
	}
}
