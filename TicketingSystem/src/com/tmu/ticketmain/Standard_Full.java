package com.tmu.ticketmain;

import java.io.*;
import java.util.*;

public class Standard_Full extends User {

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

    public int getPosition(String target, List userList) {
        int foundAt = 0;
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
            localCheck = target.equals(nameList.get(i));
            if (localCheck == true) {
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

    public Standard_Full(String usertype, String username, double credit) {
        super(usertype, username, credit);
    }

    public void refundRequest(String currentUser, double currentUserCredits, List<User> storedList, List<RefundRequest> refundRequestList) {
        //basic requirements
        String operation;
        String seller = currentUser;
        String buyer = "";
        double sellerCredit = currentUserCredits;
        double buyerCredit = 0.0;
        double refundCredit = 0.0;
        //method specific
        boolean refundRequest_active = true;
        Scanner userInput = new Scanner(System.in);
        boolean validInput = false;
        boolean check = false;
        boolean buyerChecked = false;
        double comparator = 0.0;
        int position = 0;
        List userList = getUserList(storedList);

        while (refundRequest_active) {
            System.out.println("Select operation: new refund / cancel");
            do {
                operation = userInput.nextLine().toLowerCase();
                if (!(operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel"))) {
                    System.out.println("Invalid operation. Please resubmit.");
                }
            } while (!(operation.toLowerCase().equals("new refund") || operation.toLowerCase().equals("cancel")));

            switch (operation) {
                case "cancel":
                    refundRequest_active = false;
                    break;
                case "new refund":
                    //find buyer
                    System.out.println("Enter buyer username");
                    buyer = userInput.nextLine();
                    while (buyer.length() > 15) {
                        System.out.println("Invalid entry for username. Please resubmit.");
                        buyer = userInput.nextLine();
                    }
                    check = getUser(buyer, userList);
                    if (check == true) {
                        position = getPosition(seller, userList);
                    }
                    //buyer wallet
                    buyerCredit = storedList.get(position).getCredit();
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
                    //Formatting request
                    refundRequestList.add(new RefundRequest(seller, buyer, sellerCredit, buyerCredit, refundCredit));
                    String formattedRequest = String.format("Created refund request for %s for an amount of %.2f", buyer, refundCredit);
                    System.out.println(formattedRequest);
                    refundRequest_active = false;
                    break;
            }
        }
        refundRequest_active = false;
    }
}
