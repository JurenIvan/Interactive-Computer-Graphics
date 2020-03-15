package hr.fer.zemris.irg.color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hr.fer.zemris.irg.color.Color.values;

public class ColorSupplier {

    private List<Color> colorList;
    private int currentColorIndex;

    public ColorSupplier() {
        this.colorList = Arrays.stream(values()).collect(Collectors.toList());
        this.currentColorIndex = 0;
    }

    public Color setNext() {
        return this.set(1);
    }

    public Color setPrevious() {
        return this.set(-1);
    }

    private Color set(int step) {
        currentColorIndex = (currentColorIndex + colorList.size() + step) % colorList.size();
        return colorList.get(currentColorIndex);
    }

    public Color set(Color color) {
        int index = colorList.indexOf(color);
        currentColorIndex = index != -1 ? index : currentColorIndex;
        return color;
    }

    public Color get() {
        return colorList.get(currentColorIndex);
    }
}
