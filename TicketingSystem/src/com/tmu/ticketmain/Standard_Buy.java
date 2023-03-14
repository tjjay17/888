package com.tmu.ticketmain;

import java.util.*;

public class Standard_Buy extends User{
    private String userType;
    private String username;
    private double credit;

    public Standard_Buy(String usertype, String username, double credit) {
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

}
