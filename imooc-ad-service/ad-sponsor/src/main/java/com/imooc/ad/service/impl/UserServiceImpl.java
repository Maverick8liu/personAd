package com.imooc.ad.service.impl;

import com.imooc.ad.constant.Constants;
import com.imooc.ad.dao.AdUserRepository;
import com.imooc.ad.entity.AdUser;
import com.imooc.ad.exception.AdException;
import com.imooc.ad.service.IUserService;
import com.imooc.ad.utils.CommonUtils;
import com.imooc.ad.vo.CreateUserReponse;
import com.imooc.ad.vo.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    private final AdUserRepository userRepository;
    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public CreateUserReponse createUser(CreateUserRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUser oldUser = userRepository.
                findByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        AdUser newUser = userRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.md5(request.getUsername())
        ));

        return new CreateUserReponse(
                newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}