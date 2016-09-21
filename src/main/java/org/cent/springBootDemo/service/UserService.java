package org.cent.springBootDemo.service;

import com.github.pagehelper.PageInfo;
import org.cent.springBootDemo.entity.User;

/**
 * User服务接口
 * Created by cent on 2016/9/20.
 */
public interface UserService {
    /**
     * 根据ID查找
     * @return
     * @throws Exception
     */
    public User queryById(Integer id) throws Exception;

    /**
     * 获取分页数据
     * @param start
     * @param limit
     * @return
     * @throws Exception
     */
    public PageInfo<User> selectPage(int start,int limit) throws Exception;
}
