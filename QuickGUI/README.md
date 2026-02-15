# QuickGUI — Internal DSL for Quick GUI Prototyping

A **model-driven** internal DSL in Java for rapidly prototyping graphical user
interfaces.  Instead of writing 50+ lines of Swing boilerplate, describe your
GUI in a few lines of fluent Java and either **show it instantly** (interpreter)
or **generate standalone Java code** (code generator).

---

## Project Structure

```
QuickGUI/
├── docs/
│   └── BNF_GRAMMAR.md          # Formal BNF grammar of the DSL
├── src/
│   └── quickgui/
│       ├── model/               # Meta-model (domain classes)
│       │   ├── GUIElement.java      Abstract root of all elements
│       │   ├── GUIVisitor.java      Visitor interface
│       │   ├── Window.java          Top-level container
│       │   ├── Panel.java           Nested container
│       │   ├── Widget.java          Abstract leaf widget
│       │   ├── Label.java           Static text
│       │   ├── Button.java          Clickable button
│       │   ├── TextField.java       Single-line input
│       │   ├── TextArea.java        Multi-line input
│       │   ├── CheckBox.java        Boolean toggle
│       │   ├── ComboBox.java        Dropdown selector
│       │   ├── Slider.java          Range selector
│       │   ├── Separator.java       Visual divider
│       │   ├── LayoutSpec.java      Layout configuration
│       │   ├── LayoutType.java      Layout type enum
│       │   └── Position.java        BorderLayout position enum
│       ├── dsl/                 # Internal DSL (fluent builders)
│       │   ├── GUI.java             Entry point: GUI.window(...)
│       │   ├── WindowBuilder.java   Builds Window model
│       │   ├── PanelBuilder.java    Builds Panel model
│       │   └── ContainerBuilder.java Shared widget-adding API
│       ├── interpreter/         # Semantics: Swing interpreter
│       │   └── SwingInterpreter.java
│       ├── codegen/             # Semantics: Java code generator
│       │   └── JavaCodeGenerator.java
│       └── examples/            # DSL example programs
│           ├── LoginFormExample.java
│           ├── TextEditorExample.java
│           ├── SettingsFormExample.java
│           └── CodeGenExample.java
└── README.md
```

---

## Quick Start

### Compile

```bash
cd QuickGUI
javac -d out src/quickgui/model/*.java src/quickgui/dsl/*.java \
      src/quickgui/interpreter/*.java src/quickgui/codegen/*.java \
      src/quickgui/examples/*.java
```

### Run an Example

```bash
# Show the login form GUI
java -cp out quickgui.examples.LoginFormExample

# Show the text editor GUI
java -cp out quickgui.examples.TextEditorExample

# Show the settings form GUI
java -cp out quickgui.examples.SettingsFormExample

# Generate standalone Java code (prints to stdout)
java -cp out quickgui.examples.CodeGenExample
```

---

## DSL Usage

### Minimal Example

```java
import quickgui.dsl.GUI;

public class HelloGUI {
    public static void main(String[] args) {
        GUI.window("Hello")
           .size(300, 100)
           .label("greeting", "Hello, World!")
           .show();
    }
}
```

### Login Form (full example)

```java
import quickgui.dsl.GUI;
import quickgui.model.*;

GUI.window("Login")
   .size(350, 200)
   .layout(LayoutSpec.border(5, 5))

   .label("title", "Please sign in", Position.NORTH)

   .panel("form", LayoutSpec.grid(3, 2, 5, 5))
       .at(Position.CENTER)
       .label("lblUser", "Username:")
       .textField("username", 15)
       .label("lblPass", "Password:")
       .textField("password", 15)
       .label("lblEmpty", "")
       .checkBox("remember", "Remember me")
   .endPanel()

   .panel("buttons", LayoutSpec.flow(10, 5))
       .at(Position.SOUTH)
       .button("Login", () -> System.out.println("Login clicked!"))
       .button("Cancel", () -> System.out.println("Cancel clicked!"))
   .endPanel()

   .show();
```

### Code Generation

```java
String code = GUI.window("My App")
    .label("msg", "Hello")
    .button("OK", () -> {})
    .generateCode("MyApp");

System.out.println(code);  // prints compilable Java Swing code
```

---

## Architecture & Design

### Meta-model

The meta-model captures the essential concepts of a GUI:

