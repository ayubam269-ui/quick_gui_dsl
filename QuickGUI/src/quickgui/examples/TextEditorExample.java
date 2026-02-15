package quickgui.examples;

import quickgui.dsl.GUI;
import quickgui.model.LayoutSpec;
import quickgui.model.Position;

/**
 * Example 2: A text editor prototype built using the QuickGUI internal DSL.
 *
 * Demonstrates:
 *  - BorderLayout for main structure
 *  - TextArea with scrolling in the center
 *  - Toolbar panel at the top with buttons
 *  - Status bar at the bottom
 *  - Combo box for font selection
 */
public class TextEditorExample {

    public static void main(String[] args) {

        GUI.window("QuickEdit — Text Editor")
           .size(600, 400)
           .layout(LayoutSpec.border())

           // ── Toolbar at the top ──
           .panel("toolbar", LayoutSpec.flow(5, 2))
               .at(Position.NORTH)
               .button("New",  () -> System.out.println("New file"))
               .button("Open", () -> System.out.println("Open file"))
               .button("Save", () -> System.out.println("Save file"))
               .separator()
               .comboBox("font", "Monospaced", "Arial", "Times New Roman", "Courier New")
               .slider("fontSize", 8, 48, 14, 8)
           .endPanel()

           // ── Main editing area ──
           .textArea("editor", 20, 60, Position.CENTER)

           // ── Status bar at the bottom ──
           .panel("statusBar", LayoutSpec.flow(5, 0))
               .at(Position.SOUTH)
               .label("status", "Ready")
               .label("cursor", "Ln 1, Col 1")
           .endPanel()

           .show();
    }
}
