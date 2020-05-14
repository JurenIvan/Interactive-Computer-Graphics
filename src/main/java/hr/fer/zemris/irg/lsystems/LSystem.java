package hr.fer.zemris.irg.lsystems;

import hr.fer.zemris.irg.lsystems.painter.Painter;

public interface LSystem {

    void draw(int arg0, Painter arg1);

    String generate(int arg0);
}
