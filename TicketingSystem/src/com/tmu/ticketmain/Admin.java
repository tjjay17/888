package com.tmu.ticketmain;

import java.io.*;
import java.util.*;

public class Admin extends User {

    public Admin(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }

    public String userFormatter(String username) {
        String tempFormattedUser = username;
        String formattedUser;
        int length;
        length = tempFormattedUser.length();
        do {
            formattedUser = tempFormattedUser + " ";
            tempFormattedUser = formattedUser;
            length = tempFormattedUser.length();
        } while (length != 15);
        return formattedUser;
    }

    public String typeFormatter(String type) {
        String formattedType = type + " ";
        return formattedType;
    }

    public String creditFormatter(Double credits) {
        String tempFormattedCredits = Double.toString(credits);
        String formattedCredits;
        int length;
        length = tempFormattedCredits.length();
        do {
            formattedCredits = tempFormattedCredits + "0";
            tempFormattedCredits = formattedCredits;
            length = tempFormattedCredits.length();
        } while (length != 9);
        return formattedCredits;
    }

    public List getUserList(List<User> storedList) {
        //basic
        int i = 0;
        String entry;
        //method specific
        String username;
        String usertype;
        double credits;
        //functionality
        List userList = new ArrayList();
        for (User u : storedList) {
            username = userFormatter(u.getUsername());
            usertype = typeFormatter(u.getUsertype());
            credits = Double.parseDouble(creditFormatter(u.getCredit()));
            entry = username + usertype + credits;
            userList.add(entry);
        }
        return userList;
    }

    public boolean getUser(String target, List userList) {
        int i = 0;
        boolean check = false;
        boolean localCheck;
        String outputMessage = "";
        //Name list
        List nameList = new ArrayList();
        for (i = 0; i < userList.size(); i++) {
            String name = userList.get(i).toString();
            nameList.add(name.split(" ")[0]);
        }
        i = 0;
        do {
            localCheck = target.equals(nameList.get(i));
            if (localCheck == true) {
                outputMessage = String.format("User \"%s\" successfully found.", target);
                System.out.println(outputMessage);
                check = true;
                break;
            } else if (localCheck == false && i < nameList.size() - 1) {
                i++;
                check = false;
            } else {
                System.out.println("No such user exists in system.");
                check = false;
                break;
            }
        } while (check != true);
        return check;
    }

    public boolean checkUsername(String testUsername, List userList) {
        int i = 0;
        boolean check = false;
        boolean localCheck;
        //name list
        List nameList = new ArrayList();
        for (i = 0; i < userList.size(); i++) {
            String name = userList.get(i).toString();
            nameList.add(name.split(" ")[0]);
        }
        i = 0;
        do {
            localCheck = testUsername.equals(nameList.get(i));
            if (localCheck == true) {
                System.out.println("User already exists.");
                check = false;
                break;
            } else if (localCheck == false && i < nameList.size() - 1) {
                i++;
                check = false;
            } else {
                System.out.println("No such user exists in system.");
                check = true;
                break;
            }
        } while (check != true);
        return check;
    }

    public int getPosition(String target, List userList) {
        int foundAt = 0;
        int i = 0;
        boolean check = false;
        boolean localCheck;
        String outputMessage = "";
        //name list
        List nameList = new ArrayList();
        for (i = 0; i < userList.size(); i++) {
            String name = userList.get(i).toString();
            nameList.add(name.split(" ")[0]);
        }
        i = 0;
        do {
            localCheck = target.equals(nameList.get(i));
            if (localCheck == true) {
                System.out.println(outputMessage);
                check = true;
                foundAt = i;
                break;
            } else if (localCheck == false && i < nameList.size() - 1) {
                i++;
                check = false;
            } else {
                check = false;
                break;
            }
        } while (check != true);
        return foundAt;
    }

    public boolean checkCredits(double target, int position, List<User> storedList) {
        boolean check = false;
        double toCompare = storedList.get(position).getCredit();
        int comparator = Double.compare(target, toCompare);
        if (comparator == 0) {
            check = true;
        }
        return check;
    }

    public void getRefundRequests() {
        //Placeholder function. Used to retreive list of refund requests from sellers
        System.out.println("No refund requests to be retrieved.");
    }

