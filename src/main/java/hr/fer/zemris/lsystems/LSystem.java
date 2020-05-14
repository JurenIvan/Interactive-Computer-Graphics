package hr.fer.zemris.lsystems;

import hr.fer.zemris.lsystems.painter.Painter;

public interface LSystem {

    void draw(int arg0, Painter arg1);

    String generate(int arg0);
}
