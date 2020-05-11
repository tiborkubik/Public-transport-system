package ija.proj;

import java.time.LocalTime;

/***
 * Interface UpdateState specifies all elements which are going to be updated
 */
public interface UpdateState {
    /***
     * Updates Javafx elements which has type UpdateState
     *
     * @param time current time of the application
     * @param speedMultiplier if the time goes faster than normal
     * @param trafficCoefficient level of traffic jam
     */
    void update(LocalTime time, double speedMultiplier, int trafficCoefficient);
}
