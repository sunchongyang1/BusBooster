/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.*;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chongyangsun
 */
@Stateless
public class FeedbackManagementSessionBean implements FeedbackManagementSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Feedback createFeedback(Long userId, Long busId, Boolean busBreakDown, Boolean trafficJam, Integer delay) {
        User user = em.find(User.class, userId);
        Bus bus = em.find(Bus.class, busId);
        Feedback feedback = new Feedback(userId, busId, bus.getBusNo(), bus.getDirection(), bus.getLatitude(), bus.getLongitude());
        em.persist(feedback);
        feedback.setIsBusBreakDown(busBreakDown);
        feedback.setTrafficJam(trafficJam);
        feedback.setDelay(delay);
        em.merge(feedback);
        return feedback;
    }
    
    @Override
    public Integer getDelayFromFeedback(Long busId) {
        Query q = em.createQuery("SELECT f FROM Feedback f WHERE f.busId=:id");
        q.setParameter("id", busId);
        List<Feedback> feedbackList = new ArrayList(q.getResultList());
        if(feedbackList.isEmpty()) {
            System.out.println("Bus delay: N.A.");
            return 0;
        } else {
            int sum = 0;
            for(Feedback f: feedbackList) {
                sum += f.getDelay();
            }
            return sum/feedbackList.size();
        }
    }
    
    @Override
    public Boolean isBusBreakDown(Long busId) {
        Query q = em.createQuery("SELECT f FROM Feedback f WHERE f.busId=:id");
        q.setParameter("id", busId);
        List<Feedback> feedbackList = new ArrayList(q.getResultList());
        if(feedbackList.isEmpty()) {
            System.out.println("Bus condition: normal.");
            return Boolean.FALSE;
        } else {
            for(Feedback f: feedbackList) {
                if(f.getIsBusBreakDown()) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }
    }
}
