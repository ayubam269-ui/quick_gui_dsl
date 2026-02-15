package quickgui.model;

/**
 * Meta-model: Layout specification for a Panel.
 *
 * Captures layout type and optional parameters (rows, cols for GRID;
 * hgap/vgap for FLOW and GRID).
 */
public class LayoutSpec {
    private final LayoutType type;
    private int rows;
    private int cols;
    private int hgap;
    private int vgap;

    private LayoutSpec(LayoutType type) {
        this.type = type;
    }

    /* ---------- factory methods (used by DSL) ---------- */

    public static LayoutSpec flow() {
        return new LayoutSpec(LayoutType.FLOW);
    }

    public static LayoutSpec flow(int hgap, int vgap) {
        LayoutSpec ls = new LayoutSpec(LayoutType.FLOW);
        ls.hgap = hgap;
        ls.vgap = vgap;
        return ls;
    }

    public static LayoutSpec grid(int rows, int cols) {
        LayoutSpec ls = new LayoutSpec(LayoutType.GRID);
        ls.rows = rows;
        ls.cols = cols;
        return ls;
    }

    public static LayoutSpec grid(int rows, int cols, int hgap, int vgap) {
        LayoutSpec ls = new LayoutSpec(LayoutType.GRID);
        ls.rows = rows;
        ls.cols = cols;
        ls.hgap = hgap;
        ls.vgap = vgap;
        return ls;
    }

    public static LayoutSpec border() {
        return new LayoutSpec(LayoutType.BORDER);
    }

    public static LayoutSpec border(int hgap, int vgap) {
        LayoutSpec ls = new LayoutSpec(LayoutType.BORDER);
        ls.hgap = hgap;
        ls.vgap = vgap;
        return ls;
    }

    public static LayoutSpec vertical() {
        return new LayoutSpec(LayoutType.VERTICAL);
    }

    public static LayoutSpec horizontal() {
        return new LayoutSpec(LayoutType.HORIZONTAL);
    }

    /* ---------- getters ---------- */

    public LayoutType getType() { return type; }
    public int getRows()  { return rows; }
    public int getCols()  { return cols; }
    public int getHgap()  { return hgap; }
    public int getVgap()  { return vgap; }
}
