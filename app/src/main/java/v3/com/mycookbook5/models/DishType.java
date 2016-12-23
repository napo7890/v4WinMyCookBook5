package v3.com.mycookbook5.models;

public class DishType {

    private String dishType;

    public DishType(String dishType){
        this.dishType = dishType;
    }

    public String getDishType(){return dishType;}

    @Override
    public String toString() {
        return dishType;
    }
}
