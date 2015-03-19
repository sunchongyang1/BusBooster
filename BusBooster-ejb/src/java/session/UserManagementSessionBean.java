/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class UserManagementSessionBean implements UserManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public User registration(String username, String password, String passwordCheck, String email) {
        if(password.equals(passwordCheck)) {
            try {
                User user = new User(username, email, password);
                em.persist(user);
                return user;
            } catch(Exception e) {
                System.out.println("Problem signing up");
                return null;
            }
            
        } else {
            return null;
        }
    }
}
