package com.example.knowaround;

public class Auth {

    private int numEntered;
    private static Auth instance;

    private Auth(){
        this.numEntered=12;
    }

    public static Auth getInstance() {
        if (instance==null){
            instance=new Auth();
        }
        return instance;
    }

    public void doubleIt (){
        numEntered= 2*numEntered;
    }

    public void subtract1 (){
        numEntered--;
    }

    public int getNum (){
        return numEntered;
    }


}
