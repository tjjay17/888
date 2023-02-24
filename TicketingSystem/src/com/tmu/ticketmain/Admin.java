public class Admin {
    User user = new User();
    User newUser = new User();
    
    public User createUser(String username, String type, int credits){
        return newUser;
    }
    
    public User deleteUser(String username, String type, int credits){
        return newUser;
    }
    
    public int addCredit(String username, int credits){
        return credits;
    }
    
    public int refund(String seller, String buyer, int sellerCredits, int buyerCredits){
        return buyerCredits;
    }
}
