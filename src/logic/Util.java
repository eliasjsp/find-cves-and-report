/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Access;
import model.AttackWay;
import model.CVE;
import model.Impact;
import projetoac.Main;

/**
 *
 * @author Elias
 */
public abstract class Util {
    
    private static List<CVE> tempcves;
    private static final String URL_API_VENDORS = "http://cve.circl.lu/api/browse";
    private static final String URL_API_SEARCH = "http://cve.circl.lu/api/search";
    private static final String URL_API_CVE = "http://cve.circl.lu/api/cve/";
    
    private static final String CPE_TEXT = "vulnerable_configuration";
    private static final String CPE_TEXTV2 = "vulnerable_configuration_cpe_2_2";
    
    
    
    
    public static List<CVE> getCvesDetails(List<CVE> cves, ProgressUpdateListener progressListener) {
        List<CVE> completedCves = veryfyCves(cves);
        int progress = 0;
        System.out.println("CVES size: " + cves.size());
        for (CVE cve : cves) {
            if (!completedCves.contains(cve))
                try {
                    JsonObject cveDetails = getData(URL_API_CVE + cve.getId()).getAsJsonObject();
                    JsonObject impact = cveDetails.getAsJsonObject("impact");
                    JsonObject access = cveDetails.getAsJsonObject("access");

                    CVE newCve = getCveFromJson(cve, access, cveDetails.get("cvss"), cveDetails.get("cwe"), impact);

                    JsonArray capec = cveDetails.getAsJsonArray("capec");
                    newCve.setAttackWays(getCapecFromJSon(capec));
                    completedCves.add(newCve);

                    progress++;
                    if (progressListener.onProgressUpdate(progress))
                            return null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        
        return completedCves;
    }

    private static List<AttackWay> getCapecFromJSon(JsonArray capec) {
        List<AttackWay> attackWays = new ArrayList<>();
        if(capec != null)
            capec.forEach(c -> {
                AttackWay aw = new AttackWay();
                aw.setName(c.getAsJsonObject().get("name").getAsString());
                c.getAsJsonObject().getAsJsonArray("solutions").forEach(s -> {
                    aw.setSolution((aw.getSolution() == null ? "" : aw.getSolution()) + s.getAsString() + "<br>");
                });
                c.getAsJsonObject().getAsJsonArray("summary").forEach(s -> {
                    aw.setSummary((aw.getSummary()==null?"":aw.getSummary()) + s.getAsString() + "<br>");
                });
                attackWays.add(aw);
            });
        return attackWays;
    }
    public static boolean haveCveOnJson(String id){
        return (new File("database/"+id+".json").exists());
    }
    private static CVE getCveFromJson(CVE cve, JsonObject access, JsonElement cvss, JsonElement cwe, JsonObject impact) {
        CVE newCve = new CVE();
        newCve.setId(cve.getId());
        newCve.setVendor(cve.getVendor());
        newCve.setProduct(cve.getProduct());
        newCve.setSearchedVersion(cve.getSearchedVersion());
        newCve.setCpe(cve.getCpe());
        try {
            newCve.setAccess(new Access(access.get("authentication").getAsString(), access.get("complexity").getAsString(), access.get("vector").getAsString()));
        } catch (Exception e) {}
        try {
            newCve.setCvss(cvss.getAsFloat());
        } catch (Exception e) {}
        try {
            newCve.setCwe(cwe.getAsString());
        } catch (Exception e) {}
        try {
            newCve.setImpact(new Impact(impact.get("availability").getAsString(), impact.get("confidentiality").getAsString(), impact.get("integrity").getAsString()));
        } catch (Exception e) {}
            
        return newCve;
    }
    
    public static ArrayList<String> getVendors() throws IOException {
        ArrayList<String> vs = new ArrayList<>();
        vs.add("Vendors");
        JsonArray vendors = getData(URL_API_VENDORS).getAsJsonObject().getAsJsonArray("vendor");
        vendors.forEach(x -> vs.add(x.getAsString()));
        return vs;
    }
    
    public static ArrayList<String> getProducts(String vendor) throws IOException {
        ArrayList<String> ps = new ArrayList<>();
        ps.add("Products");
        JsonArray products = getData(URL_API_VENDORS+"/"+vendor).getAsJsonObject().getAsJsonArray("product");
        products.forEach(x -> ps.add(x.getAsString()));
        return ps;
    }
    public static ArrayList<String> getCvesByProduct(String vendor, String product) throws IOException {
        ArrayList<String> cps = new ArrayList<>();
        tempcves = new ArrayList<>();
        cps.add("Versions");
        JsonArray products = getData(URL_API_SEARCH + "/" + vendor + "/" + product).getAsJsonArray();
        products.forEach((obj) -> {
            String cve = obj.getAsJsonObject().get("id").getAsString();
            CVE cveObj = new CVE(cve, product, vendor);
            getCpesFromApi(obj, cveObj, cps, CPE_TEXT);
            getCpesFromApi(obj, cveObj, cps, CPE_TEXTV2);
            tempcves.add(cveObj);
        });
        return cps;
    }

    private static void getCpesFromApi(JsonElement obj, CVE cveObj, ArrayList<String> cps, String cpeVersion) {
        obj.getAsJsonObject().get(cpeVersion).getAsJsonArray().forEach(cpe -> {
            String version = CVE.getVersion(cpe.getAsString());
            if (!version.isEmpty()){
                if(!haveThisVersionInsideCpe(cveObj.getCpe(),cpe.getAsString()))
                    cveObj.addCpe(cpe.getAsString());
                if(!cps.contains(version))
                    cps.add(version);
            }
        });
    }
    private static boolean haveThisVersionInsideCpe(List<String> cpes, String cpeToFind) {
        boolean flag = false;
        for(String s: cpes){
            if(s.equals(cpeToFind) || CVE.getVersion(s).equals(CVE.getVersion(cpeToFind))){
                flag = true;
                System.out.println(CVE.getVersion(s));
                System.out.println(CVE.getVersion(cpeToFind));
            }
        }
        return flag;
    }
    private static JsonElement getData(String urlStr) throws IOException {
        System.out.println(urlStr);
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser(); 
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); 
        return root;
    }  
    
    public static List<CVE> getCves() {
        return tempcves;
    }
    private static JsonElement getDatabase(String file) {
        JsonParser jp = new JsonParser();
        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            return null;
        }
        Reader inputStreamReader = new InputStreamReader(is);
        return jp.parse(inputStreamReader);
    }
    private static ArrayList<CVE> veryfyCves(List<CVE> cves) {
        Gson gson = new Gson();
        ArrayList<CVE> cvesAux = new ArrayList<>();
        cves.stream().forEach((item) -> {
            JsonElement validate = getDatabase("database/"+item.getId()+".json");
            JsonObject c = validate == null ? null : validate.isJsonObject() ? validate.getAsJsonObject() : null;
            if(c!=null){
                cvesAux.add(gson.fromJson(c, CVE.class));
            }
        });
        return cvesAux;
    }

