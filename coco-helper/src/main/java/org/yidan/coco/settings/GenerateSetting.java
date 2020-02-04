package org.yidan.coco.settings;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kongxiangxin on 2017/8/1.
 */
public class GenerateSetting {
    private JdbcProperty jdbcProperty;

    private String tablePrefix;
    private boolean truncatePrefix;
    private String tableName;
    private Set<String> excludeTableSet = new HashSet<>();
    private Set<String> excludeColumnSet = new HashSet<>();

    private Map<JDBCType, String> typeMapping = new HashMap<>();

    /**
     * 如果我们把某个大表的数据，分割到多张表中，可利用tableNameMapping和classNameMapping
     * 例如，订单数据量太大，根据订单号hash到10张表里，表名可能是这个样子：order_01、order_02、...、order_10，这10张表的结构完全相同。
     * 我们可以指定tableName，只利用其中一张表作为模型来生成代码，但是如果在模板中不特殊处理的话，生成的实体类名是Order01，生成的sql中的表名是order_01。这显然不是我们想要的结果。
     */
    private Map<String, String> tableNameMapping = new HashMap<>();
    private Map<String, String> classNameMapping = new HashMap<>();


    public JdbcProperty getJdbcProperty() {
        return jdbcProperty;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public Set<String> getExcludeTables() {
        return excludeTableSet;
    }

    public Set<String> getExcludeColumns() {
        return excludeColumnSet;
    }


    public boolean isValidTable(String table){
        if(excludeTableSet.contains(table)){
            return false;
        }
        return true;
    }

    public boolean isValidColumn(String column){
        if(excludeColumnSet.contains(column)){
            return false;
        }
        return true;
    }

    public Map<JDBCType, String> getTypeMapping(){
        return typeMapping;
    }

    public Map<String, String> getTableNameMapping() {
        return tableNameMapping;
    }

    public Map<String, String> getClassNameMapping() {
        return classNameMapping;
    }

    public boolean truncatePrefix() {
        return truncatePrefix;
    }

    public String getTableName() {
        return tableName;
    }


    public void setJdbcProperty(JdbcProperty jdbcProperty) {
        this.jdbcProperty = jdbcProperty;
    }

    public void setTruncatePrefix(boolean truncatePrefix) {
        this.truncatePrefix = truncatePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
