/*
 * Programing exam
 */
package dto;

import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;

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

    public String getUsername() {
        return username;
    }

    public boolean isIsUser() {
        return isUser;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", isUser=" + isUser + ", isAdmin=" + isAdmin + '}';
    }
    
    public List<String> getRoles(){
        List<String> roles=new ArrayList();
        if(isAdmin==true){roles.add("admin");}
        if(isUser==true){roles.add("user");}
        return roles;
        
    }
    
    
}
