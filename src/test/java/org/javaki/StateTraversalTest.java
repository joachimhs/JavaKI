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
		//First level
		State a = rootState.addState(new State("A"));
		State b = rootState.addState(new State("B"));
		State c = rootState.addState(new State("C"));
		
		//Second level
		State d = a.addState(new State("D"));
		State f = a.addState(new State("F"));
		State i = b.addState(new State("I"));
		m = c.addState(new State(c, "M"));
		
		//Third Level
		State e = d.addState(new State("E"));
		State g = f.addState(new State("G"));
		State j = i.addState(new State("J"));
		State k = i.addState(new State("K"));
		State n = m.addState(new State("N"));
		
		//Fourth level
		State h = g.addState(new State("H"));
		State l = j.addState(new State("L"));
		State o = n.addState(new State("O"));
		State p = n.addState(new State("P"));
		
		//Fifth level
		q = o.addState(new State("Q"));
		
		statechart = new Statechart("testchart", rootState);
		statechart.initializeStatechart();
	}
	
	@Test
	public void verify_that_statechart_can_traverse_to_state() {
		statechart.initializeStatechart();
		State actual = statechart.gotoState("G");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state G", actual.getName(), "G");
		
		actual = statechart.gotoState("H");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state H", actual.getName(), "H");
		
		actual = statechart.gotoState("B");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state B", actual.getName(), "B");
		
		actual = statechart.gotoState("N");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state N", actual.getName(), "N");
		
		actual = statechart.gotoState("X");
		Assert.assertNull("Expecting ikke en state", actual);
		
		actual = m.getSubstate("Q");
		Assert.assertNotNull("Expecting a state", actual);
		Assert.assertEquals("Expecting state Q", actual.getName(), "Q");
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
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 3 elements in createStateChain",  new Integer(3), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is A", "A", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is F", "F", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is G", "G", statechart.createStateChain.get(2).getName());
		
		statechart.gotoState("H");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is H", "H", statechart.createStateChain.get(0).getName());
		
		initializeStatechart();
		statechart.gotoState("C");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is C", "C", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("M");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is M", "M", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("N");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is N", "N", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is O", "O", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("Q");
		Assert.assertTrue("Expecting a non-empty createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Expecting that the 1st element in createStateChain is Q", "Q", statechart.createStateChain.get(0).getName());
	}
	
	@Test
	public void verify_that_the_right_destroy_states_is_creates_when_moving_to_parent_state() {
		statechart.gotoState("Q");
		
		statechart.gotoState("M");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 3 elements in destroyStateChain",  new Integer(3), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is Q", "Q", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is O", "O", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is N", "N", statechart.destroyStateChain.get(2).getName());
		
		initializeStatechart();
		statechart.gotoState("Q");
		Assert.assertTrue("Expecting en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 5 elements in createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is C", "C", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is M", "M", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is N", "N", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4th element in createStateChain is O", "O", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Expecting that the 5th element in createStateChain is Q", "Q", statechart.createStateChain.get(4).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is Q", "Q", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("N");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is O", "O", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("M");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is N", "N", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("C");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is M", "M", statechart.destroyStateChain.get(0).getName());
	}
	
	@Test
	public void verify_that_the_right_destroy_and_create_states_are_created_when_switching_branches_in_the_tree() {
		statechart.gotoState("Q");
		
		statechart.gotoState("P");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 2 elements in destroyStateChain",  new Integer(2), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is Q", "Q", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is O", "O", statechart.destroyStateChain.get(1).getName());
		
		Assert.assertTrue("Expecting en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in createStateChain",  new Integer(1), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is P", "P", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("K");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 4 elements in destroyStateChain",  new Integer(4), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is P", "P", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in destroyStateChain is N", "N", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in destroyStateChain is M", "M", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Expecting that the 4 element in destroyStateChain is C", "C", statechart.destroyStateChain.get(3).getName());
		
		Assert.assertTrue("Expecting en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 3 elements in createStateChain",  new Integer(3), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is B", "B", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is I", "I", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Expecting that the 3rd element in createStateChain is K", "K", statechart.createStateChain.get(2).getName());
		
		statechart.gotoState("L");
		Assert.assertTrue("Expecting en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Expecting 1 elements in destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in destroyStateChain is K", "K", statechart.destroyStateChain.get(0).getName());
		
		Assert.assertTrue("Expecting en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Expecting 2 elements in createStateChain",  new Integer(2), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Expecting that the 1st element in createStateChain is J", "J", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Expecting that the 2nd element in createStateChain is L", "L", statechart.createStateChain.get(1).getName());
	}
}
