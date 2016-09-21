package org.cent.springBootDemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.cent.springBootDemo.entity.User;
import org.cent.springBootDemo.entity.UserExample;
import org.cent.springBootDemo.mapper.UserMapper;
import org.cent.springBootDemo.service.UserService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User服务接口实现类
 * Created by cent on 2016/9/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public User queryById(Integer id) throws Exception {
        UserMapper userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        return userMapper.selectByPrimaryKey(id);
    }

    public PageInfo<User> selectPage(int start,int limit) throws Exception {
        UserMapper userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        PageHelper.startPage(start,limit);
        UserExample ue = new UserExample();
        PageInfo<User> users = new PageInfo<User>(userMapper.selectByExample(ue));
        return users;
    }
}
