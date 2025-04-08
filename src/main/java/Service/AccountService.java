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

    public Account addAccount(Account account) {
        if(validate(account) && validateDuplicate(account)) {
            return accountDAO.createAccount(account);
        }else {
            return null;
        }
    }

    public Account login(Account account) {
        if(validate(account)) {
            return accountDAO.login(account);
        } else {
            return null;
        }
    }

    private boolean validateDuplicate(Account account) {
        if(accountDAO.findByUsername(account.getUsername()) != null) {
            System.out.println("DUPLICATE");
            return false;
        }

        return true;
    }

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
