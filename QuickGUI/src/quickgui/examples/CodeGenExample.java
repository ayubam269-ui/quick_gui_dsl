package quickgui.examples;

import quickgui.dsl.GUI;
import quickgui.model.LayoutSpec;
import quickgui.model.Position;

/**
 * Example 4: Demonstrates the code generation feature.
 *
 * Instead of showing the GUI, this example generates a standalone
 * Java source file that can be compiled and run independently.
 */
public class CodeGenExample {

    public static void main(String[] args) {

        String generatedCode = GUI.window("Generated App")
            .size(400, 300)
            .layout(LayoutSpec.border(5, 5))

            .label("header", "Welcome to the App!", Position.NORTH)

            .panel("form", LayoutSpec.grid(2, 2, 5, 5))
                .at(Position.CENTER)
                .label("lblName", "Name:")
                .textField("name", 20)
                .label("lblEmail", "Email:")
                .textField("email", 20)
            .endPanel()

            .panel("actions", LayoutSpec.flow())
                .at(Position.SOUTH)
                .button("Submit", () -> {})
                .button("Clear", () -> {})
            .endPanel()

            .generateCode("GeneratedApp");

        System.out.println("=== Generated Java Code ===");
        System.out.println(generatedCode);
        System.out.println("=== End of Generated Code ===");
    }
}
