/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bus;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface SimulationSessionBeanLocal {
    public Boolean createNewBus(String busNo, String direction, List<Integer> dwells, List<Integer> speeds);
}
