/*
 * Programing exam
 */
package dto.demo;

import entities.Role;
import entities.User;

/**
 *
 * @author magda
 */
public class UserDTO {
    String username;
    boolean isUser=false;
    boolean isAdmin=false;

    public UserDTO(User user) {
        this.username = user.getUserName();
        checkRoles(user);
        
    }
    private void checkRoles(User user){
        for (Role role :user.getRoleList()) {
            if(role.getRoleName().equals("user")){
                this.isUser=true;
            }
            if(role.getRoleName().equals("admin")){
                this.isAdmin=true;
            }
        }
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", isUser=" + isUser + ", isAdmin=" + isAdmin + '}';
    }
    
    
    
}
