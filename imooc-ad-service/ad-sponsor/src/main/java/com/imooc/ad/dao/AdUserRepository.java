package com.imooc.ad.dao;

import com.imooc.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdUserRepository extends JpaRepository<AdUser,Long>{
    /**
     *
     * @param name
     * @return
     */
    AdUser findByUsername(String name);
}
