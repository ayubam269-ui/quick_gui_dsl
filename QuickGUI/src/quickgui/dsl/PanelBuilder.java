package quickgui.dsl;

import quickgui.model.*;

/**
 * Fluent builder for constructing a Panel model node.
 *
 * A PanelBuilder can add children just like a WindowBuilder.
 * It has an endPanel() method to return to the parent builder.
 *
 * @param <P> the type of the parent builder (for fluent return)
 */
public class PanelBuilder<P> extends ContainerBuilder<PanelBuilder<P>> {
    private final Panel panel;
    private final P parent;
    private final java.util.function.BiConsumer<GUIElement, Position> addToParent;

    PanelBuilder(String name, P parent, java.util.function.BiConsumer<GUIElement, Position> addToParent) {
        this.panel = new Panel(name);
        this.parent = parent;
        this.addToParent = addToParent;
    }

    @Override
    protected PanelBuilder<P> self() { return this; }

    @Override
    protected void addChildToModel(GUIElement child, Position pos) {
        panel.addChild(child, pos);
    }

    @Override
    protected void setLayoutOnModel(LayoutSpec layout) {
        panel.setLayout(layout);
    }

    /* --- panel-specific configuration --- */

    /** Add a titled border around this panel. */
    public PanelBuilder<P> titled(String title) {
        panel.setBorderTitle(title);
        return this;
    }

    /** Set the position of this panel within a parent BorderLayout. */
    public PanelBuilder<P> at(Position pos) {
        panel.setPosition(pos);
        return this;
    }

    /* --- end panel and return to parent --- */

    /**
     * Finalize this panel, add it to the parent container, and return
     * to the parent builder for further configuration.
     */
    public P endPanel() {
        addToParent.accept(panel, panel.getPosition());
        return parent;
    }
}
