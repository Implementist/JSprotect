package com.rocky.db;

import java.sql.Timestamp;

/**
 * Created by Implementist on 2017.06.01
 */
public class ProjectInfo {
    private String username;
    private int projectId;
    private int antidbg;
    private int obfuscation;
    private int antiTamper;
    private Timestamp date;
    private String obfuscationStrength;
    private int flatternCount;
    private int opaqueCount;
    private Boolean runnable;
    private String fileName;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAntidbg() {
        return antidbg;
    }

    public void setAntidbg(int antidbg) {
        this.antidbg = antidbg;
    }

    public int getObfuscation() {
        return obfuscation;
    }

    public void setObfuscation(int obfuscation) {
        this.obfuscation = obfuscation;
    }

    public int getAntiTamper() {
        return antiTamper;
    }

    public void setAntiTamper(int antiTamper) {
        this.antiTamper = antiTamper;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getObfuscationStrength() {
        return obfuscationStrength;
    }

    public void setObfuscationStrength(String obfuscationStrength) {
        this.obfuscationStrength = obfuscationStrength;
    }

    public int getFlatternCount() {
        return flatternCount;
    }

    public void setFlatternCount(int flatternCount) {
        this.flatternCount = flatternCount;
    }

    public int getOpaqueCount() {
        return opaqueCount;
    }

    public void setOpaqueCount(int opaqueCount) {
        this.opaqueCount = opaqueCount;
    }

    public Boolean getRunnable() {
        return runnable;
    }

    public void setRunnable(Boolean runnable) {
        this.runnable = runnable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
