package com.ericsson.cifwk.taf.ui;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

/**
 * Created by ejambuc on 23/10/2014.
 */
public class UiAction {

    private final UiComponent component;
    private final Type actionType;
    private final CharSequence[] keyStrokes;

    public UiAction(Type actionType) {
        this(null, actionType, new CharSequence[0]);
    }

    public UiAction(UiComponent component, Type actionType) {
        this(component, actionType, new CharSequence[0]);
    }

    public UiAction(Type actionType, CharSequence... keyStrokes) {
        this(null, actionType, keyStrokes);
    }

    public UiAction(UiComponent component, Type actionType, CharSequence... keyStrokes) {
        this.keyStrokes = keyStrokes;
        this.actionType = actionType;
        this.component = component;
    }

    public Type getActionType() {
        return actionType;
    }

    public UiComponent getComponent() {
        return component;
    }

    public CharSequence[] getKeyStrokes() {
        return keyStrokes;
    }

    public enum Type {
        CLICK_ACTION, CONTEXT_CLICK_ACTION, MOUSE_DOWN_ACTION, MOUSE_UP_ACTION, MOUSE_OVER_ACTION, KEY_DOWN_ACTION,
        KEY_UP_ACTION, DOUBLE_CLICK_ACTION, SEND_KEYS_ACTION
    }

}
