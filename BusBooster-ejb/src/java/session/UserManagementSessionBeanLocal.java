/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface UserManagementSessionBeanLocal {
    public Boolean registration(String username, String password, String passwordCheck, String email);
}
