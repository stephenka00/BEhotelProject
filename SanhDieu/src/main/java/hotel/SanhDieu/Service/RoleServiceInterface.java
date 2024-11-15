package hotel.SanhDieu.Service;

import hotel.SanhDieu.Model.Role;
import hotel.SanhDieu.Model.User;

import java.util.List;

public interface RoleServiceInterface {
    List<Role> getRoles();

    Role createRole(Role theRole);

    void deleteRole(Long roleId);

    Role findByName(String name);

    User removeUserFromRole(Long userId,Long roleId);

    User assignRoleToUser(Long userId,Long roleId);

    Role removeAllUsersFromRole(Long roleId);
}
