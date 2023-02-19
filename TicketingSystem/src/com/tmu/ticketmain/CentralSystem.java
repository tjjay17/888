/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tmu.ticketmain;
import java.io.*;

/**
 *
 * @author Tj
 */
public class CentralSystem {
    public static void main(String[] args) throws IOException{
        if(args.length > 0){
            System.out.println("No args needed.");
        }
        
        String userInput = "";
        try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
            //prompts can be changed
            System.out.println("Welcome to the ticket system");
            System.out.println("Enter a command to begin");
            while((userInput = stdIn.readLine()) != null){
                //readin user inputs and assess them
                if(userInput.equals("login")){
                    System.out.println("login stuff");
                    break;
                }
                
            }        
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
