package org.cent.springBootDemo.controller;


import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cent.springBootDemo.constants.PageConstants;
import org.cent.springBootDemo.entity.User;
import org.cent.springBootDemo.properties.DataSourceProperties;
import org.cent.springBootDemo.service.UserService;
import org.cent.springBootDemo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User Demo Controller
 * Created by cent on 2016/9/2.
 */
@RestController
@RequestMapping(value="/user")
public class UserController extends BaseController {

    Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public User user(@PathVariable("id") Integer id) {
        try {
            User user = userService.queryById(id);
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new User();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public PageInfo<User> user(HttpServletRequest request) {
        try {
            Map<String, String> params = getRequestParams(request);
            Integer start = StringUtil.isBlank(params.get("start")) ? PageConstants.DEFAULT_START_PAGE : Integer.valueOf(params.get("start"));
            Integer limit = StringUtil.isBlank(params.get("limit")) ? PageConstants.DEFAULT_PAGE_LIMIT : Integer.valueOf(params.get("limit"));

            PageInfo<User> users = userService.selectPage(start, limit);
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }
}
