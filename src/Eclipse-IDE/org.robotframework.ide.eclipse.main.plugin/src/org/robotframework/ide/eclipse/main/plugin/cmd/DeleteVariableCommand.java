package org.robotframework.ide.eclipse.main.plugin.cmd;

import java.util.List;

import org.robotframework.ide.eclipse.main.plugin.model.RobotModelEvents;
import org.robotframework.ide.eclipse.main.plugin.model.RobotSuiteFileSection;
import org.robotframework.ide.eclipse.main.plugin.model.RobotVariable;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.EditorCommand;

public class DeleteVariableCommand extends EditorCommand {

    private final List<RobotVariable> variablesToDelete;

    public DeleteVariableCommand(final List<RobotVariable> variablesToDelete) {
        this.variablesToDelete = variablesToDelete;
    }

    @Override
    public void execute() throws CommandExecutionException {
        if (variablesToDelete.isEmpty()) {
            return;
        }
        final RobotSuiteFileSection variableSection = (RobotSuiteFileSection) variablesToDelete.get(0).getParent();
        variableSection.getChildren().removeAll(variablesToDelete);

        eventBroker.post(RobotModelEvents.ROBOT_VARIABLE_REMOVED, variableSection);
    }
}
