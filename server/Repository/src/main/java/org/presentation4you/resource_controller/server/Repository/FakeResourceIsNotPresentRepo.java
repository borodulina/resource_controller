package org.presentation4you.resource_controller.server.Repository;

import java.util.NoSuchElementException;

public class FakeResourceIsNotPresentRepo implements IResourceRepo {
    @Override
    public boolean has(int id) {
        return false;
    }

    @Override
    public void add(int id, int typeId) {
    }

    @Override
    public void remove(int id) {
    }

    @Override
    public int getType(String type) throws NoSuchElementException {
        return 1;
    }
}
