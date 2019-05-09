/*
 * Copyright 2019 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.model.cmd;

import java.util.List;

import org.robotframework.ide.eclipse.main.plugin.model.RobotCodeHoldingElement;
import org.robotframework.ide.eclipse.main.plugin.model.RobotKeywordCall;
import org.robotframework.ide.eclipse.main.plugin.model.RobotModelEvents;
import org.robotframework.ide.eclipse.main.plugin.model.cmd.ReplaceWithCallCommand.TripleFunction;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.EditorCommand;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.code.ExecutablesRowView;
import org.robotframework.services.event.RedEventBroker;

public class ReplaceWithEmptyCommand extends EditorCommand {

    public static ReplaceWithEmptyCommand replaceCall(final RobotKeywordCall call, final int index,
            final List<String> tokens) {
        return new ReplaceWithEmptyCommand(call, index, tokens, ReplaceWithCallCommand::replaceEmpty);
    }

    public static ReplaceWithEmptyCommand replaceSetting(final RobotKeywordCall setting, final int index,
            final List<String> tokens) {
        return new ReplaceWithEmptyCommand(setting, index, tokens, ReplaceWithSettingCommand::replaceEmpty);
    }

    private final RobotKeywordCall call;
    private final int index;
    private final List<String> tokens;

    private List<String> oldTokens;

    private final TripleFunction<RobotKeywordCall, Integer, List<String>, EditorCommand> undoCommandProvider;


    private ReplaceWithEmptyCommand(final RobotKeywordCall call, final int index, final List<String> tokens,
            final TripleFunction<RobotKeywordCall, Integer, List<String>, EditorCommand> undoCommandProvider) {
        this.call = call;
        this.index = index;
        this.tokens = tokens;
        this.undoCommandProvider = undoCommandProvider;
    }

    @Override
    public void execute() throws CommandExecutionException {
        oldTokens = ExecutablesRowView.rowData(call);

        final RobotCodeHoldingElement<?> parent = (RobotCodeHoldingElement<?>) call.getParent();
        parent.replaceWithEmpty(index, tokens);

        RedEventBroker.using(eventBroker).send(RobotModelEvents.ROBOT_KEYWORD_CALL_CELL_CHANGE, call);
    }

    @Override
    public List<EditorCommand> getUndoCommands() {
        return newUndoCommands(undoCommandProvider.apply(call, index, oldTokens));
    }
}
