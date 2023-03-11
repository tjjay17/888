package com.tmu.ticketmain;

import java.io.*;
import java.util.*;

public class Admin extends User{

    public Admin(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }
    
    public boolean getUser(String target){
        //Placeholder function. Used to retrieve user data from user list.
        int i = 0;
        boolean check = false;
        boolean localCheck;
        String outputMessage = "";
        List userList = new ArrayList();
        //create list while not reading from array list in CentralCore
        try{
            File users = new File("users.txt");
            Scanner userReader = new Scanner(users);
            while (userReader.hasNextLine()) {
                String readIn = userReader.nextLine();
                String temp = readIn.split(" ")[0];
                userList.add(temp);
            }
            do{
                localCheck = target.equals(userList.get(i));
//                System.out.println(userList.get(i));
                if(localCheck == true){
                    outputMessage = String.format("User \"%s\" successfully found.", target);
                    System.out.println(outputMessage);
                    check = true;
                    break;
                }
                else if(localCheck == false && i<userList.size()-1){
                    i++;
                    check = false;
                }
                else{
                    System.out.println("No such user exists in system.");
                    check = false;
                    break;
                }
            }
            while(check != true);
            userReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        return check;
    }
    
    public boolean checkUsername(String testUsername){
        int i = 0;
        boolean check = false;
        boolean localCheck;
        List userList = new ArrayList();
        //create list while not reading from array list in CentralCore
        try{
            File users = new File("users.txt");
            Scanner userReader = new Scanner(users);
            while (userReader.hasNextLine()) {
                String readIn = userReader.nextLine();
                String temp = readIn.split(" ")[0];
                userList.add(temp);
            }
            do{
                localCheck = testUsername.equals(userList.get(i));
                if(localCheck == true){
                    System.out.println("User already exists.");
                    check = false;
                    break;
                }
                else if(localCheck == false && i<userList.size()-1){
                    i++;
                    check = false;
                }
                else{
                    System.out.println("No such user exists in system.");
                    check = true;
                    break;
                }
            }
            while(check != true);
            userReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        return check;
    }
    
    public String formatter(String username){
        String tempFormattedUser = username;
        String formattedUser;
        int length;
        length = tempFormattedUser.length();
        do{
            formattedUser = tempFormattedUser + " ";
            tempFormattedUser = formattedUser;
            length = tempFormattedUser.length();
        }
        while(length!=15);
        return formattedUser;
    }
    
    public static int getPosition(String target){
        int foundAt = 0;
        int i = 0;
        boolean check = false;
        boolean localCheck;
        String outputMessage = "";
        List userList = new ArrayList();
        //create list while not reading from array list in CentralCore
        try{
            File users = new File("users.txt");
            Scanner userReader = new Scanner(users);
            while (userReader.hasNextLine()) {
                String readIn = userReader.nextLine();
                String temp = readIn.split(" ")[0];
                userList.add(temp);
            }
            do{
                localCheck = target.equals(userList.get(i));
//                System.out.println(userList.get(i));
                if(localCheck == true){
                    System.out.println(outputMessage);
                    check = true;
                    foundAt = i;
                    break;
                }
                else if(localCheck == false && i<userList.size()-1){
                    i++;
                    check = false;
                }
                else{
                    check = false;
                    break;
                }
            }
            while(check != true);
            userReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        System.out.println(foundAt);
        return foundAt;
    }
    
    public static String creditFormatter(Double credits){
        String tempFormattedCredits = Double.toString(credits);
        String formattedCredits;
        int length;
        length = tempFormattedCredits.length();
        do{
            formattedCredits = tempFormattedCredits + "0";
            tempFormattedCredits = formattedCredits;
            length = tempFormattedCredits.length();
        }
        while(length!=9);
        return formattedCredits;
    }
    
    public void getRefundRequests(){
        //Placeholder function. Used to retreive list of refund requests from sellers
        System.out.println("No refund requests to be retrieved.");
    }
    
    @Override
    public void addCredit(){
        //basic requirements
        String username;
        double updatedCredits = 0.0;
        double credits = 0.0;
        //specific for function
        boolean addCredits_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        boolean check = false;
        int position = 0;
        double oldValue = 0.0;
        double comparator = 0.0;
        
        while(addCredits_active){
            System.out.println("Enter target username:");
            //checking username
            username = userInput.nextLine();
            check = getUser(username);
            while(username.length() > 15 || check == false){
                System.out.println("Invalid entry for username. Please resubmit.");
                username = userInput.nextLine();
                check = getUser(username);
            }
            //Enter credit addition
            System.out.println("Enter the credit amount");
            do {
                if(userInput.hasNextDouble()){
                    credits = userInput.nextDouble();
                    comparator = Double.compare(credits, 1000.00);
                    if(comparator > 0){
                        System.out.println("value in excess of 1000.00. Please resubmit.");
                        validInput = false;
                        userInput.next();
                        comparator = Double.compare(credits, 1000.00);
                    }
                    else{
                        validInput = true;
                        break;
                    }
                }
                else{
                    System.out.println("Invalid entry for credit. Please resubmit.");
                    validInput = false;
                    userInput.next();
                }
            }
            while(!(validInput));
            //finding position
            if(check == true){
                position = getPosition(username);
                //updating the credits
                try{
                    int i = 0;
                    List userList = new ArrayList();
                    //create list while not reading from array list in CentralCore
                    File users = new File("users.txt");
                    Scanner userReader = new Scanner(users);
                    //readIn while userlist not ready in CentralCore
                    while(userReader.hasNextLine()) {
                        String readIn = userReader.nextLine();
                        userList.add(readIn);
                    }
                    userReader.close();
                    //Modifying the credits
                    String modifiable = userList.get(position).toString();
                    String coreChunk = modifiable.substring(0,20);
                    oldValue = Double.parseDouble(modifiable.substring(20,29));
                    updatedCredits = oldValue + credits;
                    String convertedCredits = creditFormatter(updatedCredits);
                    modifiable = coreChunk + convertedCredits;
                    userList.set(position, modifiable);
                    System.out.println(userList);
                    //writing while userlist not ready in CentralCore
                    FileWriter fileWriter = new FileWriter("users.txt");
                    int size = userList.size();
                    for (i=0;i<size;i++) {
                        String str = userList.get(i).toString();
                        fileWriter.write(str);
                        if(i < size - 1)
                            fileWriter.write("\n");
                    }
                    fileWriter.close();
                    //Success message
                    String deletionMessageSuccess = String.format("Updated credits for user with username \"%s\" to %f", username, updatedCredits);
                    System.out.println(deletionMessageSuccess);
                }
                catch(IOException e){
                    System.out.println("File not found.");
                }
            }
            addCredits_active = false;
        }
    }
    
    public void createUser(){
        //basic requirements
        String username;
        String type;
        double credits = 0;
        
        //specific for function
        boolean createUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        boolean check;

        while(createUser_active){
            //filling data
            System.out.println("Enter a new username");
            username = userInput.nextLine();
            check = checkUsername(username);
            while(username.length() > 15 || check == false){
                System.out.println("Invalid entry for username. Please resubmit.");
                username = userInput.nextLine();
                check = checkUsername(username);
            }
            
            System.out.println("Enter the user type");
            do {
                type = userInput.nextLine().toLowerCase();
                if(!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss"))){
                    System.out.println("Invalid entry for type. Please resubmit.");
                }
            }while(!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss")));
            validInput = false;
            
            System.out.println("Enter the credit amount");
            do {
                if(userInput.hasNextDouble()){
                    credits = userInput.nextDouble();
                    validInput = true;
                }
                else{
                    System.out.println("Invalid entry for credit. Please resubmit.");
                    validInput = false;
                    userInput.next();
                }
            }while(!(validInput));
            validInput = false;
            
            userInput.close();
            
            //attempt to bundle data
            //Write to file directly while userlist not made in CentralCore
            try{
                FileWriter fw = new FileWriter("users.txt", true); //the true will append the new data
                String formattedUsername = formatter(username);
                String formatted = String.format("%s  %s %.2f\n", formattedUsername, type.toUpperCase(), credits);
                fw.write(formatted);
                System.out.print(formatted);
                fw.close();
            }
            catch (Exception e) {
		System.out.println("Exception caught. Reattempt submission.");
	    }
            //attempt to add to user file
            //-->To be added in later version
            createUser_active = false;
        }
    }
    
    public void deleteUser(){
        //Preliminary version. No reading from UserList currently incorporated. Future version will update function to search userList for matching user.
        String username;
        boolean isFound;
        //Specific to function
        boolean check;
        int position = 0;
        
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter user to be deleted");
        username = userInput.nextLine();
        check = getUser(username);
        while(username.length() > 15 || check == false){
            System.out.println("Invalid entry for username. Please resubmit.");
            username = userInput.nextLine();
            check = getUser(username);
        }
        isFound = check;
        if(isFound == true){
            position = getPosition(username);
        }
        if(isFound == true){
            try{
                int i = 0;
                List userList = new ArrayList();
                //create list while not reading from array list in CentralCore
                File users = new File("users.txt");
                Scanner userReader = new Scanner(users);
                //readIn while userlist not ready in CentralCore
                while(userReader.hasNextLine()) {
                    String readIn = userReader.nextLine();
                    userList.add(readIn);
                }
                userReader.close();
                userList.remove(position);
                //writing while userlist not ready in CentralCore
                FileWriter fileWriter = new FileWriter("users.txt");
                int size = userList.size();
                for (i=0;i<size;i++) {
                    String str = userList.get(i).toString();
                    fileWriter.write(str);
                    if(i < size - 1)
                        fileWriter.write("\n");
                }
                fileWriter.close();
                //Success message
                String deletionMessageSuccess = String.format("Deleted user with username \"%s\"", username);
                System.out.println(deletionMessageSuccess);
            }
            catch(IOException e){
                System.out.println("File not found.");
            }
        }
        else{
            String deletionMessageFail = String.format("Failed to delete user \"%s\".", username);
            System.out.println(deletionMessageFail);
        }
        
        userInput.close();
    }
    
    public void refund(){
        //Preliminary version. Currently not reading or receiving data from sources. Future version will process requests as well as create new refunds and update user data accordingly.
        //basic requirements
        String operation;
        String seller;
        String buyer;
        double sellerCredit = 0;
        double buyerCredit = 0;
        double refundCredit = 0;
        //method specific
        boolean refundUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        double comparator = 0.0;
        
        while(refundUser_active){
            System.out.println("Select operation: fetch requests / new refund / cancel");
            do {
                operation = userInput.nextLine().toLowerCase();
                if(!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel"))){
                    System.out.println("Invalid operation. Please resubmit.");
                }
            }while(!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel")));
            switch(operation){
                case "cancel":
                    userInput.close();
                    refundUser_active = false;
                    break;
                case "fetch requests":
                    getRefundRequests();
                    break;
                case "new refund":
                    //find seller
                    System.out.println("Enter seller username");
                    seller = userInput.nextLine();
                    while(seller.length() > 15){
                        System.out.println("Invalid entry for username. Please resubmit.");
                        seller = userInput.nextLine();
                    }
                    getUser(seller);
                    //find buyer
                    System.out.println("Enter buyer username");
                    buyer = userInput.nextLine();
                    while(buyer.length() > 15){
                        System.out.println("Invalid entry for username. Please resubmit.");
                        buyer = userInput.nextLine();
                    }
                    getUser(buyer);
                    //seller wallet
                    System.out.println("Enter the seller credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            sellerCredit = userInput.nextDouble();
                            validInput = true;
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //buyer wallet
                    System.out.println("Enter the buyer credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            buyerCredit = userInput.nextDouble();
                            validInput = true;
                            break;
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //refund amount
                    System.out.println("Enter the refund credit amount");
                    do {
                        if(userInput.hasNextDouble()){
                            refundCredit = userInput.nextDouble();
                            comparator = Double.compare(refundCredit, sellerCredit);
                            if(comparator > 0){
                                System.out.println("Insufficient seller credit. Please resubmit.");
                                validInput = false;
                                userInput.next();
                            }
                            else{
                                validInput = true;
                                break;
                            }
                        }
                        else{
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    }while(!(validInput));
                    validInput = false;
                    //adding credit
                    double tempBuyer = buyerCredit + refundCredit;
                    buyerCredit = tempBuyer;
                    //subtracting credit
                    double tempSeller = sellerCredit - refundCredit;
                    sellerCredit = tempSeller;
                    //confirmation message
                    String successfulAdded = String.format("Successfully added %.2f to %s wallet, resulting in %.2f",refundCredit, buyer, buyerCredit);
                    System.out.println(successfulAdded);
                    String successfulSubtracted = String.format("Successfully subtracted %.2f from %s wallet, resulting in %.2f",refundCredit, seller, sellerCredit);
                    System.out.println(successfulSubtracted);
                    break;
            }
        }
    }
}
