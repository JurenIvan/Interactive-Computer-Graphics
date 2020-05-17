package hr.fer.zemris.irg.lsystems.commands;

import hr.fer.zemris.irg.lsystems.Command;
import hr.fer.zemris.irg.lsystems.Context;
import hr.fer.zemris.irg.lsystems.painter.Painter;

import java.awt.*;

/**
 * Class used as a implementation of {@link Command} interface.
 * <p>
 * Has constructor that takes color as an argument and remembers it because it
 * is later used in {@link #execute(Context, Painter)} method.
 *
 * @author juren
 */
public class ColorCommand implements Command {
    /**
     * Variable of type color
     */
    private final Color color;

    /**
     * Constructor for {@link ColorCommand} that takes color as a argument and saves
     * it.
     *
     * @param color that is saved
     */
    public ColorCommand(Color color) {
        this.color = color;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setColor(color);
    }

}
