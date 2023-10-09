package com.idcpj.change_export;

import com.intellij.openapi.ListSelection;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vfs.*;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class ChangelogExportAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent event) {
        //获取当前项目
        Project project = event.getProject();
        if (project==null){
            return;
        }


        List<VirtualFile> selectedList = new ArrayList<>();

        // 获取当前选中的changelist
        // todo 目前无法实现

        // methods one
//        Iterable<VirtualFile> data = event.getData(VcsDataKeys.VIRTUAL_FILES);
//        if (data==null){
//            return;
//        }
//        for (VirtualFile file : data) {
//            selectedList.add(file);
//        }

//         methods two
        ListSelection<Change> data = event.getData(VcsDataKeys.CHANGES_SELECTION);
        if (data==null){
            return;
        }
        for (Change change : data.getList()) {
            selectedList.add(change.getVirtualFile());

        }

        if (selectedList.isEmpty()){
            return;
        }

        // 弹出文件选择器，选择目标路径
        VirtualFile[] selectedFiles = FileChooser.chooseFiles(
                FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                project,
                null
        );
        if (selectedFiles.length == 0) {
            return;
        }
        VirtualFile targetDirectory = selectedFiles[0];

        //创建时间目录
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        String projectName = project.getName();
        String createProjectDir = targetDirectory.getPath()+"/"+ projectName +"_"+dateFormat.format(date)+"/";

        try {


            for (VirtualFile file : selectedList) {
                String relativePath = getRelativePath(project, file);
                File exportDir = new File(createProjectDir + relativePath).getParentFile();
                if (!exportDir.exists()) {
                    boolean created = exportDir.mkdirs();
                    if (!created) {
                        throw new RuntimeException("Failed to create directory: " + exportDir.getPath());
                    }
                }

                Path target =  Paths.get(createProjectDir + relativePath);
                Path source  =  Paths.get(file.getPath());
                Files.copy(source,target);

            }

            // 创建并写入path.txt
            String path = createProjectDir + "/"+dateFormat.format(date)+"_path.txt";
            FileWriter writer = new FileWriter(path);

            for (VirtualFile file : selectedList) {

                String filePath = getRelativePath(project,file);
                writer.write(filePath + "\n");

            }
            writer.close();

            // 提示导出成功
            int result = Messages.showYesNoDialog("Open this directory: "+createProjectDir+" ?", "Confirmation", Messages.getQuestionIcon());
            if (result==Messages.YES){
                Desktop.getDesktop().open(new File(createProjectDir));
            }


        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog("Export changelist failed: " + e.getMessage(), "Error");
        }
    }


    private String getRelativePath(Project project, VirtualFile file) {
        String basePath = project.getBasePath();
        if (basePath == null) {
            throw new RuntimeException("Failed to get project base path");
        }
        String filePath = file.getPath();
        return filePath.startsWith(basePath + "/") ? filePath.substring(basePath.length() + 1) : filePath;
    }

}
