package Models;

import Interfaces.IBase;

/*
 * Basis Klasse, welche das Interface IBase implementiert
 */
public abstract class Base implements IBase
{
    public int id;
    public String name;
    
    public Base(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}