package org.presentation4you.resource_controller.server.Repository;

import org.presentation4you.resource_controller.commons.Response.GetUserInfoResp;
import org.presentation4you.resource_controller.commons.Response.IResponse;
import org.presentation4you.resource_controller.commons.Response.Response;
import java.util.Calendar;

public class RepositoryWrapper implements IRepositoryWrapper {
    private IUserRepo userRepo;
    private IRequestRepo requestRepo;
    private IResourceRepo resourceRepo;

    public IRepositoryWrapper setUserRepo(final IUserRepo userRepo) {
        this.userRepo = userRepo;
        return this;
    }

    public IRepositoryWrapper setResourceRepo(final IResourceRepo resourceRepo) {
        this.resourceRepo = resourceRepo;
        return this;
    }

    public IRepositoryWrapper setRequestRepo(final IRequestRepo requestRepo) {
        this.requestRepo = requestRepo;
        return this;
    }

    public IResponse getUserInfo(final String login) {
        if (userRepo == null) {
            throw new NullPointerException();
        }

        GetUserInfoResp response = new GetUserInfoResp();
        String email = userRepo.get(login);
        if (email == null) {
            response.setIsNotFound();
        } else {
            response.setLogin(login);
            response.setEmail(email);
        }
        return response;
    }

    public IResponse addResource(final int id, final String type) {
        if (resourceRepo == null) {
            throw new NullPointerException();
        }

        IResponse response = new Response();
        if (resourceRepo.has(id)) {
            response.setAlreadyHas();
            return response;
        }
        resourceRepo.add(id, type);
        return response;
    }

    public IResponse removeResource(final int id) {
        if (resourceRepo == null) {
            throw new NullPointerException();
        }

        IResponse response = new Response();
        if (resourceRepo.has(id)) {
            resourceRepo.remove(id);
            return response;
        }

        response.setIsNotFound();
        return response;
    }

    public IResponse addRequest(final int resourceId, final Calendar from,
                                final Calendar to, final String login) {
        if (requestRepo == null) {
            throw new NullPointerException();
        }

        IResponse response = new Response();
        try {
             if (requestRepo.canAdd(resourceId, from, to, login)) {
                 requestRepo.add(resourceId, from, to, login);
             } else {
                 response.setAlreadyHas();
             }
        } catch (IllegalArgumentException iae) {
            response.setHasConflict();
        }

        return response;
    }
}