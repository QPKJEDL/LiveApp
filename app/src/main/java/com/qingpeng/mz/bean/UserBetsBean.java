package com.qingpeng.mz.bean;

public class UserBetsBean {

    /**
     * data : {"Double":97,"Equal":97,"SuperDouble":97}
     * info : 用户下注费率!
     * status : 1
     */
    /**
     * Double : 97
     * Equal : 97
     * SuperDouble : 97
     */

    private double Double;
    private double Equal;
    private double SuperDouble;

    public double getBanker() {
        return banker;
    }

    public void setBanker(double banker) {
        this.banker = banker;
    }

    public double getPlayer() {
        return player;
    }

    public void setPlayer(double player) {
        this.player = player;
    }

    public double getPlayerPair() {
        return playerPair;
    }

    public void setPlayerPair(double playerPair) {
        this.playerPair = playerPair;
    }

    public double getBankerPair() {
        return bankerPair;
    }

    public void setBankerPair(double bankerPair) {
        this.bankerPair = bankerPair;
    }

    private double dragon;
    private double tie;
    private double tiger;

    private double banker;
    private double player;
    private double playerPair;
    private double bankerPair;

    public double getDouble() {
        return Double;
    }

    public void setDouble(double aDouble) {
        Double = aDouble;
    }

    public double getEqual() {
        return Equal;
    }

    public void setEqual(double equal) {
        Equal = equal;
    }

    public double getSuperDouble() {
        return SuperDouble;
    }

    public void setSuperDouble(double superDouble) {
        SuperDouble = superDouble;
    }

    public double getDragon() {
        return dragon;
    }

    public void setDragon(double dragon) {
        this.dragon = dragon;
    }

    public double getTie() {
        return tie;
    }

    public void setTie(double tie) {
        this.tie = tie;
    }

    public double getTiger() {
        return tiger;
    }

    public void setTiger(double tiger) {
        this.tiger = tiger;
    }
}
