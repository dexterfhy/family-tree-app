package ict373assignment2;

import java.io.Serializable;
import java.util.ArrayList;

/*
Title: ICT373 Assignment #2
Author: Dexter Fong (33467253)
File Name: FamilyMember.java

Purpose: 
FamilyMember.java is a class that stores information about a FamilyMember, including his/her
first/last/maiden name, gender, address, life description and relatives. It implements the 
Serializable interface so that the FamilyTree may be written to and read from a file. Furthermore,
it contains an instance of the Address object to store address values and uses an ArrayList to store
information about the FamilyMember's children.

It contains:
    - set and get methods that allow a client to change and retrieve values of private instance variables
    - a method to convert the FamilyMember object into a suitable string for display
    - a method to check if the FamilyMember object has a specific relative, or the number of children
    - a method to add additional relatives to the FamilyMember object
    - static methods that validate the input provided for values of instance variables

Input: 
None. Input is gotten from client GUI and passed into FamilyMember as parameters.

Output:
None. Output is gotten from class and displayed in the client GUI.

Assumptions/Conditions:
- All instance variable values may not be empty when setting values.
- A family member may have as many children as possible.
- A family member's parents may not be set again once values have been assigned to it.
- A family member is deemed to be equal to another family member if they have the same names, gender and address.
*/

public class FamilyMember implements Serializable{
    //Constant integer values used to determine if the FamilyMember object has a given Relation, Attribute or test for its gender
    public static final int Relation_FATHER = 1, Relation_MOTHER = 2, Relation_CHILDREN = 3, Relation_SPOUSE = 4, Relation_PARENTS = 5,
                            Attribute_MAIDENNAME = 6, Gender_MALE = 0, Gender_FEMALE = 1;

    //Declaration of private instance varibles
    private static final long serialVersionUID = 1L;
    private String firstName, lastName, maidenName, lifeDescription;
    private int gender;
    private Address address;
    private FamilyMember mother, father, spouse;
    private ArrayList<FamilyMember> children;
    //ArrayList is used to efficiently store, insert and delete FamilyMember objects in the collection
    

    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    CONSTRUCTORS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Default constructor for FamilyMember
    public FamilyMember() {
        firstName = "";
        lastName = "";
        maidenName = "";
        gender = -1;
        address = null;
        lifeDescription = "";
        
        mother = null;
        father = null;
        spouse = null;
        children = new ArrayList<>();
    }

    //Overloaded public constructor for FamilyMember that accepts values for all private instance String variables (except maiden name)
    public FamilyMember(String firstName, String lastName, String gender, Address address, String lifeDescription) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender.equals("MALE") ? Gender_MALE : Gender_FEMALE;
        this.address = address;
        this.lifeDescription = lifeDescription;
        
