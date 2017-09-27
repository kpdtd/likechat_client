package com.app.library.util;

/**
 * 操作工具类
 */
public class DBUtil
{
//    private final static String dbName = "test_db";
//    private static DaoMaster.DevOpenHelper openHelper;
//    private static Context mContext;
//
//    private static void init()
//    {
//        mContext = theApp.CONTEXT;
//        if (openHelper == null)
//        {
//            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
//        }
//    }
//
//    /**
//     * 获取可读数据库
//     */
//    private static SQLiteDatabase readableDatabase()
//    {
//        init();
//        SQLiteDatabase db = openHelper.getReadableDatabase();
//        return db;
//    }
//
//    /**
//     * 获取可写数据库
//     */
//    private static SQLiteDatabase writableDatabase()
//    {
//        init();
//        SQLiteDatabase db = openHelper.getWritableDatabase();
//        return db;
//    }
//
//    public static<T> void insert(T obj, Class<T> cls)
//    {
//        DaoMaster daoMaster = new DaoMaster(writableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        AbstractDao dao = daoSession.getDao(cls);
//        dao.insert(obj);
//    }
//
//    /**
//     *
//     * @param cls
//     * @param key 主键
//     * @param <T>
//     * @return
//     */
//    public static <T> T query(Class<T> cls, Long key)
//    {
//        DaoMaster daoMaster = new DaoMaster(readableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        AbstractDao dao = daoSession.getDao(cls);
//        return (T)dao.load(key);
//    }
//
//    public static<T> List<T> queryAll(Class<T> cls)
//    {
//        DaoMaster daoMaster = new DaoMaster(readableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        AbstractDao dao = daoSession.getDao(cls);
//        QueryBuilder<T> qb = dao.queryBuilder();
//        List<T> list = qb.list();
//        return list;
//    }
//
//    /**
//     * 插入一条记录
//     *
//     * @param test
//     */
//    public static void insertTest(Test test)
//    {
//        DaoMaster daoMaster = new DaoMaster(writableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        userDao.insert(test);
//    }
//
//    /**
//     * 插入用户集合
//     *
//     * @param tests
//     */
//    public static void insertTestList(List<Test> tests)
//    {
//        if (tests == null || tests.isEmpty())
//        {
//            return;
//        }
//        DaoMaster daoMaster = new DaoMaster(writableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        userDao.insertInTx(tests);
//    }
//
//    /**
//     * 删除一条记录
//     *
//     * @param test
//     */
//    public static void deleteTest(Test test)
//    {
//        DaoMaster daoMaster = new DaoMaster(writableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        userDao.delete(test);
//    }
//
//    /**
//     * 更新一条记录
//     *
//     * @param test
//     */
//    public static void updateTest(Test test)
//    {
//        DaoMaster daoMaster = new DaoMaster(writableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        userDao.update(test);
//    }
//
//    /**
//     * 查询用户列表
//     */
//    public static List<Test> queryTestList()
//    {
//        DaoMaster daoMaster = new DaoMaster(readableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        QueryBuilder<Test> qb = userDao.queryBuilder();
//        List<Test> list = qb.list();
//        return list;
//    }
//
//    /**
//     * 查询用户列表
//     */
//    public static List<Test> queryTestList(int age)
//    {
//        DaoMaster daoMaster = new DaoMaster(readableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TestDao userDao = daoSession.getTestDao();
//        QueryBuilder<Test> qb = userDao.queryBuilder();
//        qb.where(TestDao.Properties.Age.gt(age)).orderAsc(TestDao.Properties.Age);
//        List<Test> list = qb.list();
//        return list;
//    }
}
