package com.rocky.adbProject;

import com.rocky.db.ProjectInfo;
import com.rocky.db.ProjectInfoDAO;
import com.rocky.protect.Protection;

import java.io.File;
import java.util.ArrayList;


public class Project {

    private ArrayList<String> libNames;

    public Project() {
        this.initialLibNames();

    }

    private void initialLibNames() {
        this.libNames = new ArrayList<String>();
        this.libNames.add("jquery");
        this.libNames.add("bootstrap");
    }

    public int getProjectId(String username) {
        int currentCount = ProjectInfoDAO.queryMaxByUsername(username);
        return currentCount + 1;
    }

    public ArrayList<Integer> ProcessProject(ArrayList<String> filesNeedProcess, int flatternStrength, int opaqueStrength) {

        ArrayList<Integer> strengthTotal = new ArrayList<Integer>();
        ArrayList<Integer> strength;
        strengthTotal.add(0);
        strengthTotal.add(0);

        int len = filesNeedProcess.size();
        for (String fileItemName : filesNeedProcess) {
            // 获得每个文件的扩展名
            String extension = fileItemName.substring(fileItemName.lastIndexOf('.') + 1);

            // 若该文件为js文件，并且不属于类似JQuery等库文件
            if (checkExtension(extension) && checkLibrary(fileItemName)) {
                //this.antidbg.injectAntiDbg(fileItemName);
                Protection protection = new Protection();
                strength = protection.protect(fileItemName, flatternStrength, opaqueStrength);
                int str1 = strengthTotal.get(0) + strength.get(0);
                int str2 = strengthTotal.get(1) + strength.get(1);
                strengthTotal.clear();
                strengthTotal.add(str1);
                strengthTotal.add(str2);
            }
        }
        return strengthTotal;
    }

    public boolean checkExtension(String extension) {
        return extension.equals("js");
    }

    public boolean checkLibrary(String fileName) {
        for (String libName : libNames)
            if (fileName.contains(libName))
                return false;

        return true;
    }

    public void deleteDirectory(String directoryPath) {
        File file = new File(directoryPath);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            int len = list.length;

            for (File aList : list) aList.delete();
        }
        file.delete();
    }

    public ProjectInfo getOneProjectInfo(String username, int projectID) {
        return ProjectInfoDAO.queryProjectInfoByUsernameAndProjectId(username, projectID);
    }

    public void deleteFiles(String path) {
        File file = new File(path);

        if (!file.exists() || !file.isDirectory())
            return;

        String[] files = file.list();

        for (int i = 0; i < files.length; ++i) {
            String filePath = file.getAbsolutePath() + File.separator + files[i];
            File newfile = new File(filePath);
            if (newfile.isDirectory()) {
                deleteFiles(filePath);
                newfile.delete();
            } else {
                newfile.delete();
            }
        }
        file.delete();
    }

}