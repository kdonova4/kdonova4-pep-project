package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * add the account using AccountDAO
     * @param account
     * @return Account added
     */
    public Account addAccount(Account account) {
        if(validate(account) && validateDuplicate(account)) {
            return accountDAO.createAccount(account);
        }else {
            return null;
        }
    }

    /**
     * Logs in using AccountDAO
     * @param account
     * @return the account that was logged into
     */
    public Account login(Account account) {
        if(validate(account)) {
            return accountDAO.login(account);
        } else {
            return null;
        }
    }

    /**
     * validates if an account was a duplicate or not for adding an account
     * @param account
     * @return true if the account is valid, false if not
     */
    private boolean validateDuplicate(Account account) {
        if(accountDAO.findByUsername(account.getUsername()) != null) {
            System.out.println("DUPLICATE");
            return false;
        }

        return true;
    }

    /**
     * validates the account username and password
     * @param account
     * @return true if the username and password are valid
     */
    private boolean validate(Account account) {
        if(account == null) {
            System.out.println("ACCOUNT NULL");
            return false;
        }

        if(account.getUsername().isBlank()) {
            System.out.println("USERNAME BLANK");
            return false;
        }

        if(account.getPassword().length() < 4) {
            System.out.println("PASSWORD LENGTH");
            return false;
        }


        return true;
    }
}
