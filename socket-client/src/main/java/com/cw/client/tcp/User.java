package com.cw.client.tcp;

import java.io.Serializable;

public class User implements Serializable {
    static final long serialVersionUID = 42L;
    public String name;
    User( String name){
        this.name = name;
    }
}
