package hr.fer.zemris.lsystems.commands;

import hr.fer.zemris.lsystems.Command;
import hr.fer.zemris.lsystems.Context;
import hr.fer.zemris.lsystems.painter.Painter;

public class PenSizeCommand implements Command {

    private double penSize;

    public PenSizeCommand(double penSize) {
        this.penSize = penSize;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setPenSize(ctx.getCurrentState().getPenSize() * penSize);
    }
}
