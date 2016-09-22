package com.utils.GetSetGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: xiaoyx
 * Date: 2016-9-16
 * Time: 16:40
 * 测试用Pojo类
 */
public class Person {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private String name;
    private double height;
}
