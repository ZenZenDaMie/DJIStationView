package com.example.rsj.DJIStationView;

/**
 * Created by rsj on 2018/1/15.
 */

public class Person {

    private int id;
    private String name;
    private String address;
    private String email;
    private double money;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return  "编号："+id+"  名字:"+name+"   地址:"+address+"   E-mail:"+email+"  钱:"+money;
    }
    public Person() {
        // TODO Auto-generated constructor stub
    }
    public Person(int id, String name, String address, String email,
                  double money) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.money = money;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }


}
