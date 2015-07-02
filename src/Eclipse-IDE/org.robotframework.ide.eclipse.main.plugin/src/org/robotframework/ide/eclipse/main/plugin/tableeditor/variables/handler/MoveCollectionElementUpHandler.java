package org.robotframework.ide.eclipse.main.plugin.tableeditor.variables.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.compat.parts.DIHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.robotframework.ide.eclipse.main.plugin.RobotCollectionElement;
import org.robotframework.ide.eclipse.main.plugin.RobotModelEvents;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.variables.handler.MoveCollectionElementUpHandler.E4MoveCollectionElementUpHandler;
import org.robotframework.viewers.Selections;

public class MoveCollectionElementUpHandler extends DIHandler<E4MoveCollectionElementUpHandler> {

    public MoveCollectionElementUpHandler() {
        super(E4MoveCollectionElementUpHandler.class);
    }

    public static class E4MoveCollectionElementUpHandler {

        @Inject
        protected IEventBroker eventBroker;

        @Execute
        public Object moveCollectionElementUp(@Named(Selections.SELECTION) final IStructuredSelection selection) {

            final RobotCollectionElement selectedValue = Selections.getSingleElement(selection,
                    RobotCollectionElement.class);
            eventBroker.send(RobotModelEvents.ROBOT_VARIABLE_COLLECTION_ELEMENT_MOVE_UP, selectedValue);

            return null;
        }
    }
}