    public List addCredit(List<User> storedList) {
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
        List userList = getUserList(storedList);
        //Functionality
        while (addCredits_active) {
            System.out.println("Enter target username:");
            //checking username
            username = userInput.nextLine();
            check = getUser(username, userList);
            while (username.length() > 15 || check == false) {
                System.out.println("Invalid entry for username. Please resubmit.");
                username = userInput.nextLine();
                check = getUser(username, userList);
            }
            //Enter credit addition
            System.out.println("Enter the credit amount");
            do {
                if (userInput.hasNextDouble()) {
                    credits = userInput.nextDouble();
                    comparator = Double.compare(credits, 1000.00);
                    if (comparator > 0) {
                        System.out.println("value in excess of 1000.00. Please resubmit.");
                        validInput = false;
                        userInput.nextDouble();
                        comparator = Double.compare(credits, 1000.00);
                    } else {
                        validInput = true;
                        break;
                    }
                } else {
                    System.out.println("Invalid entry for credit. Please resubmit.");
                    validInput = false;
                    userInput.nextDouble();
                }
            } while (!(validInput));
            //finding position
            if (check == true) {
                position = getPosition(username, userList);
                //Retreiving credits
                oldValue = storedList.get(position).getCredit();
                //Updating credits
                updatedCredits = oldValue + credits;
                //Updating list entry
                storedList.get(position).setCredit(updatedCredits);
                //Success message
                String addCreditMessageSuccess = String.format("Updated credits for user with username \"%s\" to %f", username, updatedCredits);
                System.out.println(addCreditMessageSuccess);
            }
            addCredits_active = false;
        }
        return storedList;
    }

    public List createUser(List<User> storedList) {
        //basic requirements
        String username;
        String type;
        double credits = 0;
        //specific for function
        boolean createUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        boolean check;
        List userList = getUserList(storedList);
        //User input stream
        while (createUser_active) {
            //Username
            System.out.println("Enter a new username");
            username = userInput.nextLine();
            check = checkUsername(username, userList);
            while (username.length() > 15 || check == false) {
                System.out.println("Invalid entry for username. Please resubmit.");
                username = userInput.nextLine();
                check = checkUsername(username, userList);
            }
            //User type
            System.out.println("Enter the user type");
            do {
                type = userInput.nextLine().toLowerCase();
                if (!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss"))) {
                    System.out.println("Invalid entry for type. Please resubmit.");
                }
            } while (!(type.toLowerCase().equals("aa") || type.toLowerCase().equals("fs") || type.toLowerCase().equals("sb") || type.toLowerCase().equals("ss")));
            validInput = false;
            //Entering credit value
            System.out.println("Enter the credit amount");
            do {
                if (userInput.hasNextDouble()) {
                    credits = userInput.nextDouble();
                    validInput = true;
                } else {
                    System.out.println("Invalid entry for credit. Please resubmit.");
                    validInput = false;
                    userInput.next();
                }
            } while (!(validInput));
            validInput = false;
            //attempt to bundle data
            try {
                String formattedUsername = userFormatter(username);
                String formatted = String.format("%s  %s %.2f\n", formattedUsername, type.toUpperCase(), credits);
//                userList.add(formatted);
                switch (type) {
                    case "aa":
                        storedList.add(new Admin(type, username, credits));
                        break;
                    case "fs":
                        storedList.add(new Standard_Full(type, username, credits));
                        break;
                    case "sb":
                        storedList.add(new Standard_Buy(type, username, credits));
                        break;
                    case "ss":
                        storedList.add(new Standard_Sell(type, username, credits));
                        break;
                }
                System.out.print(formatted);
                System.out.println(storedList);
            } catch (Exception e) {
                System.out.println("Exception caught. Reattempt submission.");
            }
            createUser_active = false;
        }
        return storedList;
    }

    public List deleteUser(List<User> storedList) {
        String username;
        boolean isFound;
        //Specific to function
        boolean check;
        int position = 0;
        List userList = getUserList(storedList);
        //User Input stream
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter user to be deleted");
        username = userInput.nextLine();
        check = getUser(username, userList);
        while (username.length() > 15 || check == false) {
            System.out.println("Invalid entry for username. Please resubmit.");
            username = userInput.nextLine();
            check = getUser(username, userList);
        }
        isFound = check;
        if (isFound == true) {
            position = getPosition(username, userList);
        }
        if (isFound == true) {
            try {
                storedList.remove(position);
                //Success message
                String deletionMessageSuccess = String.format("Deleted user with username \"%s\"", username);
                System.out.println(deletionMessageSuccess);
                //For testing purposes
                System.out.println(storedList);
            } catch (Exception e) {
                System.out.println("Delete request failed.");
            }
        } else {
            String deletionMessageFail = String.format("Failed to delete user \"%s\".", username);
            System.out.println(deletionMessageFail);
        }
        return storedList;
    }

