# QuickGUI — BNF Grammar Specification

This document defines the **abstract syntax** of the QuickGUI domain-specific
language using **Backus-Naur Form (BNF)**.  Because QuickGUI is an *internal* DSL
embedded in Java, the concrete syntax is Java method chains.  The BNF below
describes the **conceptual grammar** — the structure that the DSL enforces via
its fluent API types.

---

## Grammar

```bnf
<gui-program>    ::= "GUI.window(" <string> ")" <window-config>* <terminal>

<window-config>  ::= <size-clause>
                   | <layout-clause>
                   | <child-element>

<size-clause>    ::= ".size(" <int> "," <int> ")"

<layout-clause>  ::= ".layout(" <layout-spec> ")"

<layout-spec>    ::= "LayoutSpec.flow()"
                   | "LayoutSpec.flow(" <int> "," <int> ")"
                   | "LayoutSpec.grid(" <int> "," <int> ")"
                   | "LayoutSpec.grid(" <int> "," <int> "," <int> "," <int> ")"
                   | "LayoutSpec.border()"
                   | "LayoutSpec.border(" <int> "," <int> ")"
                   | "LayoutSpec.vertical()"
                   | "LayoutSpec.horizontal()"

<child-element>  ::= <label>
                   | <button>
                   | <text-field>
                   | <text-area>
                   | <check-box>
                   | <combo-box>
                   | <slider>
                   | <separator>
                   | <panel>

<label>          ::= ".label(" <string> ")"
                   | ".label(" <string> "," <string> ")"
                   | ".label(" <string> "," <string> "," <position> ")"

<button>         ::= ".button(" <string> ")"
                   | ".button(" <string> "," <action> ")"
                   | ".button(" <string> "," <string> "," <action> ")"
                   | ".button(" <string> "," <string> "," <action> "," <position> ")"

<text-field>     ::= ".textField(" <string> ")"
                   | ".textField(" <string> "," <int> ")"
                   | ".textField(" <string> "," <int> "," <string> ")"
                   | ".textField(" <string> "," <int> "," <string> "," <position> ")"

<text-area>      ::= ".textArea(" <string> ")"
                   | ".textArea(" <string> "," <int> "," <int> ")"
                   | ".textArea(" <string> "," <int> "," <int> "," <position> ")"

<check-box>      ::= ".checkBox(" <string> "," <string> ")"
                   | ".checkBox(" <string> "," <string> "," <boolean> ")"

<combo-box>      ::= ".comboBox(" <string> "," <string>+ ")"

<slider>         ::= ".slider(" <string> "," <int> "," <int> "," <int> ")"
                   | ".slider(" <string> "," <int> "," <int> "," <int> "," <int> ")"

<separator>      ::= ".separator()"
                   | ".separator(" <orientation> ")"

<panel>          ::= ".panel(" <string> ")" <panel-config>* ".endPanel()"
                   | ".panel(" <string> "," <layout-spec> ")" <panel-config>* ".endPanel()"

<panel-config>   ::= <layout-clause>
                   | <titled-clause>
                   | <at-clause>
                   | <child-element>

<titled-clause>  ::= ".titled(" <string> ")"
<at-clause>      ::= ".at(" <position> ")"

<position>       ::= "Position.NORTH"
                   | "Position.SOUTH"
                   | "Position.EAST"
                   | "Position.WEST"
                   | "Position.CENTER"

<orientation>    ::= "Separator.Orientation.HORIZONTAL"
                   | "Separator.Orientation.VERTICAL"

<terminal>       ::= ".show()"
                   | ".build()"
                   | ".generateCode(" <string> ")"

<action>         ::= <lambda-expression>    (* Java Runnable lambda *)
<string>         ::= (* Java string literal *)
<int>            ::= (* Java integer literal *)
<boolean>        ::= "true" | "false"
```

---

## Explanation of Grammar Structure

| Non-terminal      | Meta-model concept   | Description                              |
|-------------------|----------------------|------------------------------------------|
| `<gui-program>`   | **Window**           | Top-level window, the model root         |
| `<panel>`         | **Panel**            | Nested container with its own layout     |
| `<child-element>` | **GUIElement**       | Any widget or sub-panel                  |
| `<layout-spec>`   | **LayoutSpec**       | Layout strategy (FLOW, GRID, BORDER, …)  |
| `<position>`      | **Position**         | Constraint for BorderLayout placement    |
| `<label>`         | **Label**            | Static text display widget               |
| `<button>`        | **Button**           | Clickable widget with action callback    |
| `<text-field>`    | **TextField**        | Single-line text input widget            |
| `<text-area>`     | **TextArea**         | Multi-line text input widget             |
| `<check-box>`     | **CheckBox**         | Boolean toggle widget                    |
| `<combo-box>`     | **ComboBox**         | Dropdown selection widget                |
| `<slider>`        | **Slider**           | Range selection widget                   |
| `<separator>`     | **Separator**        | Visual divider line                      |
| `<terminal>`      | —                    | Triggers interpretation or code gen      |

## Key Observations

1. **Hierarchical structure**: The grammar is inherently recursive — a `<panel>` 
   can contain `<child-element>` which includes `<panel>`, enabling arbitrary nesting.

2. **Type safety**: In the internal DSL, the Java type system enforces the grammar —
   `.endPanel()` returns to the parent builder type, so you cannot call `.show()`
   inside a panel or `.endPanel()` outside one.

3. **Fluent chaining**: Every configuration method returns `SELF` (the builder type),
   enabling the natural left-to-right reading order typical of internal DSLs.

4. **Terminal operations**: The grammar distinguishes between configuration methods
   (which return a builder) and terminal methods (`.show()`, `.build()`, `.generateCode()`)
   that finalize the model and trigger semantics.
