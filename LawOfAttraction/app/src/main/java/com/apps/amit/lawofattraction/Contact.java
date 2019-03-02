package com.apps.amit.lawofattraction;

/**
 * Created by amit on 9/2/18.
 */


public class Contact {

  //private variables
  int _id;
  String _name;
  double _phone_number;

  // Empty constructor
  public Contact(){

  }
  // constructor
  public Contact(int id, String name, double _phone_number){
    this._id = id;
    this._name = name;
    this._phone_number = _phone_number;
  }

  // constructor
  public Contact(String name, double _phone_number){
    this._name = name;
    this._phone_number = _phone_number;
  }
  // getting ID
  public int getID(){
    return this._id;
  }

  // setting id
  public void setID(int id){
    this._id = id;
  }

  // getting name
  public String getName(){
    return this._name;
  }

  // setting name
  public void setName(String name){
    this._name = name;
  }

  // getting phone number
  public double getPhoneNumber(){
    return this._phone_number;
  }

  // setting phone number
  public void setPhoneNumber(double phone_number){
    this._phone_number = phone_number;
  }
}