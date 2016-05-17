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
public class Impact {
    
    private String availability;
    private String confidentiality;
    private String integrity;

    public Impact() {
    }

    public Impact(String availability, String confidentiality, String integrity) {
        this.availability = availability;
        this.confidentiality = confidentiality;
        this.integrity = integrity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getIntegrity() {
        return integrity;
    }

    public void setIntegrity(String integrity) {
        this.integrity = integrity;
    }

    @Override
    public String toString() {
        return "<b>Availability: </b>" + availability + "<br>" + "<b>Confidentiality: </b>" + confidentiality + "<br>" + "<b>Integrity: </b>" + integrity + "<br>";
    }
    
    
}
