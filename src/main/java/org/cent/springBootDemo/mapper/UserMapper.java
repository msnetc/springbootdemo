package org.cent.springBootDemo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.cent.springBootDemo.entity.User;
import org.cent.springBootDemo.entity.UserExample;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @SelectProvider(type=UserSqlProvider.class, method="countByExample")
    long countByExample(UserExample example);

    @DeleteProvider(type=UserSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserExample example);

    @Delete({
        "delete from sys_user",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into sys_user (ID, USER_CODE, ",
        "USER_NAME, REMARK, ",
        "CREATE_TIME, UPDATE_TIME)",
        "values (#{id,jdbcType=INTEGER}, #{userCode,jdbcType=VARCHAR}, ",
        "#{userName,jdbcType=VARCHAR}, #{remark,jdbcType=VARCHAR}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    int insertSelective(User record);

    @SelectProvider(type=UserSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="USER_CODE", property="userCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="USER_NAME", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="REMARK", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATE_TIME", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<User> selectByExample(UserExample example);

    @Select({
        "select",
        "ID, USER_CODE, USER_NAME, REMARK, CREATE_TIME, UPDATE_TIME",
        "from sys_user",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="USER_CODE", property="userCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="USER_NAME", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="REMARK", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATE_TIME", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    User selectByPrimaryKey(Integer id);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update sys_user",
        "set USER_CODE = #{userCode,jdbcType=VARCHAR},",
          "USER_NAME = #{userName,jdbcType=VARCHAR},",
          "REMARK = #{remark,jdbcType=VARCHAR},",
          "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
          "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP}",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(User record);
}