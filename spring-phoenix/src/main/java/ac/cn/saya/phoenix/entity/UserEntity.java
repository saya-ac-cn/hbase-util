package ac.cn.saya.phoenix.entity;

/**
 * @author saya
 * @title: UserEntity
 * @projectName hbase-util
 * @description: TODO
 * @date: 2022/8/29 20:30
 * @description:
 */

public class UserEntity{

   private String id;

   private String name;

   private String addr;

   public UserEntity() {
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAddr() {
      return addr;
   }

   public void setAddr(String addr) {
      this.addr = addr;
   }
}