    public static boolean getReport(String arg) {
        JsonElement validate = getDatabase(arg);
        JsonArray c = validate == null ? null : validate.isJsonArray() ? validate.getAsJsonArray() : null;
        Main m = new Main(new ArrayList<CVE>());
        List<String> vendors = new ArrayList<>();
        try {
            vendors = getVendors();
        }catch(Exception e) {
            System.err.println("Server fail or no internet connection! " + e.getMessage());
        }
        if(c != null) {
            for(JsonElement vulns: c) {
                CVE cve = new CVE(vulns.getAsJsonObject().get("vendor").getAsString(), vulns.getAsJsonObject().get("product").getAsString());
                cve.setSearchedVersion(vulns.getAsJsonObject().get("version").getAsString());
                try {
                    if(vendors.contains(cve.getVendor())) {
                        try {
                            if(!getProducts(cve.getVendor()).contains(cve.getProduct()))
                                throw new Exception();
                            getCvesByProduct(cve.getVendor(), cve.getProduct());
                            m.addToCves(cve);
                        } catch (Exception e) {
                            System.err.println("This vendor " + cve.getVendor() + " does not have this product: " + cve.getProduct()); 
                        }
                    } else {
                        System.err.println("Vendor does not exists: " + cve.getVendor());
                    }
                } catch (Exception ex) {}
                
            }
            m.generateReport();
        }
        
        return c != null;
    }

    
    
   
    
    
}
