package ija.proj;

import java.time.LocalTime;

public interface UpdateState {
    void update(LocalTime time, double speedMultiplier, int trafficCoefficient);

}
