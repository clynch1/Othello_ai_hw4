/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Connor
 */
public class NewClass {
     public static                       KeyboardInputClass keyboardInput = new KeyboardInputClass();
     
     public static void main(String[] args) {
        keyboardInput.getString(" ", "Who would you like to hack:");
        keyboardInput.getString(" ", "What is the target:");
        keyboardInput.getString(" ", "You have selected fridge, what would you like to add:");
         System.out.println("Hack Complete!\n"
                 + "Please Check Fridge");
            
    }
}
