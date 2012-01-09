JavaKI
======================================
 
JavaKI is a port of the Statechart JavaScript framework KI (https://github.com/FrozenCanuck/Ki). More information will be made available shortly. 

## Downloads
For each build issued by the CI system (Jenins), artifacts will be published to http://nightly.haagen.name/JavaKI/

## Final Release

A final release will be made available during November 2011. 

## Contributors

* Joachim Haagen Skeie

## License

JavaKI is distributed through the MIT license. 

## Examples

States are created with a name, an enter action and an exit action. Both the enter state action and the exit state action can be null. 

```java
  State showMainFrameState = new State("stateName", new EnterStateAction(), new ExitStateAction);
```

```java
  State rootState = new State("Root");
  State showMainFrameState = new State("showMainFrame", new InitializeMainFrame(), null);
  showMainFrameState.addAction("clickAddNewProjectAction", new ClickAddNewProjectAction());
		
  State showSelectedProjectState = new State("selectedProjectState", new InitializeSelectedProjectAction(), null);
  showSelectedProjectState.addAction("saveSelectedProjectAction", new SaveSelectedProjectAction());
  showMainFrameState.addState(showSelectedProjectState);
  
  rootState.addState(showMainFrameState);
  rootState.setInitialState("showMainFrame");
  
  Statechart statechart = new Statechart("My Application State Chart", rootState);
  statechart.initializeStatechart();
```
The call to statechart.initializeStatechart() will set up the statechart and navigate to the statecharts initial state. 

Then, you can navigate between states or call actions on the current state like so: 

```java
  statechart.gotoState(stateName);
```

```java
  statechart.sendEvent(eventName);
```

You can implement your own Actions by implementing the StateAction interface: 

```java
package com.akvaplan.statechart.action;

import java.util.Locale;

import javax.swing.UIManager;

import org.javaki.StateAction;

import com.myapp.MyApplicatoon;

public class InitializeMainFrameAction implements StateAction {
 private MyApplication gui;
	public InitialiserArbeidsmappevelgerAction(MyApplication gui) {
		this.gui = gui;
	}
	
	@Override
	public String invokeAction() {
        System.out.println("InitializeMainFrameAction");
        Locale locale = Locale.getDefault();
        locale.setDefault(Locale.US);

        //Set UI Manager to Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        
        //Initialize the main Frame
        gui.initializeWorkspaceChooser();
        
        //return null to stay at the current state, or return a string with the
        //state name to go to after this action is invoked
        return null;
	}

}

```