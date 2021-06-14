package net.kappabyte.kappaengine.graphics;

import java.nio.charset.Charset;

public class Font {
    private final Texture texture;
    private final int columns, rows;

    private final Charset charset;

    public Font(String fileName, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.texture = new Texture(fileName);
        charset = Charset.forName("ISO-8859-1");
    }

    public Font(String fileName, int rows, int columns, Charset charset) {
        this.rows = rows;
        this.columns = columns;
        this.texture = new Texture(fileName);
        this.charset = charset;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Charset getCharset() {
        return charset;
    }
}
