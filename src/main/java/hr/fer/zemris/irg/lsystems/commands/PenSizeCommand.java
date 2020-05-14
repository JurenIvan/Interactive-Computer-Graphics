package hr.fer.zemris.irg.lsystems.commands;

import hr.fer.zemris.irg.lsystems.Command;
import hr.fer.zemris.irg.lsystems.Context;
import hr.fer.zemris.irg.lsystems.painter.Painter;

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
