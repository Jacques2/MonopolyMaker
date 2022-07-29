package com.jacques.monopoly;

public class UserData {
    public String username = "";
    public boolean computerPlayer = false;
    public String sprite = "Battleship";

    public UserData(String name){
        username = name;
    }

    public UserData(){
        username = "New Player";
    }
}
