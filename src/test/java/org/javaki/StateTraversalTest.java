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
		Assert.assertNotNull("Forventer en state", actual);
		Assert.assertEquals("Forventer state G", actual.getName(), "G");
		
		actual = statechart.gotoState("H");
		Assert.assertNotNull("Forventer en state", actual);
		Assert.assertEquals("Forventer state H", actual.getName(), "H");
		
		actual = statechart.gotoState("B");
		Assert.assertNotNull("Forventer en state", actual);
		Assert.assertEquals("Forventer state B", actual.getName(), "B");
		
		actual = statechart.gotoState("N");
		Assert.assertNotNull("Forventer en state", actual);
		Assert.assertEquals("Forventer state N", actual.getName(), "N");
		
		actual = statechart.gotoState("X");
		Assert.assertNull("Forventer ikke en state", actual);
		
		actual = m.getSubstate("Q");
		Assert.assertNotNull("Forventer en state", actual);
		Assert.assertEquals("Forventer state Q", actual.getName(), "Q");
	}
	
	@Test
	public void verify_that_state_can_identify_parent_state() {
		State actual = q.getParentState("C");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Forventer state C", actual.getName(), "C");
		
		actual = q.getParentState("O");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Forventer state O", actual.getName(), "O");
		
		actual = q.getParentState("P");
		Assert.assertNull("P is not a parent of Q", actual);
		
		actual = q.getParentState("N");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Forventer state N", actual.getName(), "N");
		
		actual = q.getParentState("M");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Forventer state M", actual.getName(), "M");
		
		actual = q.getParentState("ROOT");
		Assert.assertNotNull(actual);
		Assert.assertEquals("Forventer state ROOT", actual.getName(), "ROOT");

		actual = q.getParentState("B");
		Assert.assertNull("B is not a parent of Q", actual);
		
		actual = q.getParentState("X");
		Assert.assertNull("X is not a parent of Q", actual);
	}
	
	@Test
	public void verify_that_the_right_create_states_is_created_when_moving_to_substate() {
		statechart.gotoState("G");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 3 elementer i createStateChain",  new Integer(3), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i createStateChain er A", "A", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i createStateChain er F", "F", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Forventer at 3 element i createStateChain er G", "G", statechart.createStateChain.get(2).getName());
		
		statechart.gotoState("H");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er H", "H", statechart.createStateChain.get(0).getName());
		
		initializeStatechart();
		statechart.gotoState("C");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er C", "C", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("M");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er M", "M", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("N");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er N", "N", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er O", "O", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("Q");
		Assert.assertTrue("Forventer en ikke tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain", new Integer(1), new Integer(statechart.createStateChain.size()) );
		Assert.assertEquals("Forventer at 1 element i createStateChain er Q", "Q", statechart.createStateChain.get(0).getName());
	}
	
	@Test
	public void verify_that_the_right_destroy_states_is_creates_when_moving_to_parent_state() {
		statechart.gotoState("Q");
		
		statechart.gotoState("M");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 3 elementer i destroyStateChain",  new Integer(3), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er Q", "Q", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i destroyStateChain er O", "O", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Forventer at 3 element i destroyStateChain er N", "N", statechart.destroyStateChain.get(2).getName());
		
		initializeStatechart();
		statechart.gotoState("Q");
		Assert.assertTrue("Forventer en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 5 elementer i createStateChain",  new Integer(5), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i createStateChain er C", "C", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i createStateChain er M", "M", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Forventer at 3 element i createStateChain er N", "N", statechart.createStateChain.get(2).getName());
		Assert.assertEquals("Forventer at 4 element i createStateChain er O", "O", statechart.createStateChain.get(3).getName());
		Assert.assertEquals("Forventer at 5 element i createStateChain er Q", "Q", statechart.createStateChain.get(4).getName());
		
		statechart.gotoState("O");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er Q", "Q", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("N");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er O", "O", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("M");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er N", "N", statechart.destroyStateChain.get(0).getName());
		
		statechart.gotoState("C");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er M", "M", statechart.destroyStateChain.get(0).getName());
	}
	
	@Test
	public void verify_that_the_right_destroy_and_create_states_are_created_when_switching_branches_in_the_tree() {
		statechart.gotoState("Q");
		
		statechart.gotoState("P");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 2 elementer i destroyStateChain",  new Integer(2), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er Q", "Q", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i destroyStateChain er O", "O", statechart.destroyStateChain.get(1).getName());
		
		Assert.assertTrue("Forventer en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i createStateChain",  new Integer(1), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i createStateChain er P", "P", statechart.createStateChain.get(0).getName());
		
		statechart.gotoState("K");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 4 elementer i destroyStateChain",  new Integer(4), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er P", "P", statechart.destroyStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i destroyStateChain er N", "N", statechart.destroyStateChain.get(1).getName());
		Assert.assertEquals("Forventer at 3 element i destroyStateChain er M", "M", statechart.destroyStateChain.get(2).getName());
		Assert.assertEquals("Forventer at 4 element i destroyStateChain er C", "C", statechart.destroyStateChain.get(3).getName());
		
		Assert.assertTrue("Forventer en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 3 elementer i createStateChain",  new Integer(3), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i createStateChain er B", "B", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i createStateChain er I", "I", statechart.createStateChain.get(1).getName());
		Assert.assertEquals("Forventer at 3 element i createStateChain er K", "K", statechart.createStateChain.get(2).getName());
		
		statechart.gotoState("L");
		Assert.assertTrue("Forventer en ikke-tom destroyStateChain", statechart.destroyStateChain.size() > 0);
		Assert.assertEquals("Forventer 1 elementer i destroyStateChain",  new Integer(1), new Integer(statechart.destroyStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i destroyStateChain er K", "K", statechart.destroyStateChain.get(0).getName());
		
		Assert.assertTrue("Forventer en ikke-tom createStateChain", statechart.createStateChain.size() > 0);
		Assert.assertEquals("Forventer 2 elementer i createStateChain",  new Integer(2), new Integer(statechart.createStateChain.size()));
		Assert.assertEquals("Forventer at 1 element i createStateChain er J", "J", statechart.createStateChain.get(0).getName());
		Assert.assertEquals("Forventer at 2 element i createStateChain er L", "L", statechart.createStateChain.get(1).getName());
	}
}
