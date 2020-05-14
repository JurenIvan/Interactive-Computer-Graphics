package hr.fer.zemris.irg.lsystems.commands;

import java.awt.Color;

import hr.fer.zemris.irg.lsystems.Command;
import hr.fer.zemris.irg.lsystems.Context;
import hr.fer.zemris.irg.lsystems.painter.Painter;
import hr.fer.zemris.irg.lsystems.TurtleState;

/**
 * Class used as a implementation of {@link Command} interface.
 * 
 * Has constructor that takes color as an argument and remembers it because it
 * is later used in {@link #execute(Context, Painter)} method.
 * 
 * @author juren
 *
 */
public class ColorCommand implements Command {
	/**
	 * Variable of type color
	 */
	private Color color;

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
		TurtleState curr = ctx.getCurrentState();
		curr.setColor(color);
	}

}
