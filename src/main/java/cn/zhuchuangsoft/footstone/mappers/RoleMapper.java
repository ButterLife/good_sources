package cn.zhuchuangsoft.footstone.mappers;

import cn.zhuchuangsoft.footstone.entity.user.Role;
import cn.zhuchuangsoft.footstone.entity.user.RoleAndUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {

    @Select("select r.ID,r.ROLE_NAME roleName,r.ROLE_CODE roleCode " +
            "from role r,user_role ur " +
            "where r.id=ur.role_id and ur.user_id=#{uid}")
    List<Role> findByUid(Integer uid);

    @Insert("Insert into user_role(role_id,user_id)values(#{roleId},#{userId})")
    Integer saveRoleAndUser(RoleAndUser roleAndUser);

    @Select("select * from role where ID=#{id}")
    Role findById(Integer id);


}
