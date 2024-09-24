package itstep.learning;

import java.util.ArrayList;
import java.util.List;

public class AutoShop {
    private List<Vehicle> vehicles;

    public AutoShop() {
        vehicles = new ArrayList<>();
        vehicles.add(new Bike("Kawasaki Ninja", "Sport"));
        vehicles.add(new Bike("Harley-Davidson Sportster", "Road"));
        vehicles.add(new Bus("Renault Master", 48));
        vehicles.add(new Bus("Mercedes-Benz Sprinter", 21));
        vehicles.add(new Bus("Bogdan A092", 24));
        vehicles.add(new Bus("Volvo 9700", 54));
        vehicles.add(new Truck("Renault C-Truck", 7.5));
        vehicles.add(new Truck("DAF XF 106 2018", 3.5));
        vehicles.add(new Truck("Mercedes Actros L", 15.0));
        vehicles.add(new Car("Honda Civic", "Sedan"));
        vehicles.add(new Car("Toyota Corolla", "Coupe"));
        vehicles.add(new Crossover("Toyota RAV4", 155));
        vehicles.add(new Crossover("Nissan Rogue", 210 ));
    }

    public void run() {
        printAll();
        System.out.println("-----------LARGE SIZED------------");
        printLargeSized();
        System.out.println("-----------NON-LARGE SIZED------------");
        printNonLargeSized();
        System.out.println("-----------TRAILER-ABLE------------");
        printTrailers();
    }

    public void printAll() {
        for (Vehicle v : vehicles) {
            System.out.println(v.getInfo());
        }
    }

    public void printLargeSized() {
        for (Vehicle v : vehicles) {
            if (v instanceof LargeSized) System.out.println(v.getInfo());
        }
    }

    public void printNonLargeSized() {
        for (Vehicle v : vehicles) {
            if (!(v instanceof LargeSized)) System.out.println(v.getInfo());
        }
    }

    private void printTrailers() {
        for (Vehicle v : vehicles) {
            if (v instanceof Trailer) {
                System.out.print(v.getInfo());
                System.out.println(" could have a trailer of type " + ((Trailer) v).trailerInfo());
            }
        }
    }
}
