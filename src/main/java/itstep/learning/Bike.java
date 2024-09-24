package itstep.learning;

import java.util.Locale;

public class Bike extends Vehicle implements Trailer {
    String type;

    public Bike(String name, String type) {
        this.setType(type);
        super.setName(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {

    }

    @Override
    public String getInfo() {
        return String.format(Locale.ROOT,
                "Bike name: '%s', type '%s'",
                this.getName(),
                this.getType());
    }

    @Override
    public String trailerInfo() {
        return "Cradle trailer";
    }
}
