/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elias
 */
public class CVE {
    private static final int CPE_VERSION = 5;
    
    private String id;
    private String product;
    private String vendor;
    private float cvss;
    private String cwe;
    private Impact impact;
    private Access access;
    private String searchedVersion;
    private List<String> cpe;
    private List<AttackWay> attackWays;

    public CVE() {
        this.cpe = new ArrayList<>();
        this.attackWays = new ArrayList<>();
    }
    public CVE(String vendor, String product) {
        this.product = product;
        this.vendor = vendor;
        this.cpe = new ArrayList<>();
        this.attackWays = new ArrayList<>();
    }
    public CVE(String cve, String product, String vendor) {
        this.id = cve;
        this.product = product;
        this.vendor = vendor;
        this.cpe = new ArrayList<>();
        this.attackWays = new ArrayList<>();
    }

    public CVE(String id, String product, String vendor, float cvss, String cwe, Impact impact, Access access, List<String> cpe, List<AttackWay> attackWays) {
        this.id = id;
        this.product = product;
        this.vendor = vendor;
        this.cvss = cvss;
        this.cwe = cwe;
        this.impact = impact;
        this.access = access;
        this.cpe = cpe;
        this.attackWays = attackWays;
    }
    
    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public float getCvss() {
        return cvss;
    }

    public void setCvss(float cvss) {
        this.cvss = cvss;
    }

    public String getCwe() {
        return cwe;
    }

    public void setCwe(String cwe) {
        this.cwe = cwe == null ? "": cwe;
    }

    public Impact getImpact() {
        return impact;
    }

    public void setImpact(Impact impact) {
        this.impact = impact;
    }

    public List<AttackWay> getAttackWays() {
        return attackWays;
    }

    public void setAttackWays(List<AttackWay> attackWays) {
        this.attackWays = attackWays;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String cve) {
        this.id = cve;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public List<String> getCpe() {
        return cpe;
    }

    public void addCpe(String version) {
        this.cpe.add(version);
    }
    
    public void addAttackWay(AttackWay aw) {
        this.attackWays.add(aw);
    }

    public String getSearchedVersion() {
        return searchedVersion;
    }

    public void setSearchedVersion(String searchedVersion) {
        this.searchedVersion = searchedVersion;
    }
    
    @Override
    public String toString() {
        return "Product: " + product + " --> Version: " + searchedVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CVE))
                return false;
		
        CVE cveAux = (CVE) obj;
        return cveAux.getId().equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    public static String getVersion (String cpe) {
        try {
            return cpe.split(":")[CPE_VERSION];
        } catch (Exception e) {
            try {
                return cpe.split(":")[CPE_VERSION-1];
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public void setCpe(List<String> cpe) {
        this.cpe = cpe;
    }
    
    
}
