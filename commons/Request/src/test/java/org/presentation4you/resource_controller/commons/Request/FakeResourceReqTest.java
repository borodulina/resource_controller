package org.presentation4you.resource_controller.commons.Request;

import org.junit.After;
import org.junit.Test;
import org.presentation4you.resource_controller.commons.Response.IResponse;
import org.presentation4you.resource_controller.commons.Response.ResponseStatus;
import org.presentation4you.resource_controller.commons.Role.Coordinator;
import org.presentation4you.resource_controller.commons.Role.Employee;
import org.presentation4you.resource_controller.server.Repository.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FakeResourceReqTest {
    private static final String PROJECTOR = "Projector";
    private IRepositoryWrapper repo;

    @After
    public void removeFakeRepository() {
        repo = null;
    }

    @Test
    public void canReturnOkIfResourceHasBeenAdded() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsNotPresentRepo());
        Request request = createAddResourceReq(1, PROJECTOR);

        IResponse response = request.exec();

        assertTrue(response.isOk());
    }

    @Test
    public void canReturnNotFoundIfResourceTypeHasNotFound() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceTypeIsNotPresentRepo());
        Request request = createAddResourceReq(1, "Tomato");

        IResponse response = request.exec();

        assertEquals(response.getStatus(), ResponseStatus.NOT_FOUND);
    }

    @Test
    public void canReturnAlreadyHasIfResourceHasNotBeenAdded() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsPresentRepo());
        Request request = createAddResourceReq(1, PROJECTOR);

        IResponse response = request.exec();

        assertEquals(response.getStatus(), ResponseStatus.ALREADY_HAS);
    }

    @Test
    public void canReturnNotValidForAddResourceReqIfRoleIsEmployee() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsNotPresentRepo());
        Request request = new AddResourceReq(new Employee(), 1, PROJECTOR);
        request.setRepository(repo);

        IResponse response = request.exec();

        assertEquals(response.getStatus(), ResponseStatus.NOT_VALID);
    }

    @Test
    public void canReturnOkIfResourceHasBeenRemoved() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsPresentRepo());
        final int id = 1;
        Request addRequest = createAddResourceReq(id, PROJECTOR);
        addRequest.exec();
        Request removeRequest = new RemoveResourceReq(addRequest.getRole(), id);
        removeRequest.setRepository(repo);

        IResponse response = removeRequest.exec();

        assertTrue(response.isOk());
    }

    @Test
    public void canReturnNotFoundIfResourceHasNotBeenRemoved() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsNotPresentRepo());
        Request request = new RemoveResourceReq(new Coordinator(), 1);
        request.setRepository(repo);

        IResponse response = request.exec();

        assertEquals(response.getStatus(), ResponseStatus.NOT_FOUND);
    }

    @Test
    public void canReturnNotValidForRemoveResourceReqIfRoleIsEmployee() {
        repo = new RepositoryWrapper().setResourceRepo(new FakeResourceIsPresentRepo());
        Request request = new RemoveResourceReq(new Employee(), 1);
        request.setRepository(repo);

        IResponse response = request.exec();

        assertEquals(response.getStatus(), ResponseStatus.NOT_VALID);
    }

    private Request createAddResourceReq(final int id, final String type) {
        Request request = new AddResourceReq(new Coordinator(), id, type);
        request.setRepository(repo);

        return request;
    }

}
