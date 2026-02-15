package quickgui.dsl;

import quickgui.model.*;

/**
 * Abstract base for builders that can contain child elements (Window and Panel).
 *
 * Provides the fluent API methods for adding widgets and sub-panels.
 *
 * @param <SELF> the concrete builder type (for CRTP fluent return)
 */
public abstract class ContainerBuilder<SELF extends ContainerBuilder<SELF>> {

    protected abstract SELF self();

    /** Add a child element to the underlying model with an optional position. */
    protected abstract void addChildToModel(GUIElement child, Position pos);

    /** Set the layout on the underlying model. */
    protected abstract void setLayoutOnModel(LayoutSpec layout);

    // ===== Layout =====

    /** Set the layout strategy for this container. */
    public SELF layout(LayoutSpec layout) {
        setLayoutOnModel(layout);
        return self();
    }

    // ===== Label =====

    /** Add a label with auto-generated name. */
    public SELF label(String text) {
        return label(text, text);
    }

    /** Add a label with explicit name. */
    public SELF label(String name, String text) {
        Label lbl = new Label(name, text);
        addChildToModel(lbl, null);
        return self();
    }

    /** Add a label at a specific BorderLayout position. */
    public SELF label(String name, String text, Position pos) {
        Label lbl = new Label(name, text);
        lbl.setPosition(pos);
        addChildToModel(lbl, pos);
        return self();
    }

    // ===== Button =====

    /** Add a button with a label text. */
    public SELF button(String label) {
        return button(label, label, null);
    }

    /** Add a button with a label and click action. */
    public SELF button(String label, Runnable action) {
        return button(label, label, action);
    }

    /** Add a button with explicit name, label, and optional action. */
    public SELF button(String name, String label, Runnable action) {
        Button btn = new Button(name, label);
        if (action != null) btn.setAction(action);
        addChildToModel(btn, null);
        return self();
    }

    /** Add a button at a specific BorderLayout position. */
    public SELF button(String name, String label, Runnable action, Position pos) {
        Button btn = new Button(name, label);
        if (action != null) btn.setAction(action);
        btn.setPosition(pos);
        addChildToModel(btn, pos);
        return self();
    }

    // ===== TextField =====

    /** Add a text field with default settings. */
    public SELF textField(String name) {
        TextField tf = new TextField(name);
        addChildToModel(tf, null);
        return self();
    }

    /** Add a text field with specified column width. */
    public SELF textField(String name, int columns) {
        TextField tf = new TextField(name);
        tf.setColumns(columns);
        addChildToModel(tf, null);
        return self();
    }

    /** Add a text field with column width and default text. */
    public SELF textField(String name, int columns, String defaultText) {
        TextField tf = new TextField(name);
        tf.setColumns(columns);
        tf.setDefaultText(defaultText);
        addChildToModel(tf, null);
        return self();
    }

    /** Add a text field at a specific BorderLayout position. */
    public SELF textField(String name, int columns, String defaultText, Position pos) {
        TextField tf = new TextField(name);
        tf.setColumns(columns);
        tf.setDefaultText(defaultText);
        tf.setPosition(pos);
        addChildToModel(tf, pos);
        return self();
    }

    // ===== TextArea =====

    /** Add a text area with default settings. */
    public SELF textArea(String name) {
        TextArea ta = new TextArea(name);
        addChildToModel(ta, null);
        return self();
    }

    /** Add a text area with specific dimensions. */
    public SELF textArea(String name, int rows, int cols) {
        TextArea ta = new TextArea(name);
        ta.setRows(rows);
        ta.setCols(cols);
        addChildToModel(ta, null);
        return self();
    }

    /** Add a text area at a BorderLayout position. */
    public SELF textArea(String name, int rows, int cols, Position pos) {
        TextArea ta = new TextArea(name);
        ta.setRows(rows);
        ta.setCols(cols);
        ta.setPosition(pos);
        addChildToModel(ta, pos);
        return self();
    }

    // ===== CheckBox =====

    /** Add a check box. */
    public SELF checkBox(String name, String label) {
        CheckBox cb = new CheckBox(name, label);
        addChildToModel(cb, null);
        return self();
    }

    /** Add a check box with initial state. */
    public SELF checkBox(String name, String label, boolean selected) {
        CheckBox cb = new CheckBox(name, label);
        cb.setSelected(selected);
        addChildToModel(cb, null);
        return self();
    }

    // ===== ComboBox =====

    /** Add a combo box (dropdown) with the given items. */
    public SELF comboBox(String name, String... items) {
        ComboBox cb = new ComboBox(name, items);
        addChildToModel(cb, null);
        return self();
    }

    // ===== Slider =====

    /** Add a slider with the given range. */
    public SELF slider(String name, int min, int max, int value) {
        Slider s = new Slider(name);
        s.setMin(min);
        s.setMax(max);
        s.setValue(value);
        addChildToModel(s, null);
        return self();
    }

    /** Add a slider with ticks shown. */
    public SELF slider(String name, int min, int max, int value, int majorTickSpacing) {
        Slider s = new Slider(name);
        s.setMin(min);
        s.setMax(max);
        s.setValue(value);
        s.setShowTicks(true);
        s.setMajorTickSpacing(majorTickSpacing);
        addChildToModel(s, null);
        return self();
    }

    // ===== Separator =====

    /** Add a horizontal separator. */
    public SELF separator() {
        Separator sep = new Separator("sep", Separator.Orientation.HORIZONTAL);
        addChildToModel(sep, null);
        return self();
    }

    /** Add a separator with explicit orientation. */
    public SELF separator(Separator.Orientation orientation) {
        Separator sep = new Separator("sep", orientation);
        addChildToModel(sep, null);
        return self();
    }

    // ===== Panel (nested container) =====

    /** Start building a sub-panel. Call endPanel() to return to this builder. */
    public PanelBuilder<SELF> panel(String name) {
        return new PanelBuilder<>(name, self(), this::addChildToModel);
    }

    /** Start building a sub-panel with a specific layout. */
    public PanelBuilder<SELF> panel(String name, LayoutSpec layout) {
        PanelBuilder<SELF> pb = new PanelBuilder<>(name, self(), this::addChildToModel);
        pb.layout(layout);
        return pb;
    }
}
