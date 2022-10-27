package cinema;

import io.micrometer.core.lang.Nullable;

public class BookAuth {
    @Nullable
    private String password;

    public BookAuth(String password) {
        this.password = password;
    }

    public BookAuth() {
        this.password = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
