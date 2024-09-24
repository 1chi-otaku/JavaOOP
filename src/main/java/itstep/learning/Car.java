package itstep.learning;

import java.util.Locale;

public class Car extends Vehicle {

    public String getCarType(){
        return carType;
    }

    public void setCarType(String carType){
        this.carType = carType;
    }

    private String carType;

    public Car(String name, String carType){

        super.setName(name);
        setCarType(carType);
    }

    @Override
    public String getInfo() {
        return String.format(
                Locale.ROOT,
                "Car: %s, Car type: %s",
                super.getName(),
                this.getCarType()
        );
    }


}
