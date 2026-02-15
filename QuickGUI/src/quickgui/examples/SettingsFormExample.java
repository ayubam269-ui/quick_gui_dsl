package quickgui.examples;

import quickgui.dsl.GUI;
import quickgui.model.LayoutSpec;
import quickgui.model.Position;

/**
 * Example 3: Settings / Preferences panel with nested panels.
 *
 * Demonstrates:
 *  - Deeply nested panel hierarchy
 *  - Titled border panels
 *  - All widget types
 *  - Grid and vertical layouts
 */
public class SettingsFormExample {

    public static void main(String[] args) {

        GUI.window("Settings")
           .size(500, 400)
           .layout(LayoutSpec.border(10, 10))

           // ── Appearance section ──
           .panel("appearance", LayoutSpec.grid(4, 2, 5, 5))
               .titled("Appearance")
               .at(Position.CENTER)
               .label("lblTheme", "Theme:")
               .comboBox("theme", "Light", "Dark", "System Default")
               .label("lblFont", "Font Size:")
               .slider("fontSize", 8, 32, 14, 4)
               .checkBox("lineNumbers", "Show line numbers", true)
               .checkBox("wordWrap", "Word wrap", false)
               .label("lblLang", "Language:")
               .comboBox("language", "English", "German", "French", "Spanish")
           .endPanel()

           // ── Network section ──
           .panel("network", LayoutSpec.grid(2, 2, 5, 5))
               .titled("Network")
               .at(Position.SOUTH)
               .label("lblProxy", "Proxy Host:")
               .textField("proxyHost", 20, "localhost")
               .label("lblPort", "Proxy Port:")
               .textField("proxyPort", 6, "8080")
           .endPanel()

           .show();
    }
}
