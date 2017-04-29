package com.sorcerer.sorcery.iconpack.data.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/18
 */

public class TableBuilder {
    public static final int FLAG_AUTOINCREMENT = 2;
    public static final int FLAG_COLLATE_NOCASE = 8;
    public static final int FLAG_NOT_NULL = 4;
    public static final int FLAG_PRIMARY_KEY = 1;
    public static final String FOREIGN_KEY_CASCADE = "CASCADE";
    public static final String FOREIGN_KEY_NO_ACTION = "NO ACTION";
    public static final String FOREIGN_KEY_RESTRICT = "RESTRICT";
    public static final String TYPE_BOOL = "INTEGER";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_TEXT = "TEXT";
    private StringBuilder mBuilder;
    private List<String> mForeignKeys = new ArrayList<>();
    private List<String> mPrimaryKeyColumns = new ArrayList<>();
    private String mTableName;
    private List<String> mUniqueColumns = new ArrayList<>();

    public TableBuilder(String tableName) {
        this.mTableName = tableName;
    }

    public TableBuilder setTableName(String tableName) {
        this.mTableName = tableName;
        return this;
    }

    public TableBuilder addTextColumn(String name) {
        return addColumn(name, TYPE_TEXT, 0);
    }

    public TableBuilder addTextColumn(String name, int flags) {
        return addColumn(name, TYPE_TEXT, flags);
    }

    public TableBuilder addIntegerColumn(String name) {
        return addColumn(name, TYPE_INTEGER, 0);
    }

    public TableBuilder addIntegerColumn(String name, int flags) {
        return addColumn(name, TYPE_INTEGER, flags);
    }

    public TableBuilder addBooleanColumn(String name) {
        return addColumn(name, TYPE_INTEGER, 0);
    }

    public TableBuilder addBooleanColumn(String name, int flags) {
        return addColumn(name, TYPE_INTEGER, flags);
    }

    public TableBuilder addColumn(String name, String type) {
        return addColumn(name, type, 0);
    }

    public TableBuilder addColumn(String name, String type, int flags) {
        if (this.mBuilder == null) {
            this.mBuilder = new StringBuilder();
            this.mBuilder.append("CREATE TABLE ").append(this.mTableName).append(" (");
        } else {
            this.mBuilder.append(", ");
        }
        this.mBuilder.append(name).append(" ").append(type);
        if ((flags & FLAG_PRIMARY_KEY) != 0) {
            this.mBuilder.append(" primary key ");
        }
        if ((flags & FLAG_AUTOINCREMENT) != 0) {
            this.mBuilder.append(" autoincrement ");
        }
        if ((flags & FLAG_NOT_NULL) != 0) {
            this.mBuilder.append(" not null ");
        }
        if ((flags & FLAG_COLLATE_NOCASE) != 0) {
            this.mBuilder.append(" collate nocase ");
        }
        return this;
    }

    public TableBuilder addPrimaryKeyColumn(String columnName) {
        this.mPrimaryKeyColumns.add(columnName);
        return this;
    }

    public TableBuilder addForeignKey(String columnName, String referencedTable,
                                      String referencedColumn, String onDeleteAction) {
        List list = this.mForeignKeys;
        Object[] objArr = new Object[FLAG_NOT_NULL];
        objArr[0] = columnName;
        objArr[FLAG_PRIMARY_KEY] = referencedTable;
        objArr[FLAG_AUTOINCREMENT] = referencedColumn;
        objArr[3] = onDeleteAction;
        list.add(String.format("FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE %s", objArr));
        return this;
    }

    public TableBuilder addForeignKey(String[] columnsName, String referencedTable,
                                      String[] referencedColumns, String onDeleteAction) {
        int i;
        String key = "FOREIGN KEY(";
        int count = columnsName.length;
        for (i = 0; i < count; i += FLAG_PRIMARY_KEY) {
            key = key + columnsName[i];
            if (i < columnsName.length - 1) {
                key = key + ", ";
            }
        }
        key = key + ") REFERENCES " + referencedTable + "(";
        count = referencedColumns.length;
        for (i = 0; i < count; i += FLAG_PRIMARY_KEY) {
            key = key + referencedColumns[i];
            if (i < referencedColumns.length - 1) {
                key = key + ", ";
            }
        }
        this.mForeignKeys.add(key + ") ON DELETE " + onDeleteAction);
        return this;
    }

    public TableBuilder addUniqueColumns(String... columns) {
        int length = columns.length;
        for (int i = 0; i < length; i += FLAG_PRIMARY_KEY) {
            this.mUniqueColumns.add(columns[i]);
        }
        return this;
    }

    public String build() {
        int count;
        int i;
        if (!this.mPrimaryKeyColumns.isEmpty()) {
            this.mBuilder.append(", PRIMARY KEY (");
            count = this.mPrimaryKeyColumns.size();
            for (i = 0; i < count; i += FLAG_PRIMARY_KEY) {
                this.mBuilder.append((String) this.mPrimaryKeyColumns.get(i));
                if (i < this.mPrimaryKeyColumns.size() - 1) {
                    this.mBuilder.append(", ");
                }
            }
            this.mBuilder.append(")");
        }
        if (!this.mForeignKeys.isEmpty()) {
            this.mBuilder.append(", ");
            count = this.mForeignKeys.size();
            for (i = 0; i < count; i += FLAG_PRIMARY_KEY) {
                this.mBuilder.append((String) this.mForeignKeys.get(i));
                if (i < this.mForeignKeys.size() - 1) {
                    this.mBuilder.append(", ");
                }
            }
        }
        if (!this.mUniqueColumns.isEmpty()) {
            this.mBuilder.append(", UNIQUE (");
            count = this.mUniqueColumns.size();
            for (i = 0; i < count; i += FLAG_PRIMARY_KEY) {
                this.mBuilder.append((String) this.mUniqueColumns.get(i));
                if (i < this.mUniqueColumns.size() - 1) {
                    this.mBuilder.append(", ");
                }
            }
            this.mBuilder.append(")");
        }
        return this.mBuilder.append(" ); ").toString();
    }
}
