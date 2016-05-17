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
public class Access {
    
    private String authencitcation;
    private String complexity;
    private String vector;

    public Access() {
    }

    public Access(String authencitcation, String complexity, String vector) {
        this.authencitcation = authencitcation;
        this.complexity = complexity;
        this.vector = vector;
    }

    public String getAuthencitcation() {
        return authencitcation;
    }

    public void setAuthencitcation(String authencitcation) {
        this.authencitcation = authencitcation;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "<b>Complexity: </b>" + complexity + "<br><b>Authencitcation: </b> " + authencitcation + "<br><b>Vector: </b> " + vector; 
    }
    
    
}
