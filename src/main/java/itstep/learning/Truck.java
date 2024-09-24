package itstep.learning;

import java.util.Locale;

public class Truck extends Vehicle implements LargeSized, Trailer {
    public double getCargo() {
        return cargo;
    }

    public Truck(String name, double cargo) {
        this.cargo = cargo;
        super.setName(name);
    }

    public void setCargo(double cargo) {
        this.cargo = cargo;
    }

    private double cargo;

    @Override
    public String trailerInfo() {
        return "";
    }

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Truck '%s' with cargo %.1f tone(s)",
                super.getName(),
                this.getCargo()
        );
    }
}
