/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Elias
 */
public class AttackWay {
    private String name;
    private String summary;
    private String solution;

    public AttackWay() {
    }

    public AttackWay(String name, String summary, String solution) {
        this.name = name;
        this.summary = summary;
        this.solution = solution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "<b>Name: </b>" + name + "<br><b>Summary: </b><br>" + summary + "<br><b>Solutions:</b><br>" + solution ;
    }
    
    
}
