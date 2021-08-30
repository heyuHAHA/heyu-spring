package service;

public class WorldServiceImpl implements WorldService{

    private String name;

    @Override
    public void explode() {
        System.out.println("The Earth is going to expload");
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }
}
