package hr.fer.zemris.lsystems;

import java.util.Stack;

/**
 * Class that is adapter to objectstack and provides bundle of methods that
 * allows us to get context of a TurtleState
 *
 * @author juren
 */
public class Context {

    /**
     * Variable of type ObjectStack that enables us to have memory
     */
    private Stack<TurtleState> stack= new Stack<>();

    /**
     * Method that returns peek of stack.
     *
     * @return state that was on top of stack
     */
    public TurtleState getCurrentState() {
        return stack.peek();
    }

    /**
     * Method that saves state by putting it on stack
     *
     * @param state
     */
    public void pushState(TurtleState state) {
        stack.push(state);
    }

    /**
     * Method that removes(pop) top of stack.
     */
    public void popState() {
        stack.pop();
    }
}
