package com.apps.amit.lawofattraction.utils;

/**
 * Created by amit on 9/2/18.
 */


public class ManifestationTrackerUtils {

  //private variables
  int id;
  String name;
  String phoneNumber;

  // Empty constructor
  public ManifestationTrackerUtils(){

  }
  // constructor
  public ManifestationTrackerUtils(int id, String name, String phoneNumber){
    this.id = id;
    this.name = name;
    this.phoneNumber = phoneNumber;
  }

  // constructor
  public ManifestationTrackerUtils(String name, String phoneNumber){
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
  // getting ID
  public int getID(){
    return this.id;
  }

  // setting id
  public void setID(int id){
    this.id = id;
  }

  // getting name
  public String getName(){
    return this.name;
  }

  // setting name
  public void setName(String name){
    this.name = name;
  }

  // getting phone number
  public String getPhoneNumber(){
    return this.phoneNumber;
  }

  // setting phone number
  public void setPhoneNumber(String phoneNumber){
    this.phoneNumber = phoneNumber;
  }
}