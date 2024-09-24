package itstep.learning;

import java.nio.DoubleBuffer;
import java.util.Locale;

public class Crossover extends Vehicle {

    public double getClearance(){
        return clearance;
    }

    public void setClearance(double clearance){
        this.clearance = clearance;
    }

    private double clearance;

    public Crossover(String name, double clearance){

        super.setName(name);
        setClearance(clearance);
    }

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Crossover: %s, clearance: %f",
                super.getName(),
                this.getClearance()
        );
    }


}