```
GUIElement (abstract)
├── Window          — top-level container (title, size, layout, children)
├── Panel           — nested container (layout, border-title, children)
└── Widget (abstract) — leaf element with optional position & tooltip
    ├── Label       — static text
    ├── Button      — clickable with Runnable action
    ├── TextField   — single-line input (columns, default text)
    ├── TextArea    — multi-line input (rows, cols, scrollable)
    ├── CheckBox    — boolean toggle
    ├── ComboBox    — dropdown (list of items)
    ├── Slider      — range input (min, max, ticks)
    └── Separator   — visual divider line

LayoutSpec          — layout strategy: FLOW | GRID | BORDER | VERTICAL | HORIZONTAL
Position            — BorderLayout constraint: NORTH | SOUTH | EAST | WEST | CENTER
```

### Design Patterns Used

| Pattern             | Where                          | Purpose                                    |
|---------------------|--------------------------------|--------------------------------------------|
| **Builder**         | `WindowBuilder`, `PanelBuilder`| Fluent construction of model objects        |
| **CRTP**            | `ContainerBuilder<SELF>`       | Type-safe fluent return in inheritance      |
| **Visitor**         | `GUIVisitor`                   | Separate traversal from model structure     |
| **Interpreter**     | `SwingInterpreter`             | Execute model as live Swing GUI             |
| **Code Generator**  | `JavaCodeGenerator`            | Transform model to Java source code         |
| **Composite**       | `Window` / `Panel` / `Widget`  | Tree structure of GUI elements              |

### Internal DSL Techniques

1. **Method chaining**: Every builder method returns `this` (or `self()`) for fluent chains.
2. **Nested builders**: `.panel(...) ... .endPanel()` creates a scope that returns
   to the parent builder with the correct type.
3. **Generics (CRTP)**: `ContainerBuilder<SELF>` ensures that methods inherited from the
   base class return the concrete builder type, maintaining fluency.
4. **Terminal operations**: `.show()`, `.build()`, `.generateCode()` finalize the model
   and trigger semantics (interpretation or code generation).
5. **Factory methods**: `LayoutSpec.flow()`, `LayoutSpec.grid(2,3)` read like DSL keywords.

---

## Learning Objectives Covered

| Objective                                  | Implementation                              |
|--------------------------------------------|---------------------------------------------|
| Design a meta-model                        | `model/` package: class hierarchy, enums     |
| Design DSL syntax matching a meta-model    | `dsl/` package: fluent API mirrors the model |
| Express syntax using BNF                   | `docs/BNF_GRAMMAR.md`                        |
| Implement an interpreter                   | `SwingInterpreter`: model → live Swing GUI   |
| Implement a code generator                 | `JavaCodeGenerator`: model → Java source     |
| Model key concepts of a problem domain     | GUI elements, layout, hierarchy, events      |
| Automatically generated software systems   | Code gen produces standalone executables      |

---

## Available Widgets

| DSL Method                                  | Widget     | Description                |
|---------------------------------------------|------------|----------------------------|
| `.label(text)`                              | Label      | Static text                |
| `.button(label, action)`                    | Button     | Clickable with callback    |
| `.textField(name, cols)`                    | TextField  | Single-line text input     |
| `.textArea(name, rows, cols)`               | TextArea   | Multi-line text input      |
| `.checkBox(name, label, selected)`          | CheckBox   | Boolean toggle             |
| `.comboBox(name, items...)`                 | ComboBox   | Dropdown selector          |
| `.slider(name, min, max, value, ticks)`     | Slider     | Range selector with ticks  |
| `.separator()`                              | Separator  | Horizontal divider         |
| `.panel(name, layout) ... .endPanel()`      | Panel      | Nested container           |

## Available Layouts

| Factory Method                    | Type       | Description                         |
|-----------------------------------|------------|-------------------------------------|
| `LayoutSpec.flow()`               | FLOW       | Left-to-right, wrapping             |
| `LayoutSpec.flow(hgap, vgap)`     | FLOW       | Flow with custom spacing            |
| `LayoutSpec.grid(rows, cols)`     | GRID       | Fixed rows × columns grid           |
| `LayoutSpec.border()`             | BORDER     | 5 regions (N, S, E, W, CENTER)      |
| `LayoutSpec.vertical()`           | VERTICAL   | Stacked top-to-bottom               |
| `LayoutSpec.horizontal()`         | HORIZONTAL | Side-by-side left-to-right          |
