package org.presentation4you.resource_controller.commons.Role;

public class Employee extends Role {
    @Override
    public boolean canGetUserInfo() {
        return false;
    }

    @Override
    public boolean canManageResources() {
        return false;
    }

    @Override
    public boolean canUpdateRequests() {
        return false;
    }
}
