package mate.academy.internetshop.lib;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.factory.Factory;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;

import java.lang.reflect.Field;

public class Injector {
    public static void injectDependency() throws IllegalAccessException {

        Class<ItemDao> itemDaoClass = ItemDao.class;
        Class<UserDao> userDaoClass = UserDao.class;
        Class<BucketDao> bucketDaoClass = BucketDao.class;
        Class<OrderDao> orderDaoClass = OrderDao.class;

        Class<BucketServiceImpl> bucketServiceClass = BucketServiceImpl.class;
        Field[] bucketServiceFields = bucketServiceClass.getDeclaredFields();

        for (Field field : bucketServiceFields) {
            injectBucketDao(field, bucketDaoClass);
            injectItemDao(field, itemDaoClass);
        }

        Class<ItemServiceImpl> itemServiceClass = ItemServiceImpl.class;
        Field[] itemServiceFields = itemServiceClass.getDeclaredFields();

        for (Field field : itemServiceFields) {
            injectItemDao(field, itemDaoClass);
        }

        Class<OrderServiceImpl> orderServiceClass = OrderServiceImpl.class;
        Field[] orderServiceFields = orderServiceClass.getDeclaredFields();

        for (Field field : orderServiceFields) {
            injectOrderDao(field, orderDaoClass);
        }

        Class<UserServiceImpl> userServiceClass = UserServiceImpl.class;
        Field[] userServiceFields = userServiceClass.getDeclaredFields();

        for (Field field : userServiceFields) {
            injectUserDao(field, userDaoClass);
        }
    }

    private static void injectBucketDao(Field field, Class<BucketDao> bucketDao)
            throws IllegalAccessException {
        if (field.getDeclaredAnnotation(Inject.class) != null
                && field.getType().equals(BucketDao.class)) {
            if (bucketDao.getDeclaredAnnotation(Dao.class) != null) {
                field.setAccessible(true);
                field.set(null, Factory.getBucketDao());
            } else {
                System.err.println("Wrong DAO format");
            }
        }
    }

    private static void injectUserDao(Field field, Class<UserDao> userDao)
            throws IllegalAccessException {
        if (field.getDeclaredAnnotation(Inject.class) != null
                && field.getType().equals(UserDao.class)) {
            if (userDao.getDeclaredAnnotation(Dao.class) != null) {
                field.setAccessible(true);
                field.set(null, Factory.getUserDao());
            } else {
                System.err.println("Wrong DAO format");
            }
        }
    }

    private static void injectOrderDao(Field field, Class<OrderDao> orderDao)
            throws IllegalAccessException {
        if (field.getDeclaredAnnotation(Inject.class) != null
                && field.getType().equals(OrderDao.class)) {
            if (orderDao.getDeclaredAnnotation(Dao.class) != null) {
                field.setAccessible(true);
                field.set(null, Factory.getOrderDao());
            } else {
                System.err.println("Wrong DAO format");
            }
        }
    }

    private static void injectItemDao(Field field, Class<ItemDao> itemDao)
            throws IllegalAccessException {
        if (field.getDeclaredAnnotation(Inject.class) != null
                && field.getType().equals(ItemDao.class)) {
            if (itemDao.getDeclaredAnnotation(Dao.class) != null) {
                field.setAccessible(true);
                field.set(null, Factory.getItemDao());
            } else {
                System.err.println("Wrong DAO format");
            }
        }
    }
}
