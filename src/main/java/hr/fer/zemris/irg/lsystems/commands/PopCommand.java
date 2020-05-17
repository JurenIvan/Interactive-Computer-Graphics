package hr.fer.zemris.irg.lsystems.commands;

import hr.fer.zemris.irg.lsystems.Command;
import hr.fer.zemris.irg.lsystems.Context;
import hr.fer.zemris.irg.lsystems.painter.Painter;

/**
 * Class used as a implementation of {@link Command} interface.
 * <p>
 * Has {@link #execute(Context, Painter)} method which pops the current state
 * out of provided {@link Context}
 *
 * @author juren
 */
public class PopCommand implements Command {

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.popState();
    }

}
