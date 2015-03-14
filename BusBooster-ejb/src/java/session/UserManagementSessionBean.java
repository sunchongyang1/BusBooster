/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

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
    public Boolean registration(String username, String password, String passwordCheck, String email) {
        return false;
    }
}
