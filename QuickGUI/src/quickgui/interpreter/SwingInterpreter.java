package quickgui.interpreter;

import quickgui.model.Button;
import quickgui.model.CheckBox;
import quickgui.model.ComboBox;
import quickgui.model.GUIElement;
import quickgui.model.GUIVisitor;
import quickgui.model.Label;
import quickgui.model.LayoutSpec;
import quickgui.model.LayoutType;
import quickgui.model.Panel;
import quickgui.model.Position;
import quickgui.model.Separator;
import quickgui.model.Slider;
import quickgui.model.TextArea;
import quickgui.model.TextField;
import quickgui.model.Widget;
import quickgui.model.Window;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;

/**
 * Interpreter: walks the GUI meta-model and produces a live Swing window.
 *
 * Implements the GUIVisitor to traverse the model tree and create
 * corresponding javax.swing components.
 */
public class SwingInterpreter implements GUIVisitor {

    /** The Swing component most recently constructed by a visit() call. */
    private JComponent currentComponent;

    /**
     * Interpret a Window model: create and display the Swing JFrame.
     */
    public void interpret(Window window) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(window.getTitle());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(window.getWidth(), window.getHeight());
            frame.setLayout(createLayout(window.getLayout()));

            addChildren(frame.getContentPane(), window.getChildren(),
                        window.getPositions(), window.getLayout());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // ───────── Visitor methods ─────────

    @Override
    public void visit(Window window) {
        // top-level is handled by interpret()
    }

    @Override
    public void visit(Panel panel) {
        JPanel jp = new JPanel(createLayout(panel.getLayout()));

        if (panel.getBorderTitle() != null) {
            jp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), panel.getBorderTitle()));
        }

        addChildren(jp, panel.getChildren(), panel.getPositions(), panel.getLayout());
        currentComponent = jp;
    }

    @Override
    public void visit(Label label) {
        JLabel jl = new JLabel(label.getText());
        applyTooltip(jl, label);
        currentComponent = jl;
    }

    @Override
    public void visit(Button button) {
        JButton jb = new JButton(button.getLabel());
        if (button.getAction() != null) {
            jb.addActionListener(e -> button.getAction().run());
        }
        applyTooltip(jb, button);
        currentComponent = jb;
    }

    @Override
    public void visit(TextField textField) {
        JTextField jtf = new JTextField(textField.getDefaultText(), textField.getColumns());
        applyTooltip(jtf, textField);
        currentComponent = jtf;
    }

    @Override
    public void visit(TextArea textArea) {
        JTextArea jta = new JTextArea(textArea.getDefaultText(),
                                       textArea.getRows(), textArea.getCols());
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        applyTooltip(jta, textArea);
        if (textArea.isScrollable()) {
            currentComponent = new JScrollPane(jta);
        } else {
            currentComponent = jta;
        }
    }

    @Override
    public void visit(CheckBox checkBox) {
        JCheckBox jcb = new JCheckBox(checkBox.getLabel(), checkBox.isSelected());
        applyTooltip(jcb, checkBox);
        currentComponent = jcb;
    }

    @Override
    public void visit(ComboBox comboBox) {
        JComboBox<String> jcb = new JComboBox<>(comboBox.getItems().toArray(new String[0]));
        jcb.setSelectedIndex(comboBox.getSelectedIndex());
        applyTooltip(jcb, comboBox);
        currentComponent = jcb;
    }

    @Override
    public void visit(Slider slider) {
        JSlider js = new JSlider(slider.getMin(), slider.getMax(), slider.getValue());
        if (slider.isShowTicks()) {
            js.setMajorTickSpacing(slider.getMajorTickSpacing());
            js.setPaintTicks(true);
            js.setPaintLabels(true);
        }
        applyTooltip(js, slider);
        currentComponent = js;
    }

    @Override
    public void visit(Separator separator) {
        int orientation = (separator.getOrientation() == Separator.Orientation.HORIZONTAL)
                          ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL;
        currentComponent = new JSeparator(orientation);
    }

    // ───────── helpers ─────────

    private void addChildren(Container parent, List<GUIElement> children,
                              List<Position> positions, LayoutSpec layout) {
        for (int i = 0; i < children.size(); i++) {
            GUIElement child = children.get(i);
            Position   pos   = positions.get(i);

            child.accept(this); // sets currentComponent
            JComponent comp = currentComponent;

            if (layout.getType() == LayoutType.BORDER && pos != null) {
                parent.add(comp, toBorderConstraint(pos));
            } else {
                parent.add(comp);
            }
        }
    }

    private LayoutManager createLayout(LayoutSpec spec) {
        switch (spec.getType()) {
            case FLOW:
                return new FlowLayout(FlowLayout.LEFT, spec.getHgap(), spec.getVgap());
            case GRID:
                return new GridLayout(spec.getRows(), spec.getCols(),
                                      spec.getHgap(), spec.getVgap());
            case BORDER:
                return new BorderLayout(spec.getHgap(), spec.getVgap());
            case VERTICAL:
                // BoxLayout requires the target container — use a wrapper panel
                return new BoxLayout(new JPanel(), BoxLayout.Y_AXIS);
            case HORIZONTAL:
                return new BoxLayout(new JPanel(), BoxLayout.X_AXIS);
            default:
                return new FlowLayout();
        }
    }

    private String toBorderConstraint(Position pos) {
        switch (pos) {
            case NORTH:  return BorderLayout.NORTH;
            case SOUTH:  return BorderLayout.SOUTH;
            case EAST:   return BorderLayout.EAST;
            case WEST:   return BorderLayout.WEST;
            case CENTER: return BorderLayout.CENTER;
            default:     return BorderLayout.CENTER;
        }
    }

    private void applyTooltip(JComponent comp, Widget widget) {
        if (widget.getTooltip() != null) {
            comp.setToolTipText(widget.getTooltip());
        }
    }
}
