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
		State j = i.addState(new State("I"));
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
}
