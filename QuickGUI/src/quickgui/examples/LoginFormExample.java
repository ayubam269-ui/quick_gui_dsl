package quickgui.examples;

import quickgui.dsl.GUI;
import quickgui.model.LayoutSpec;
import quickgui.model.Position;

/**
 * Example 1: A simple login form built using the QuickGUI internal DSL.
 *
 * Demonstrates:
 *  - Window with BorderLayout
 *  - Nested panels with GridLayout and FlowLayout
 *  - Labels, TextFields, Buttons, CheckBox
 *  - Event handling via lambda actions
 *
 * DSL program (compare with 50+ lines of raw Swing code!):
 */
public class LoginFormExample {

    public static void main(String[] args) {

        GUI.window("Login")
           .size(350, 200)
           .layout(LayoutSpec.border(5, 5))

           // ── Title label at the top ──
           .label("title", "Please sign in", Position.NORTH)

           // ── Form fields in the center ──
           .panel("form", LayoutSpec.grid(3, 2, 5, 5))
               .at(Position.CENTER)
               .label("lblUser", "Username:")
               .textField("username", 15)
               .label("lblPass", "Password:")
               .textField("password", 15)
               .label("lblEmpty", "")
               .checkBox("remember", "Remember me")
           .endPanel()

           // ── Buttons at the bottom ──
           .panel("buttons", LayoutSpec.flow(10, 5))
               .at(Position.SOUTH)
               .button("Login", () -> System.out.println("Login clicked!"))
               .button("Cancel", () -> System.out.println("Cancel clicked!"))
           .endPanel()

           .show();
    }
}
