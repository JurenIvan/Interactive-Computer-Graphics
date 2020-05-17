package hr.fer.zemris.irg.lsystems;

public interface LSystemBuilder {

    LSystemBuilder registerCommand(char arg0, String arg1);

    LSystem build();

    LSystemBuilder configureFromText(String[] data);

    LSystemBuilder registerProduction(char arg0, String arg1);


    LSystemBuilder setAngle(double arg0);


    LSystemBuilder setAxiom(String arg0);


    LSystemBuilder setOrigin(double arg0, double arg1);


    LSystemBuilder setUnitLength(double arg0);


    LSystemBuilder setUnitLengthDegreeScaler(double arg0);
}
