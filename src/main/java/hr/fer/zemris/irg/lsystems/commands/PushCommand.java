package hr.fer.zemris.irg.lsystems.commands;

import hr.fer.zemris.irg.lsystems.Command;
import hr.fer.zemris.irg.lsystems.Context;
import hr.fer.zemris.irg.lsystems.painter.Painter;

/**
 * Class used as a implementation of {@link Command} interface.
 * <p>
 * Has {@link #execute(Context, Painter)} method which pushes the current state
 * out of provided {@link Context} onto it.
 *
 * @author juren
 */
public class PushCommand implements Command {

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.pushState(ctx.getCurrentState().copy());
    }

}
