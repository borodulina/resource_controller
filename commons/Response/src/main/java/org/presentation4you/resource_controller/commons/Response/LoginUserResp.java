package org.presentation4you.resource_controller.commons.Response;

import org.presentation4you.resource_controller.commons.Role.Coordinator;
import org.presentation4you.resource_controller.commons.Role.Employee;
import org.presentation4you.resource_controller.commons.Role.IRole;

public class LoginUserResp extends Response {
    private IRole role;

    public static IResponse buildLoginUserResp(final String login, final String password,
                                               final String role) {
        if (role == null) {
            return new LoginUserResp();
        } else if (role.equals(IRole.COORDINATOR)) {
            return new LoginUserResp(new Coordinator().setLogin(login)
                    .setPassword(password));
        } else if (role.equals(IRole.EMPLOYEE)) {
            return new LoginUserResp(new Employee().setLogin(login)
                    .setPassword(password));
        }
        return new LoginUserResp();
    }

    public IRole getRole() {
        return role;
    }

    private LoginUserResp() {
        setIsNotFound();
    }

    private LoginUserResp(final IRole role) {
        this.role = role;
        setIsOk();
    }
}
