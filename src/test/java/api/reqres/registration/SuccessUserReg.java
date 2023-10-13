package api.reqres.registration;

public class SuccessUserReg {

    private Integer id;
    private String token;

    public SuccessUserReg(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public SuccessUserReg() {
    }
}
