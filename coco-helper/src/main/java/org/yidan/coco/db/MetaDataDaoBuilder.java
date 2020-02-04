package org.yidan.coco.db;

import org.yidan.coco.settings.GenerateSetting;
import org.yidan.coco.settings.JdbcProperty;

import java.sql.JDBCType;
import java.sql.SQLException;

public class MetaDataDaoBuilder {
    private GenerateSetting setting = new GenerateSetting();

    public MetaDataDaoBuilder jdbcProperty(JdbcProperty property){
        setting.setJdbcProperty(property);
        return this;
    }

    /**
     * 表名前缀，以此前缀开头的表，才会被读入模型中，当tableName未指定时才有效。如果tablePrefix为空并且tableName为空，会读取所有表。
     * @param tablePrefix
     * @return
     */
    public MetaDataDaoBuilder tablePrefix(String tablePrefix){
        setting.setTablePrefix(tablePrefix);
        return this;
    }

    /**
     * 是否自动去掉表前缀。 当指定了tablePrefix才有效。 如果为true，自动读入的模型名称会自动去除掉tablePrefix
     * @param truncatePrefix
     * @return
     */
    public MetaDataDaoBuilder truncatePrefix(boolean truncatePrefix){
        setting.setTruncatePrefix(truncatePrefix);
        return this;
    }

    /**
     * 指定表名，会忽略tablePrefix值，只把该表读入模型中
     * @param tableName
     * @return
     */
    public MetaDataDaoBuilder tableName(String tableName){
        setting.setTableName(tableName);
        return this;
    }

    /**
     * 排除掉的表名。排除掉的表，不会读到模型中。
     * @param tableName
     * @return
     */
    public MetaDataDaoBuilder excludeTable(String tableName){
        setting.getExcludeTables().add(tableName);
        return this;
    }

    /**
     * 排除掉的列名。排除掉的列，不会读到模型中。
     * @param columnName
     * @return
     */
    public MetaDataDaoBuilder excludeColumn(String columnName){
        setting.getExcludeColumns().add(columnName);
        return this;
    }

    /**
     * jdbc的TYPE_NAME -> pojo类型映射。例如BIT -> Boolean，表示要把JDBCType的BIT类型，映射为Boolean类型。
     * @param jdbcType JDBCType类型
     * @param javaType
     * @return
     */
    public MetaDataDaoBuilder typeMapping(JDBCType jdbcType, String javaType){
        setting.getTypeMapping().put(jdbcType, javaType);
        return this;
    }

    /**
     * 如果我们把某个大表的数据，分割到多张表中，可利用tableNameMapping和classNameMapping。
     * 例如，订单数据量太大，根据订单号hash到10张表里，表名可能是这个样子：order_01、order_02、...、order_10，这10张表的结构完全相同。
     * 我们可以指定tableName，只利用其中一张表作为模型来生成代码，但是如果在模板中不特殊处理的话，生成的实体类名是Order01，生成的sql中的表名是order_01。这显然不是我们想要的结果。
     * @param rawTableName 数据库表名
     * @param metaTableName 读入模型中的数据库表名
     * @return
     */
    public MetaDataDaoBuilder tableNameMapping(String rawTableName, String metaTableName){
        setting.getTableNameMapping().put(rawTableName, metaTableName);
        return this;
    }

    /**
     * 如果我们把某个大表的数据，分割到多张表中，可利用tableNameMapping和classNameMapping。
     * 例如，订单数据量太大，根据订单号hash到10张表里，表名可能是这个样子：order_01、order_02、...、order_10，这10张表的结构完全相同。
     * 我们可以指定tableName，只利用其中一张表作为模型来生成代码，但是如果在模板中不特殊处理的话，生成的实体类名是Order01，生成的sql中的表名是order_01。这显然不是我们想要的结果。
     * @param rawTableName 数据库表名
     * @param className 读入模型中的实体类名称
     * @return
     */
    public MetaDataDaoBuilder classNameMapping(String rawTableName, String className){
        setting.getTableNameMapping().put(rawTableName, className);
        return this;
    }

    public MetaDataDao build(){
        try {
            return new MetaDataDao(setting);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
