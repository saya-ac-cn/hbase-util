package ac.cn.saya.phoenix.persistent;

import ac.cn.saya.phoenix.entity.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author saya
 * @title: UserDao
 * @projectName hbase-util
 * @description: TODO
 * @date: 2022/8/29 20:29
 * @description:
 */

@Mapper
public interface UserDao{

   @Select("SELECT * from business.student")
   List<UserEntity> queryAll();

   @Insert("UPSERT INTO business.student VALUES( #{id}, #{name}, #{addr} )")
   void save(UserEntity user);

   @Select("SELECT * FROM business.student WHERE id=#{id}")
   UserEntity queryById(String id);

   @Delete("DELETE FROM business.student WHERE id=#{id}")
   void deleteById(String id);

}
