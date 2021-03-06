package org.javaki;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class StateTraversalTest {
	private Statechart statechart;
	private State m;
	private State q;
	
	@Before
	public void setup() {
		initializeStatechart();
	}
	
	private void initializeStatechart() {
		State rootState = new State(null, "ROOT");
		rootState.setInitialState("A");
		//First level
		State a = rootState.addState(new State("A"));
		a.setInitialState("F");
		State b = rootState.addState(new State("B"));
		b.setInitialState("I");
		State c = rootState.addState(new State("C"));
		c.setInitialState("M");
		State r = rootState.addState(new State("R"));
		
		//Second level
		State d = a.addState(new State("D"));
		d.setInitialState("E");
		State f = a.addState(new State("F"));
		f.setInitialState("G");
		State i = b.addState(new State("I"));
		i.setSubstatesAreConcurrent(true);
		m = c.addState(new State("M"));
		m.setInitialState("N");
		
		//Third Level
		State e = d.addState(new State("E"));
		State g = f.addState(new State("G"));
		g.setInitialState("H");
		State j = i.addState(new State("J"));
		j.setInitialState("L");
		State k = i.addState(new State("K"));
		State n = m.addState(new State("N"));
		n.setInitialState("O");
		
		//Fourth level
		State h = g.addState(new State("H"));
		State l = j.addState(new State("L"));
		State o = n.addState(new State("O"));
		o.setInitialState("Q");
		State p = n.addState(new State("P"));
		
		//Fifth level
		q = o.addState(new State("Q"));
		
		statechart = new Statechart("testchart", rootState);
		statechart.initializeStatechart();
	}
	
	@Test
	public void verify_that_statechart_can_traverse_to_state() {
		State actual = statechart.gotoState("G");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state H because it is the intitial substate of G", "H", actual.getName());
		
		actual = statechart.gotoState("H");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state H", "H", actual.getName());
		
		actual = statechart.gotoState("B");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state L following the initial substates of the nodes", "L", actual.getName());
		
		actual = statechart.gotoState("N");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state Q, following the N and O initial substates", "Q", actual.getName());
		
		actual = statechart.gotoState("X");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state Q", "Q", actual.getName());
		
		actual = m.getSubstate("Q");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state Q", "Q", actual.getName());
	}
	
	@Test
	public void verify_that_state_can_identify_parent_state() {
		State actual = q.getParentState("C");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Expecting state C", actual.getName(), "C");
		
		actual = q.getParentState("O");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Expecting state O", actual.getName(), "O");
		
		actual = q.getParentState("P");
		Assert.assertNull("P is not a parent of Q", actual);
		
		actual = q.getParentState("N");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Expecting state N", actual.getName(), "N");
		
		actual = q.getParentState("M");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Expecting state M", actual.getName(), "M");
		
		actual = q.getParentState("ROOT");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Expecting state ROOT", actual.getName(), "ROOT");

		actual = q.getParentState("B");
		Assert.assertNull("B is not a parent of Q", actual);
		
		actual = q.getParentState("X");
		Assert.assertNull("X is not a parent of Q", actual);
	}
	
	@Test
	public void verify_that_the_right_create_states_is_created_when_moving_to_substate() {
		statechart.gotoState("G");
		//TODO: DETERMINE HOW TO HANDLE THIS, AS G has the currentState as its initialState!
		//Assert.assertTrue("Expecting an empty createStateChain as we are already at state H per initializeStatechart", statechart.createStateChain.size() == 0);
		
		statechart.gotoState("H");
		Assert.assertTrue("Expecting an empty createStateChain", statechart.createStateChain.isEmpty());
		
		initializeStatechart();
		statechart.gotoState("C");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(5), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is C", "C", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is M", "M", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is N", "N", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is O", "O", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is Q", "Q", statechart.createStateChain.get(4).getName());
		
		statechart.gotoState("M");
		Assert.assertTrue("Expecting an empty destroyStateChain", statechart.destroyStateChain.isEmpty());
		
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 3 elements in createStateChain", new Integer(3), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is N", "N", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is O", "O", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3st element in createStateChain is Q", "Q", statechart.createStateChain.get(2).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Expecting an empty destroyStateChain", statechart.destroyStateChain.isEmpty());
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is Q", "Q", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("Q");
		Assert.assertTrue("Expecting a empty createStateChain", statechart.createStateChain.isEmpty());
	}
	
	@Test
	public void verify_that_the_right_destroy_states_is_creates_when_moving_to_parent_state() {
		statechart.gotoState("Q");
		
		statechart.gotoState("M");
		Assert.assertTrue("Expecting an empty destroyStateChain as we are not really away from Q", statechart.destroyStateChain.isEmpty());
		
		initializeStatechart();
		statechart.gotoState("Q");
		Assert.assertTrue("Expecting a non-empty destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 4 elements in destroyStateChain",  new Integer(4), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is H", "H", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is G", "G", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is F", "F", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in destroyStateChain is A", "A", statechart.destroyStateChain.get(3).getName());
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is C", "C", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is M", "M", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is N", "N", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is O", "O", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is Q", "Q", statechart.createStateChain.get(4).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Expecting an empty destroyStateChain as we are not really away from Q", statechart.destroyStateChain.isEmpty());
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is Q", "Q", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the current State is Q", "Q", statechart.currentState.getName());
		
		statechart.gotoState("P");
		Assert.assertTrue("Expecting a non-empty destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is P", "P", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("I");
		Assert.assertTrue("Expecting a non-empty destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 4 elements in destroyStateChain",  new Integer(4), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is P", "P", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is N", "N", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is M", "M", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in destroyStateChain is C", "C", statechart.destroyStateChain.get(3).getName());
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is B", "B", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is I", "I", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is J", "J", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is L", "L", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is K", "K", statechart.createStateChain.get(4).getName());
	}
	
	@Test
	public void verify_that_the_right_destroy_and_create_states_are_created_when_switching_branches_in_the_tree() {
		statechart.gotoState("Q");
		
		statechart.gotoState("P");
		Assert.assertTrue("Expecting a non-emptydestroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 2 elements in destroyStateChain",  new Integer(2), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is Q", "Q", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is O", "O", statechart.destroyStateChain.get(1).getName());
		
		Assert.assertTrue("Expecting a non-emptycreateStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain",  new Integer(1), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is P", "P", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("K");
		Assert.assertEquals("Expecting 4 elements in destroyStateChain",  new Integer(4), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is P", "P", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is N", "N", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is M", "M", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in destroyStateChain is C", "C", statechart.destroyStateChain.get(3).getName());
		
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is B", "B", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is I", "I", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is J", "J", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is L", "L", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is K", "K", statechart.createStateChain.get(4).getName());
		
		statechart.gotoState("L");
		//Assert.assertTrue("Expecting an empty destroyStateChain", statechart.destroyStateChain.isEmpty());
		//Assert.assertTrue("Expecting an empty createStateChain", statechart.createStateChain.isEmpty());
	}
	
	@Test
	public void verify_that_initialize_statechart_adheres_to_initial_state_declarations() {
		Assert.assertEquals("Expecting that the initial current stat is H", "H", statechart.currentState.getName());
	}
	
	@Test
	public void verify_that_all_concurrent_substates_are_entered() {
		statechart.gotoState("I");
		Assert.assertTrue("Expecting a non-emptycreateStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is B", "B", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is I", "I", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is J", "J", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is L", "L", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is K", "K", statechart.createStateChain.get(4).getName());
		
		Assert.assertEquals("Expecting that the current state is L", "L", statechart.currentState.getName());
	}
	
	@Test
	public void verify_that_all_concurrent_substates_are_exited_when_parent_is_exited() {
		verify_that_all_concurrent_substates_are_entered();
		
		statechart.gotoState("R");
		Assert.assertTrue("Expecting a non-emptydestroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in destroyStateChain",  new Integer(5), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is K", "K", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is L", "L", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is J", "J", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in destroyStateChain is I", "I", statechart.destroyStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in destroyStateChain is B", "B", statechart.destroyStateChain.get(4).getName());
	}
	
}
