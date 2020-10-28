package com.atguigu.bean;

/**
 * @author chenhuiup
 * @create 2020-10-25 14:01
 */
public class Student {
    private String class_id;
    private String name;
    private String gender;
    private int age;
    private String favo1;
    private String favo2;

    public Student() {
    }

    public Student(String class_id, String name, String gender, int age, String favo1, String favo2) {
        this.class_id = class_id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.favo1 = favo1;
        this.favo2 = favo2;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFavo1() {
        return favo1;
    }

    public void setFavo1(String favo1) {
        this.favo1 = favo1;
    }

    public String getFavo2() {
        return favo2;
    }

    public void setFavo2(String favo2) {
        this.favo2 = favo2;
    }
}
