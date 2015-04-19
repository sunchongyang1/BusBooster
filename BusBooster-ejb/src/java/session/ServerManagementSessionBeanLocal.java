/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Output;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface ServerManagementSessionBeanLocal {
    public Boolean startServer();
    public Boolean stopServer();
    public List<Output> getOutput();
}
