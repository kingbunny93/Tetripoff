package TetripoffPack;

import java.util.Random;

public class Shape {
    protected enum Tetrominoe { NoShape, RSShape, SShape, LineShape,
        TShape, SquareShape, LShape, RLShape }

    private Tetrominoe pieceShape;
    private int coords[][];
    private int[][][] coordsTable;
    public Shape() {
        initShape();
    }
    private void initShape() {
        coords = new int[4][2];
        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },   // no block
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },  // RS block
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },   // S Block
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },   // Straight block
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },   // T block
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },   // Square block
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },   // L block
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }    // RL block
        };
        setShape(Tetrominoe.NoShape);
    }
    protected void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public Tetrominoe getShape()  { return pieceShape; }

    public void setRandomShape() {
        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoe[] values = Tetrominoe.values();
        setShape(values[x]);
    }
    public int minX() {
        int m = coords[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }
    public int minY() {
        int m = coords[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    /** Method to rotate the piece counter clock-wise */
    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        var result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
    /** Method to rotate the piece clockwise */
    public Shape rotateRight() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        var result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}

