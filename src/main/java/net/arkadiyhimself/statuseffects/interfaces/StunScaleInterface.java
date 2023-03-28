package net.arkadiyhimself.statuseffects.interfaces;

public interface StunScaleInterface {
    boolean isStunned();
    int MAX_STUN_POINTS = 0;
    int CURRENT_STUN_POINTS = 0;
    int getCurrentStunPoints();
    int getMaxStunPoints();

    void addStunPoints(int amount);
}
