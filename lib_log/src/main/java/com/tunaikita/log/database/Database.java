package com.tunaikita.log.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.orliu.kotlin.common.tools.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;

public final class Database {
    /**
     * 单例
     */
    private static Database instance = null;

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * 数据库名称
     */
    private String databaseName = "";
    /**
     * 数据库版本
     */
    private int databaseVersion = 0;
    /**
     * 数据库
     */
    private SQLiteDatabase sqliteDatabase = null;
    /**
     * 数据库辅助类,用来管理数据库的创建和版本
     */
    private DatabaseHelper dbHelper = null;
    /**
     * 装载查询集合对象
     */
    private Cursor cursor = null;
    /**
     * 初始化数据库数据表接口对象，创建数据库必须实现此接口
     */
    private IDatabase callback;


    private static final int STATIC_VAR = (Modifier.STATIC | Modifier.FINAL) + 1;

    /**
     * 创建数据库
     *
     * @param context      上下文
     * @param databaseName 数据库名称
     */
    public void createDatabase(Context context, String databaseName,
                               int version, IDatabase callback) {
        this.databaseName = databaseName;
        this.databaseVersion = version;
        this.callback = callback;
        // 创建了一个DatabaseHelper对象，只执行这句话是不会创建或打开连接的
        dbHelper = new DatabaseHelper(context, databaseName, version);
        // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    /**
     * 打开数据库
     */
    public void openDatabase(Context context, String databaseName, int version,
                             IDatabase callback) {
        if (sqliteDatabase == null) {
            this.databaseName = databaseName;
            this.databaseVersion = version;
            this.callback = callback;
            // 创建了一个DatabaseHelper对象，只执行这句话是不会创建或打开连接的
            dbHelper = new DatabaseHelper(context, databaseName, version);
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            sqliteDatabase = dbHelper.getWritableDatabase();
        }
    }

    /**
     * 更新数据库
     *
     * @param context 上下文
     * @param version 数据库版本
     */
    public void updataDatabase(Context context, int version, IDatabase callback) {
        this.databaseVersion = version;
        this.callback = callback;
        dbHelper = new DatabaseHelper(context, databaseName, version);
        // 得到一个只读的SQLiteDatabase对象
        sqliteDatabase = dbHelper.getWritableDatabase();
    }

    /**
     * 创建数据表,默认创建自增主键_id
     *
     * @param clazz "create table StudentInfo(_id integer primary key autoincrement,name varchar(20),sex varchar(20),MyTime Time,MyDate Date)"
     */
    public <T> boolean createTable(Class<T> clazz) {
        String sql = "create table " + clazz.getSimpleName()
                + "(_id integer primary key autoincrement";
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {

                // f.getModifiers() != 25 去除static final常量
                if (!f.getName().equals("_id") &&
                        !f.getName().equals("$change") &&
                        f.getModifiers() != STATIC_VAR) {
                    sql += " , " + f.getName();
                }

            }
        }
        sql += ")";
        try {
            sqliteDatabase.execSQL(sql);
        } catch (SQLException e) {
            Logger.e(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除表
     *
     * @param clazz
     * @return
     */
    public <T> boolean dropTable(Class<T> clazz) {
        String sql = "drop table " + clazz.getSimpleName();
        try {
            sqliteDatabase.execSQL(sql);
        } catch (SQLException e) {
            Logger.e(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 插入
     *
     * @param object ContentValues values = new ContentValues(); values.put("id",
     *               1); values.put("name", "yangyz");
     */
    public long insert(Object object) {
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        long tag = sqliteDatabase.insert(object.getClass().getSimpleName(),
                null, getContentValues(object));
        return tag;
    }

    /**
     * 更新表数据
     *
     * @param object
     * @param whereClause
     * @param whereArgs   ContentValues values = new ContentValues(); values.put("id",
     *                    1); values.put("name", "yangyz");
     * @param whereClause "id=?"
     * @param whereArgs   new String[]{"1"}
     */
    public boolean update(Object object, String whereClause, String[] whereArgs) {
        // 调用update方法
        // 第一个参数String：对象
        // 第二个参数ContentValues：ContentValues对象
        // 第三个参数String："id=?" where字句，相当于sql语句where后面的语句
        // 第四个参数String[]：new String[]{"1"}
        int result = sqliteDatabase.update(object.getClass().getSimpleName(),
                getContentValues(object), whereClause, whereArgs);
        return result > 0 ? true : false;
    }

    /**
     * 删除指定数据行
     *
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean delete(String tableName, String whereClause,
                          String[] whereArgs) {
        int rows = sqliteDatabase.delete(tableName, whereClause, whereArgs);
        Log.d("zrplay", rows + "===");
        return rows >= 0 ? true : false;
    }

    /**
     * 简单查询
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> query(Class<T> clazz) {
        return query(clazz, null, null, null, null, null, null);
    }

    /**
     * @param clazz
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return ArrayList<T>
     */
    @SuppressWarnings("ResourceType")
    @SuppressLint("NewApi")
    public <T> ArrayList<T> query(Class<T> clazz, String whereClause,
                                  String[] whereArgs, String groupBy, String having, String orderBy,
                                  String limit) {
        // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
        Field[] fields = clazz.getDeclaredFields();

        // 去除static final常量后的field
        LinkedList<Field> fieldList = new LinkedList<>();
        for (Field f : fields) {
            if (f.getModifiers() != STATIC_VAR && !f.getName().equals("$change")) {
                fieldList.add(f);
            }
        }

        String[] fieldNames = new String[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            fieldNames[i] = fieldList.get(i).getName();
        }

        cursor = sqliteDatabase.query(clazz.getSimpleName(), fieldNames,
                whereClause, whereArgs, groupBy, having, orderBy, limit);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        ArrayList<T> list = new ArrayList<T>();
        try {
            while (cursor.moveToNext()) {
                T vo = (T) clazz.newInstance();
                for (int i = 0; i < fieldNames.length; i++) {
                    String name = fieldNames[i];
                    Field item = clazz.getDeclaredField(name);
                    item.setAccessible(true);
                    Method method = vo.getClass().getMethod(
                            "set" + upperCaseToOneWord(name),
                            new Class[]{item.getType()});
                    Class<?> classType = item.getType();
                    Object value = cursor.getString(i);
                    if (value.equals("") || value.equals("null")) {
                        value = null;
                    }
                    if (classType == String.class) {
                        method.invoke(vo, value == null ? "" : value.toString());
                    } else if (classType == int.class
                            || classType == Integer.class) {
                        method.invoke(
                                vo,
                                value == null ? 0 : Integer.parseInt(value
                                        .toString()));
                    } else if (classType == float.class
                            || classType == Float.class) {
                        method.invoke(
                                vo,
                                value == null ? (Float) 0F : Float
                                        .parseFloat(value.toString()));
                    } else if (classType == double.class
                            || classType == Double.class) {
                        method.invoke(
                                vo,
                                value == null ? 0 : Double.parseDouble(value
                                        .toString()));
                    } else if (classType == long.class
                            || classType == Long.class) {
                        method.invoke(
                                vo,
                                value == null ? 0L : Long.parseLong(value
                                        .toString()));
                    } else if (classType == java.util.Date.class
                            || classType == java.sql.Date.class) {
                        method.invoke(vo, value == null ? (Date) null : value);
                    } else if (classType == boolean.class
                            || classType == Boolean.class) {
                        method.invoke(
                                vo,
                                value == null ? false : "1".equals(value
                                        .toString()));
                    } else {
                        method.invoke(vo, value);
                    }
                }
                list.add(vo);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * 简单查询
     *
     * @param clazz
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return Cursor
     */
    public <T> Cursor queryCursor(Class<T> clazz, String whereClause,
                                  String[] whereArgs, String groupBy, String having, String orderBy,
                                  String limit) {
        // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
        Field[] fields = clazz.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                fieldNames[i] = fields[i].getName();
            }
        }
        cursor = sqliteDatabase.query(clazz.getSimpleName(), fieldNames,
                whereClause, whereArgs, groupBy, having, orderBy, limit);
        return cursor;
    }

    /**
     * 删除记录
     *
     * @param clazz
     * @param whereClause
     * @param whereArgs
     */
    public <T> boolean delete(Class<T> clazz, String whereClause,
                              String[] whereArgs) {
        // 调用SQLiteDatabase对象的delete方法进行删除操作
        // 第一个参数String：表名
        // 第二个参数String：条件语句
        // 第三个参数String[]：条件值
        int result = sqliteDatabase.delete(clazz.getSimpleName(), whereClause, whereArgs);
        return result > 0 ? true : false;
    }

    /**
     * 显示数据库所有表
     *
     * @param sqliteDatabase
     * @return
     */
    public String showDatabaseForm(SQLiteDatabase sqliteDatabase) {
        // SQLite数据库的表数据结构被保存在一个名叫"sqlite_master"的特殊的表中。你可以像查询其它表一样通过执行“SELECT”查询这个特殊的表。
        return showTableForm(sqliteDatabase, "sqlite_master");
    }

    /**
     * 显示表结构
     *
     * @param sqliteDatabase
     * @param table
     * @return
     */
    public String showTableForm(SQLiteDatabase sqliteDatabase, String table) {
        Cursor cursor = sqliteDatabase.query(table, null, null, null, null,
                null, null);// query返回值为一个游标对象
        int index = cursor.getColumnCount();
        String[] columns = new String[index];
        for (int i = 0; i < index; i++) {
            columns[i] = cursor.getColumnName(i);
        }
        cursor.close();
        return columns.toString();
    }

    /**
     * 关闭数据库操作占用连接资源
     */
    public void close() {
        if (cursor != null) {
            cursor.close();
        }
        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        sqliteDatabase = null;
        dbHelper = null;
        cursor = null;
        callback = null;
    }

    /**
     * 反射机制获取ITable对象的属性和属性值
     *
     * @param object
     * @return
     */
    @SuppressWarnings("ResourceType")
    private ContentValues getContentValues(Object object) {
        ContentValues values = new ContentValues();
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String fieldName = f.getName();
                if (!fieldName.equals("_id") &&
                        !fieldName.equals("$change") &&
                        f.getModifiers() != STATIC_VAR) {
                    String methodName = "get" + upperCaseToOneWord(fieldName);
                    // Class<?> classType = f.getType();
                    try {
                        Method method = object.getClass().getMethod(methodName);
                        Object str = method.invoke(object);
                        if (str != null) {
                            values.put(fieldName, str.toString());
                        } else {
                            values.put(fieldName, "");
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return values;
    }

    /**
     * 字符串首字母小写
     *
     * @param value
     * @return
     */
    private String lowerCaseToOneWord(String value) {
        String oneWord = value.charAt(0) + "";
        return oneWord.toLowerCase() + value.replaceFirst(oneWord, "");
    }

    private String upperCaseToOneWord(String value) {
        String oneWord = value.charAt(0) + "";
        return oneWord.toUpperCase() + value.replaceFirst(oneWord, "");
    }

    /**
     * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本他，它提供两个方面的功能
     * 第一，getReadableDatabase()、getWritableDatabase
     * ()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
     * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * 在SQLiteOpenHelper的子类当中，必须有该构造函数
         *
         * @param context 上下文对象
         * @param name    数据库名称
         * @param factory
         * @param version 当前数据库的版本，值必须是整数并且是递增的状态
         */
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            // 必须通过super调用父类当中的构造函数
            super(context, name, factory, version);
        }

        public DatabaseHelper(Context context, String name, int version) {
            this(context, name, null, version);
        }

        // public DatabaseHelper(Context context, String name) {
        // this(context, name,1);
        // }
        // 该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
        @Override
        public void onCreate(SQLiteDatabase db) {
            sqliteDatabase = db;
            if (callback != null)
                callback.onCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            sqliteDatabase = arg0;
            if (arg1 != arg2)
                if (callback != null)
                    callback.onUpgrade(arg0);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            sqliteDatabase = db;
            if (callback != null)
                callback.onOpen(db);
        }
    }
}