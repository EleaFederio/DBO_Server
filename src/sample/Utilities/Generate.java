package sample.Utilities;

import sample.Systems.LoginController;

public class Generate {

    public String feesTableName(String toBeGenerate){
        String tableName;
        String tempTableName = toBeGenerate + "_fees_" + LoginController.userCourse;
        String subTableName = tempTableName.replace(" ", "");
        tableName = subTableName.replace("-", "_");
        return tableName;
    }

    public String eventsTableName(String toBeGenerate){
        String tableName;
        String tempTableName = toBeGenerate + "_events_" + LoginController.userCourse;
        String subTableName = tempTableName.replace(" ", "");
        tableName = subTableName.replace("-", "_");
        return tableName;
    }


}
