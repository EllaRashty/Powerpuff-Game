package com.example.powerpuff_game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopTen implements Serializable {
    private final int SIZE = 10;
    private List<Leader> leaders;

    public TopTen() {
        leaders = new ArrayList<>();
    }

    public TopTen(ArrayList<Leader> leaders) {
        this.leaders = leaders;
    }

    public List<Leader> getLeaders() {
        sortLeaders();
        return leaders;
    }

    public void sortLeaders() {
        Collections.sort(leaders);
    }

    public TopTen setLeaders(ArrayList<Leader> leaders) {
        this.leaders = leaders;
        return this;
    }

    public Boolean addLeader(Leader leader) {
        sortLeaders();
        if (isFull()) {
            if (checkLast(leader) <= 0) {
                removeLastLeader();
                this.leaders.add(leader);
                return true;
            }
        } else {
            this.leaders.add(leader);
            return true;
        }
        return false;
    }

    private void removeLastLeader() {
        leaders.remove(SIZE - 1);
    }

    public int checkLast(Leader leader) {
        return leader.compareTo(leaders.get(leaders.size() - 1));
    }

    public boolean isFull() {
        if (leaders.size() < SIZE)
            return false;
        return true;
    }
}
