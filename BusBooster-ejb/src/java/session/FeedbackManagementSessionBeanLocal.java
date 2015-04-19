/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Feedback;
import javax.ejb.Local;

/**
 *
 * @author chongyangsun
 */
@Local
public interface FeedbackManagementSessionBeanLocal {
    public Feedback createFeedback(Long userId, Long busId, Boolean busBreakDown, Boolean trafficJam, Integer delay, String comment);
    public Integer getDelayFromFeedback(Long busId);
    public Boolean isBusBreakDown(Long busId);
}
