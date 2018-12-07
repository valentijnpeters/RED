/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rf.ide.core.testdata.model.presenter.MoveElementHelper;
import org.rf.ide.core.testdata.text.read.IRobotTokenType;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class DictionaryVariable extends AVariable {

    private static final long serialVersionUID = 8147929506520793514L;

    private final List<DictionaryKeyValuePair> items = new ArrayList<>();

    public DictionaryVariable(final String name, final RobotToken declaration, final VariableScope scope) {
        super(VariableType.DICTIONARY, name, declaration, scope);
    }

    public void put(final DictionaryKeyValuePair pair) {
        pair.fixTypes();
        items.add(pair);
    }

    public void put(final RobotToken raw, final RobotToken key, final RobotToken value) {
        fixForTheType(raw, RobotTokenType.VARIABLES_VARIABLE_VALUE);
        fixForTheType(key, RobotTokenType.VARIABLES_DICTIONARY_KEY);
        fixForTheType(value, RobotTokenType.VARIABLES_DICTIONARY_VALUE);
        items.add(new DictionaryKeyValuePair(raw, key, value));
    }

    public void addKeyValuePair(final int position, final DictionaryKeyValuePair pair) {
        pair.fixTypes();
        items.add(position, pair);
    }

    public void removeKeyValuePair(final DictionaryKeyValuePair pair) {
        items.remove(pair);
    }

    public boolean moveLeftKeyValuePair(final DictionaryKeyValuePair pair) {
        return MoveElementHelper.moveLeft(items, pair);
    }

    public boolean moveRightKeyValuePair(final DictionaryKeyValuePair pair) {
        return MoveElementHelper.moveRight(items, pair);
    }

    public List<DictionaryKeyValuePair> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public List<RobotToken> getValueTokens() {
        final List<RobotToken> valueTokens = new ArrayList<>();
        for (final DictionaryKeyValuePair pair : items) {
            valueTokens.add(pair.getRaw());
        }
        return Collections.unmodifiableList(valueTokens);
    }

    @Override
    public boolean isPresent() {
        return (getDeclaration() != null);
    }

    @Override
    public List<RobotToken> getElementTokens() {
        final List<RobotToken> tokens = new ArrayList<>();
        if (isPresent()) {
            tokens.add(getDeclaration());
            for (final DictionaryKeyValuePair p : items) {
                if (p.getRaw() != null) {
                    tokens.add(p.getRaw());
                }
            }
            tokens.addAll(getComment());
        }

        return tokens;
    }

    @Override
    public boolean removeElementToken(final int index) {
        return false;
    }

    public static class DictionaryKeyValuePair implements Serializable {

        private static final long serialVersionUID = 5844838131341640455L;

        public static DictionaryKeyValuePair createFromRaw(final String raw) {
            final List<String> splitted = Splitter.on('=').splitToList(raw);
            final String key = splitted.get(0);
            final String value = Joiner.on('=').join(splitted.subList(1, splitted.size()));

            final RobotToken rawToken = RobotToken.create(raw, RobotTokenType.VARIABLES_VARIABLE_VALUE);
            final RobotToken keyToken = RobotToken.create(key, RobotTokenType.VARIABLES_DICTIONARY_KEY);
            final RobotToken valueToken = RobotToken.create(value, RobotTokenType.VARIABLES_DICTIONARY_VALUE);

            return new DictionaryKeyValuePair(rawToken, keyToken, valueToken);
        }

        private RobotToken raw;

        private RobotToken key;

        private RobotToken value;

        public DictionaryKeyValuePair(final RobotToken raw, final RobotToken key, final RobotToken value) {
            this.raw = fixForTheType(raw, RobotTokenType.VARIABLES_VARIABLE_VALUE, true);
            this.key = fixForTheType(key, RobotTokenType.VARIABLES_DICTIONARY_KEY, true);
            this.value = fixForTheType(value, RobotTokenType.VARIABLES_DICTIONARY_VALUE, true);
        }

        public RobotToken getKey() {
            return key;
        }

        public RobotToken getValue() {
            return value;
        }

        public RobotToken getRaw() {
            return raw;
        }

        public void set(final String raw) {
            final DictionaryKeyValuePair newElement = createFromRaw(raw);
            this.raw = newElement.raw;
            this.key = newElement.key;
            this.value = newElement.value;
        }

        private RobotToken fixForTheType(final RobotToken token, final IRobotTokenType expectedMainType,
                final boolean shouldNullCheck) {
            if (shouldNullCheck && token == null) {
                return null;
            }
            return fixForTheType(token, expectedMainType);
        }

        private void fixTypes() {
            fixForTheType(raw, RobotTokenType.VARIABLES_VARIABLE_VALUE);
            fixForTheType(key, RobotTokenType.VARIABLES_DICTIONARY_KEY);
            fixForTheType(value, RobotTokenType.VARIABLES_DICTIONARY_VALUE);
        }

        private RobotToken fixForTheType(final RobotToken token, final IRobotTokenType expectedMainType) {
            final List<IRobotTokenType> tagTypes = token.getTypes();
            if (!tagTypes.contains(expectedMainType)) {
                tagTypes.add(0, expectedMainType);
            }
            return token;
        }
    }
}
