package org.openhab.binding.veluxklf200.internal.commands.request;

public class GW_PASSWORD_ENTER_REQ extends BaseRequest {

    private String password;

    public GW_PASSWORD_ENTER_REQ(String password) {
        super();
        this.password = password;
    }

    @Override
    protected void Pack() {
    }

}
