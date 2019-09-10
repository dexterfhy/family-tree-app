package ict373assignment2;

import java.io.Serializable;
import java.util.ArrayList;

/*
Title: ICT373 Assignment #2
Author: Dexter Fong (33467253)
File Name: FamilyTree.java

Purpose: 
FamilyTree.java is a class that stores information about a FamilyTree, and forms the basis of the FamilyTree
GUI object. It implements the Serializable interface so that the FamilyTree may be written to 
and read from a file. Furthermore, it contains an instance of the FamilyMember object as the root of the
tree.

It contains:
    - set and get methods that allow a client to change and retrieve values of private instance variables
    - a method to determine if the FamilyTree object as a root or is current empty
    - a method to allow querying of a tree to check if a particular member exists, and if so, choose whether
      to delete said member

Input: 
None. Input is gotten from client GUI and passed into FamilyTree as parameters.

Output:
None. Output is gotten from class and displayed in the client GUI.

Assumptions/Conditions:
- Validation will not be performed on the FamilyMember object when assigned as a root. Instead, it will be done 
  by the client.
*/

public class FamilyTree implements Serializable{
    //Declaration of private instance variables
    private static final long serialVersionUID = 1;
    private FamilyMember root;
    
    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    CONSTRUCTORS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Default constructor for Family Tree
    public FamilyTree() {
        root = null;        
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/

    
    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    GET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Returns root Family Member object
    public FamilyMember getRoot(){
        return root;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    
    
    /////////////////////////////////////////////////////////////////////////////////////////    
    /*---------------------------------------------------------------------------------------
    SET METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Sets the root of the tree to be a given Family Member object
    public void setRoot(FamilyMember newRoot){
        root = newRoot;
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
    

    /////////////////////////////////////////////////////////////////////////////////////////
    /*---------------------------------------------------------------------------------------
    QUERY/ANALYSIS/ACCESS METHODS
    ---------------------------------------------------------------------------------------*/
    /////////////////////////////////////////////////////////////////////////////////////////
    //Returns whether the Family Tree object has a root FamilyMember object, or empty
    public boolean hasRoot(){
        return root !=null;
    }
    
    //Searches a given tree at a specified root for a given member
    //Third boolean parameter delete specifies whether, if a given match is found, the match should be deleted
    //Used in a recursive call that triggers on all children if no match is found at the root or above
    //Returns true if a match is found, and false otherwise
    public static boolean searchMember(FamilyMember root, FamilyMember member, boolean delete) {
        //Checks the father of the current root
    	if(root.getFather() != null) {	
            if(root.getFather().isEqual(member)) {
                if (delete) {
                    //check whether root has a mother who lists the soon-to-be deleted father as spouse
                    //remove the father as the spouse of the mother
                    if (root.has(FamilyMember.Relation_MOTHER)){
                        if (root.getMother().getSpouse().isEqual(root.getFather())){
                            root.getMother().setSpouseNull();
                        }
                    }
                    root.setFatherNull();
                }
                return true;
            }
        }
        
    	//Checks the mother of the current root
    	if(root.getMother() != null) {		
            if(root.getMother().isEqual(member)) {
                if (delete) {
                    //check whether root has a father who lists the soon-to-be deleted mother as spouse
                    //remove the mother as the spouse of the father
                    if (root.has(FamilyMember.Relation_FATHER)){
                        if (root.getFather().getSpouse().isEqual(root.getMother())){
                            root.getFather().setSpouseNull();
                        }
                    }
                    root.setMotherNull();
                }
                return true;
            }
        }

        ArrayList<FamilyMember> children = root.getChildren();
        
        //Checks the spouse of the current root
        if(root.getSpouse() != null) {			
            if(root.getSpouse().isEqual(member)) {
                if (delete) {
                    //run through each children for soon-to-be deleted spouse
                    for(int i = 0; i < children.size(); i++) {
                        //if spouse is female, check whether child has spouse as mother
                        //if so, delete relation to mother
                        if (root.getSpouse().getGender() == FamilyMember.Gender_FEMALE){
                            if (children.get(i).has(FamilyMember.Relation_MOTHER)){
                                if (children.get(i).getMother().isEqual(root.getSpouse())){
                                    children.get(i).setMotherNull();
                                }
                            }
                        } else {
                        //if spouse is male, check whether child has spouse as father
                        //if so, delete relation to father
                            if (children.get(i).has(FamilyMember.Relation_FATHER)){
                                if (children.get(i).getFather().isEqual(root.getSpouse())){
                                    children.get(i).setFatherNull();
                                }  
                            }
                        }
                    }
                    root.setSpouseNull();
                }               
                return true;
            }	
        }
    
        //Recursively checks all children of the current root
        for(int i = 0; i < children.size(); i++) {
            if(children.get(i).isEqual(member)) {
                    if (delete) {
                        if (root.has(FamilyMember.Relation_SPOUSE)){
                            if (root.getSpouse().has(FamilyMember.Relation_CHILDREN)){
                                root.getSpouse().getChildren().remove(i);
                            }                            
                        } 
                        root.getChildren().remove(i);
                    }
                    //remove child from spouse too
                    return true;
            } else if(searchMember(root.getChildren().get(i), member, delete)) {
                    return true;
            }
        }
        return false;		
    }
    /*---------------------------------------------------------------------------------------
    -----------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------*/
}