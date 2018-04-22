package model;

/**
 *
 * @author Símon Örn Reynisson <sor7@hi.is>
 */
public class Airline {
    private String name;
    private int priority;
    
    public Airline(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
