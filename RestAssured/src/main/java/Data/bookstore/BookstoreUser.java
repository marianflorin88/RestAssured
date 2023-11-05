package Data.bookstore;

public class BookstoreUser {
    private String userName;
    private String password;

    public BookstoreUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