    public List refund(List<User> storedList) {
        //basic requirements
        String operation = "";
        String seller = "";
        String buyer = "";
        double sellerCredit = 0;
        double buyerCredit = 0;
        double refundCredit = 0;
        int sellerPos = 0;
        int buyerPos = 0;
        //method specific
        boolean refundUser_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean check1;
        boolean check2;
        boolean validInput = false;
        int comparator = 0;
        boolean sellerChecked = false;
        boolean buyerChecked = false;
        List userList = getUserList(storedList);

        while (refundUser_active) {
            System.out.println("Select operation: fetch requests / new refund / cancel");
            do {
                operation = userInput.nextLine().toLowerCase();
                if (!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel"))) {
                    System.out.println("Invalid operation. Please resubmit.");
                }
            } while (!(operation.toLowerCase().equals("fetch requests") || operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel")));
            switch (operation) {
                case "cancel":
                    refundUser_active = false;
                    break;
                case "fetch requests":
                    getRefundRequests();
                    break;
                case "new refund":
                    //find seller
                    System.out.println("Enter seller username");
                    seller = userInput.nextLine();
                    while (seller.length() > 15) {
                        System.out.println("Invalid entry for username. Please resubmit.");
                        seller = userInput.nextLine();
                    }
                    check1 = getUser(seller, userList);
                    if (check1 == true) {
                        sellerPos = getPosition(seller, userList);
                    }
                    //find buyer
                    System.out.println("Enter buyer username");
                    buyer = userInput.nextLine();
                    while (buyer.length() > 15) {
                        System.out.println("Invalid entry for username. Please resubmit.");
                        buyer = userInput.nextLine();
                    }
                    check2 = getUser(buyer, userList);
                    if (check2 == true) {
                        buyerPos = getPosition(buyer, userList);
                    }
                    //seller wallet
                    System.out.println("Enter the seller credit amount");
                    do {
                        if (userInput.hasNextDouble()) {
                            sellerCredit = userInput.nextDouble();
                            userInput.nextLine();
                            sellerChecked = checkCredits(sellerCredit, sellerPos, storedList);
                            if (sellerChecked == false) {
                                System.out.println("Incorrect seller credit. Please resubmit.");
                                validInput = false;
                            } else {
                                validInput = true;
                                break;
                            }
                        } else {
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    } while (!(validInput));
                    validInput = false;
                    //buyer wallet
                    System.out.println("Enter the buyer credit amount");
                    do {
                        if (userInput.hasNextDouble()) {
                            buyerCredit = userInput.nextDouble();
                            userInput.nextLine();
                            buyerChecked = checkCredits(buyerCredit, buyerPos, storedList);
                            if (buyerChecked == false) {
                                System.out.println("Incorrect buyer credit. Please resubmit.");
                                validInput = false;
                            } else {
                                validInput = true;
                                break;
                            }
                        } else {
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            buyerCredit = userInput.nextDouble();
                            userInput.nextLine();
                            buyerChecked = checkCredits(buyerCredit, buyerPos, storedList);
                        }
                    } while (!(validInput));
                    validInput = false;
                    //refund amount
                    System.out.println("Enter the refund credit amount");
                    do {
                        if (userInput.hasNextDouble()) {
                            refundCredit = userInput.nextDouble();
                            comparator = Double.compare(refundCredit, sellerCredit);
                            if (comparator > 0) {
                                System.out.println("Insufficient seller credit. Please resubmit.");
                                validInput = false;
                                userInput.next();
                            } else {
                                validInput = true;
                                break;
                            }
                        } else {
                            System.out.println("Invalid entry for credit. Please resubmit.");
                            validInput = false;
                            userInput.next();
                        }
                    } while (!(validInput));
                    validInput = false;
                    //adding credit
                    double tempBuyer = buyerCredit + refundCredit;
                    buyerCredit = tempBuyer;
                    storedList.get(buyerPos).setCredit(buyerCredit);
                    //subtracting credit
                    double tempSeller = sellerCredit - refundCredit;
                    sellerCredit = tempSeller;
                    storedList.get(sellerPos).setCredit(sellerCredit);
                    //confirmation message
                    String successfulAdded = String.format("Successfully added %.2f to %s wallet, resulting in %.2f", refundCredit, buyer, buyerCredit);
                    System.out.println(successfulAdded);
                    String successfulSubtracted = String.format("Successfully subtracted %.2f from %s wallet, resulting in %.2f", refundCredit, seller, sellerCredit);
                    System.out.println(successfulSubtracted);
                    break;
            }
            refundUser_active = false;
        }
        return storedList;
    }
}