        mother = null;
        father = null;
        spouse = null;
        children = new ArrayList<>();
    }    
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
 

    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    GET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Returns first name
    public String getFirstName() {
        return firstName;
    }

    //Returns last name
    public String getLastName() {
        return lastName;
    }

    //Returns maiden name
    public String getMaidenName() {
        return maidenName;
    }

    //Returns gender
    public int getGender() {
        return gender;
    }

    //Returns life description
    public String getLifeDescription() {
        return lifeDescription;
    }
    
    //Returns address object of Family Member
    public Address getAddress() {
        return address;
    }
    
    //Returns FamilyMember object corresponding to mother of current object
    public FamilyMember getMother() {
        return mother;
    }

    //Returns FamilyMember object corresponding to father of current object    
    public FamilyMember getFather() {
        return father;
    }

    //Returns FamilyMember object corresponding to spouse of current object
    public FamilyMember getSpouse() {
        return spouse;
    }

    //Returns ArrayList collection of FamilyMember objects corresponding to children of current object
    public ArrayList<FamilyMember> getChildren() {
        return children;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
    
    /////////////////////////////////////////////////////////////////////////////////////////    
    /*---------------------------------------------------------------------------------------
    SET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Sets first name
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

    //Sets last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //Sets maiden name
    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    //Sets gender
    public void setGender(String gender) {
        this.gender = gender.equals("MALE") ? Gender_MALE : Gender_FEMALE;
    }

    //Sets life description
    public void setLifeDescription(String lifeDescription) {
        this.lifeDescription = lifeDescription;
    }
    
    //Sets Address instance variable of FamilyMember object with a given Address object parameter
    public void setAddress(Address address) {
        this.address = address;
    }

    //Sets the mother Family Member variable of the current object
    //Performs validation to ensure that the accepted parameter is female, and that the current object
    //does not already have a mother
    private void setMother(FamilyMember mother) {
        //ensure that mother parameter doesn't already have the current object as a child
        if (!mother.childExists(this)){
            mother.getChildren().add(this);
        }
        this.mother = mother;               
    }

    //Resets the mother variable of the current object to null 
    public void setMotherNull() {
    	this.mother = null;
    }
    
    //Sets the father Family Member variable of the current object
    //Performs validation to ensure that the accepted parameter is male, and that the current object
    //does not already have a father
    private void setFather(FamilyMember father) {
        //ensure that mother parameter doesn't already have the current object as a child
        if (!father.childExists(this)){
            father.getChildren().add(this);
        }
        this.father = father;   
    }
    
    //Resets the mother variable of the current object to null
    public void setFatherNull() {
    	this.father = null;
    }
    
    //Sets the spouse Family Member variable of the current object
    //Performs validation to ensure that the current object already have a spouse,
    //and that the spouse is of the opposite gender
    private void setSpouse(FamilyMember spouse) {
        this.spouse = spouse;
    }

    //Resets the spouse variable of the current object
    public void setSpouseNull() {
    	this.spouse = null;
    }

    //Sets the children ArrayList to a given ArrayList of FamilyMember objects
    public void setChildren(ArrayList<FamilyMember> children) {
        this.children = children;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    

    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    QUERY/ANALYSIS/ACCESS METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Overriden toString method that prints out the full gender and name of the FamilyMember object
    @Override
    public String toString() {
        String name = null;
        //Attaches the mars/venus gender symbol based on the gender of the FamilyMember
        if (this.gender == Gender_MALE){
            name = "♂ ";
        }else if (this.gender == Gender_FEMALE){
            name = "♀ ";
        }
        
        name += this.getFirstName() + " " + this.getLastName(); 
        //Attaches Maiden name in brackets if individual is female
        if (this.has(Attribute_MAIDENNAME)){ 
            name += " (" + this.getMaidenName() + ")";
        }
        return name;
    }

    //Returns whether the current FamilyMember object has a given relation or attribute
    public boolean has(int type){
        switch(type){            
            case Relation_FATHER:
                return father != null;
            case Relation_MOTHER:
                return mother != null;
            case Relation_CHILDREN:
                return !children.isEmpty();
            case Relation_SPOUSE:
                return spouse != null;
            case Relation_PARENTS:
                return (father != null || mother != null);
            case Attribute_MAIDENNAME:
                return maidenName.length() != 0;
        }
        return false;
    }
    
    //Adds a child to the current object, and ensure that the child has its own relational
    //attribute values properly set
    private void addChild(FamilyMember child) {
        children.add(child);        

        //If the current object is a father
        if (this.gender == Gender_MALE) {
            //Set the new father of the child object to current object
            child.setFather(this);
            
            //Check if current object has a spouse, and set the mother relation of the child 
            //to the current object's spouse
            if (this.has(Relation_SPOUSE)) {
                child.setMother(spouse);
            }
        //If the current object is a mother
        } else {
            //Set the new mother of the child object to current object
            child.setMother(this);
            
            //Check if current object has a spouse, and set the father relation of the child 
            //to the current object's spouse
            if (this.has(Relation_SPOUSE)) {
                child.setFather(spouse);
            }
        }
    }
    
    //Method that accepts a FamilyMember object and a String corresponding to the relative type 
    //of the new member to the selected member
    public void addRelative(String type, FamilyMember member){
        //Switch case calls the appropriate set method based on the relative type string value
    	switch(type){
            case "MOTHER":
                setMother(member);
                //Ensures (or rather assumes) that a mother that is set for an individual with an
                //existing father is the spouse of the latter, and vice versa
                if (has(Relation_FATHER)){
                    member.setSpouse(father);
                    father.setSpouse(member);
                }
                break;
            case "FATHER":
                setFather(member);
                //Ensures (or rather assumes) that a father that is set for an individual with an
                //existing motherher is the spouse of the latter, and vice versa
                if (has(Relation_MOTHER)){
                    member.setSpouse(mother);
                    mother.setSpouse(member);
                }
                break;
            case "CHILD":
                addChild(member);
                break;
            case "SPOUSE":
                setSpouse(member);
                member.setSpouse(this);
        }
    }

    //Checks the ArrayList of FamilyMember children to see if it contains a given parameter
    //Uses the isEquals() method to determine if two FamilyMember children are the same
    public boolean childExists(FamilyMember child){
        for (int i=0; i<children.size(); i++){
            if (children.get(i).isEqual(child)){
                return true;
            }
        }
        return false;
    }

    //Returns whether the current object and a given FamilyMember object have the same names, gender and address
    public boolean isEqual(FamilyMember member) {
        return (firstName.equals(member.getFirstName()) && lastName.equals(member.getLastName())
                && maidenName.equals(member.getMaidenName()) && gender == member.getGender()
                && address.isAddressEquals(member.getAddress()));    	
    }
    
    //Method validates all fields by calling sub validation methods (validateNames and validateNumbers)
    //Returns an errorMessage consisting of all invalid fields
    public static String validateFields(String fName, String lName, String mName, String lifeDesc, 
                                    String stNo, String stName, String sub, String postal,
                                    String gender, String rType, FamilyMember currentMember){
                
        String errorMsg = "";
        
        //create temp objects to hold new member for validation
        Address newAddress = new Address(stNo, stName, sub, postal);
        FamilyMember newMember = new FamilyMember(fName, lName, gender, newAddress, lifeDesc);
        newMember.setMaidenName(mName);

        //If relative type is not-empty (aka member is added as relative), validate the current member 
        //depending on the relation of the new member to the current member
        if (rType != null){
            switch (rType){
                case "MOTHER":
                        //check that current object doesn't have a mother
                        if (currentMember.has(FamilyMember.Relation_MOTHER)) {
                            errorMsg += "\n-Mother (Already Exists)\n";
                        //check that gender of new member is female
                        } else if (currentMember.getMother() != null) {
                            if (currentMember.getMother().getGender() != FamilyMember.Gender_FEMALE){
                                errorMsg += "\n-Mother (Wrong Gender)\n";
                            }
                        }
                    break;
                case "FATHER":
                    //check that current object doesn't have a father
                    if (currentMember.has(FamilyMember.Relation_FATHER)) {
                        errorMsg += "\n-Father (Already Exists)\n";
                    //check that gender of new member is male
                    } else if (currentMember.getFather() != null) {
                            if (currentMember.getFather().getGender() != FamilyMember.Gender_MALE){
                                errorMsg += "\n-Father (Wrong Gender)\n";
                            }
                    }
                    break;
                case "CHILD":
                    //Checks if the current object already has the parameter as a child
                    if (currentMember.childExists(newMember)){
                        errorMsg +=  "\n-Child (Already Exists)";
                    }
                    break;
                case "SPOUSE":
                    //check that current object doesn't have a spouse
                    if (currentMember.has(FamilyMember.Relation_SPOUSE)) {
                        errorMsg += "\n-Spouse (Already Exists)\n";
                    //check that gender of new member is male
                    } else if (currentMember.getGender() == newMember.getGender()) {
                        errorMsg += "\n-Spouse (Wrong Gender)\n";
                    }
            }            
        }
        
        //Check that all fields are appropriate, and append error messages if necessary
        if (!validateNames(fName) || fName.length() == 0) errorMsg += "\n-First Name (No Digits/Special Characters/Cannot be empty)";
        if (!validateNames(lName) || lName.length() == 0) errorMsg += "\n-Last Name (No Digits/Special Characters/Cannot be empty)";
        if (!validateNames(mName)) errorMsg += "\n-Maiden Name (No Digits/Special Characters)";
        if (gender == "MALE" && mName.length() > 0) errorMsg += "\n-Maiden Name (Applicable to Females Only)";
        if (lifeDesc.length() == 0) errorMsg += "\n-Life Description (Cannot be empty)";
        if (!validateNumbers(stNo) || stNo.length() == 0) errorMsg += "\n-Street Number (Only Numbers/No Spaces/Cannot be empty)";
        if (!validateNames(stName) || stName.length() == 0) errorMsg += "\n-Street Name (No Digits/Special Characters/Cannot be empty)";
        if (!validateNames(sub) || sub.length() == 0) errorMsg += "\n-Suburb (No Digits/Special Characters/Cannot be empty)";
        if (!validateNumbers(postal) || postal.length() == 0) errorMsg += "\n-Postal Code (Only Numbers/No Spaces/Cannot be empty)";  
              
        return errorMsg;
    }
    
    //Method that checks a given string name and ensures that it contains only letters and spaces
    //Uses for loop to run through each character individually
    private static boolean validateNames(String input){
        input = input.toUpperCase();
        for (int i=0; i<input.length(); i++){
            if ((input.charAt(i) < 65 || input.charAt(i) > 90) && input.charAt(i) != 32){
                return false;
            }
        }
        return true;
    }
    
    //Method that checks a given string number and ensures that it contains only digits
    //Uses for loop to run through each character individually
    private static boolean validateNumbers(String input){
        for (int i=0; i<input.length(); i++){
            if (input.charAt(i) < 48 || input.charAt(i) > 57){
                return false;
            }
        }
        return true;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
}
