package hr.fer.zemris.irg.lsystems;

import hr.fer.zemris.irg.lsystems.commands.*;
import hr.fer.zemris.irg.lsystems.painter.Painter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing implementation of {@link LSystemBuilder} interface. Having
 * two dictionaries, it is able to remember productions and commands and
 * generate appropriate L-system
 *
 * @author juren
 */
public class LSystemBuilderImpl implements LSystemBuilder {


    private static final double DEFAULT_UNIT_LENGTH = 0.1;
    private static final double DEFAULT_UNIT_LENGTH_DEGREE_SCALER = 1;
    private static final double DEFAULT_ANGLE = 0;
    private static final Vector2D DEFAULT_ORIGIN = new Vector2D(0, 0);
    private static final String DEFAULT_AXIOM = "";
    private double angle;
    private String axiom;
    private double unitLengthDegreeScaler;
    private double unitLength;
    private Vector2D origin;

    private final Map<Character, String> registeredProductions;
    private final Map<Character, Command> registeredCommands;

    public LSystemBuilderImpl() {
        this.unitLength = DEFAULT_UNIT_LENGTH;
        this.unitLengthDegreeScaler = DEFAULT_UNIT_LENGTH_DEGREE_SCALER;
        this.origin = DEFAULT_ORIGIN;
        this.angle = DEFAULT_ANGLE;
        this.axiom = DEFAULT_AXIOM;
        registeredCommands = new HashMap<>();
        registeredProductions = new HashMap<>();
    }

    @Override
    public LSystem build() {
        return new LSystemImpl();
    }

    @Override
    public LSystemBuilder configureFromText(String[] data) {
        for (String line : data) {
            if (line.isBlank())
                continue;
            String[] splitted = line.split("\\s+");
            try {
                switch (splitted[0]) {
                    case "origin":
                        setOrigin(Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]));
                        break;
                    case "angle":
                        setAngle(Double.parseDouble(splitted[1]));
                        break;
                    case "unitLength":
                        setUnitLength(Double.parseDouble(splitted[1]));
                        break;
                    case "unitLengthDegreeScaler":
                        setUnitLengthDegreeScaler(getUnitLengthDegreeScalerFromLine(line));
                        break;
                    case "command":
                        getCommandPart(line);
                        break;
                    case "axiom":
                        setAxiom(splitted[1]);
                        break;
                    case "production":
                        registerProduction(splitted[1].charAt(0), splitted[2]);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Cant interpretate data.");
            }
        }

        return this;
    }

    private LSystemBuilder getCommandPart(String line) {
        char arg0 = line.split("\\s+")[1].charAt(0);
        StringBuilder arg1 = new StringBuilder();
        String[] splitted = line.split("\\s+");
        for (int i = 2; i < splitted.length; i++) {
            arg1.append(splitted[i]).append(" ");
        }
        return registerCommand(arg0, arg1.toString());
    }

    private double getUnitLengthDegreeScalerFromLine(String line) {
        String[] splitted = line.split("\\s+");
        if (splitted.length == 2) {
            return Double.parseDouble(splitted[1]);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < splitted.length; i++) {
            sb.append(splitted[i]);
        }
        String secondPart = sb.toString().replaceAll("\\s+", "");
        String[] numbers = secondPart.split("/");
        if (Double.parseDouble(numbers[1]) == 0)
            throw new IllegalArgumentException("Division by zero doesn't make sence in this usecase.");
        return Double.parseDouble(numbers[0]) / Double.parseDouble(numbers[1]);
    }

    @Override
    public LSystemBuilder registerCommand(char arg0, String arg1) {
        String[] splitted = arg1.split("\\s+");
        Command arg1Command;
        try {
            switch (splitted[0]) {
                case "pop":
                    arg1Command = new PopCommand();
                    break;
                case "push":
                    arg1Command = new PushCommand();
                    break;
                case "draw":
                    arg1Command = new DrawCommand(Double.parseDouble(splitted[1]));
                    break;
                case "rotate":
                    arg1Command = new RotateCommand(Double.parseDouble(splitted[1]));
                    break;
                case "skip":
                    arg1Command = new SkipCommand(Double.parseDouble(splitted[1]));
                    break;
                case "scale":
                    arg1Command = new ScaleCommand(Double.parseDouble(splitted[1]));
                    break;
                case "color":
                    arg1Command = new ColorCommand(Color.decode("#" + splitted[1]));
                    break;
                case "pensize":
                    arg1Command = new PenSizeCommand(Double.parseDouble(splitted[1]));
                    break;
                default:
                    throw new IllegalArgumentException("Unsuported command!");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Can't intepret numbers");
        } catch (IllegalArgumentException e) {
            throw new NumberFormatException("Unsuported command");
        }
        registeredCommands.put(arg0, arg1Command);
        return this;
    }

    @Override
    public LSystemBuilder registerProduction(char arg0, String arg1) {
        registeredProductions.put(arg0, arg1);
        return this;
    }

    @Override
    public LSystemBuilder setAngle(double arg0) {
        this.angle = arg0;
        return this;
    }

    @Override
    public LSystemBuilder setAxiom(String arg0) {
        this.axiom = arg0;
        return this;
    }

    @Override
    public LSystemBuilder setOrigin(double arg0, double arg1) {
        this.origin = new Vector2D(arg0, arg1);
        return this;
    }

    @Override
    public LSystemBuilder setUnitLength(double arg0) {
        this.unitLength = arg0;
        return this;
    }

    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double arg0) {
        this.unitLengthDegreeScaler = arg0;
        return this;
    }


    private class LSystemImpl implements LSystem {

        private final List<String> memoisation;

        public LSystemImpl() {
            memoisation = new ArrayList<>();
            memoisation.add(axiom);
        }

        @Override
        public void draw(int depth, Painter painter) {
            Context context = new Context();
            context.pushState(new TurtleState(origin.copy(), (new Vector2D(1, 0)).rotated(Math.toRadians(angle)),
                    Color.black, unitLength * (Math.pow(unitLengthDegreeScaler, depth)), 0.7));

            String whatToDo = generate(depth);
            for (int i = 0; i < whatToDo.length(); i++) {
                Command command = registeredCommands.get(whatToDo.charAt(i));
                if (command == null)
                    continue;
                command.execute(context, painter);
            }
        }

        @Override
        public String generate(int arg0) {
            if (memoisation.size() > arg0)
                return memoisation.get(arg0);
            String previousAxiom = generate(arg0 - 1);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < previousAxiom.length(); i++) {
                String production = registeredProductions.get(previousAxiom.charAt(i));
                if (production == null) {
                    sb.append(previousAxiom.charAt(i));
                    continue;
                }
                sb.append(production);
            }
            String production = sb.toString();
            memoisation.add(arg0, production);
            return production;

        }
    }

}
