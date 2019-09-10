package ict373assignment2;

import java.io.Serializable;

/*
Title: ICT373 Assignment #2
Author: Dexter Fong (33467253)
File Name: Address.java

Purpose: 
Address.java is a class that stores information about a FamilyMember's address, including his/her
street name, number, suburb and postal code. It implements the Serializable interface so that the FamilyTree
may be written to and read from a file.


It contains:
    - set and get methods that allow a client to change and retrieve values of private instance variables
    - a method to convert the Address object into a suitable string for display
    - a method to check if two given addresses are equal

Input: 
None. Input is gotten from client GUI and passed into Address as parameters.

Output:
None. Output is gotten from class and displayed in the client GUI.

Assumptions/Conditions:
- A street number and postal code may be of any length, but only contain digits and no letters or special characters.
- A street name and suburb may be of any length, but only contain letters and spaces, and no digits or special characters.
- All instance variable values may not be empty when setting values.
*/
public class Address implements Serializable{
    //Declaration of private instance variables
    private static final long serialVersionUID = 1L;
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postalCode;
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    CONSTRUCTORS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Default constructor for Address
    public Address() {
        streetNumber = "";
        streetName = "";
        suburb = "";
        postalCode = "";
    }

    //Overloaded public constructor for Address with all instance variables provided as parameters
    public Address(String streetNumber, String streetName, String suburb, String postalCode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.postalCode = postalCode;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/

    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    GET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Returns street number
    public String getStreetNumber() {
        return streetNumber;
    }
    
    //Returns street name
    public String getStreetName() {
        return streetName;
    }
    
    //Returns suburb name
    public String getSuburb() {
        return suburb;
    }
    
    //Returns postal code
    public String getPostalCode() {
        return postalCode;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/

    
    /////////////////////////////////////////////////////////////////////////////////////////    
    /*---------------------------------------------------------------------------------------
    SET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Sets street number
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        
    }

    //Sets street name
    public void setStreetName(String streetName) {
       this.streetName =  streetName;
    }

    //Sets suburb
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    //Sets postal code
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    QUERY/ANALYSIS/ACCESS METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Overriden toString method that prints out the full address string of the Address object
    @Override
    public String toString() {
        return streetNumber + " " + streetName + ", " + suburb + ", " + postalCode;
    }
    
    //Method that determines if a given address is equal to the address stored in the current object
    public boolean isAddressEquals(Address address) {	
    	return (address.getStreetName().equals(streetName) && address.getStreetNumber().equals(streetNumber)
    			&& address.getSuburb().equals(suburb) && address.postalCode.equals(postalCode));
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
}