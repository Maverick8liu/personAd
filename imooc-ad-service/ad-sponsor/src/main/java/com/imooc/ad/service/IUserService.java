package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.CreateUserReponse;
import com.imooc.ad.vo.CreateUserRequest;

public interface IUserService {


    CreateUserReponse createUser(CreateUserRequest request) throws AdException;
}
