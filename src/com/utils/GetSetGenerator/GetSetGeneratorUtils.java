package com.utils.GetSetGenerator;

import com.utils.Exception.CopyException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: xiaoyx
 * Date: 2016-9-16
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class GetSetGeneratorUtils {
    public static void main(String[] args) {
        fromSourceToDestination(new Person(), new Human());
       /* Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        set1.add("abc");
        set1.add("bcd");
        set2.add("bcd");
        set2.add("abc");
        System.out.println(set1.equals(set2));*/
    }

    public static Object fromSourceToDestination(Object src, Object des) {
        try {
            //desClazz目标Pojo类Class对象
            Class<?> desClazz = des.getClass();
            //srcClazz源Pojo类Class对象
            Class<?> srcClazz = src.getClass();
            canCopyValidate(srcClazz, desClazz);
            //分别拿到目标对象的set方法列表以及源对象类的get方法列表
            Map<String, Class<?>[]> destinationObjectMethodMaps = getDestinationObjectMethodlist(desClazz.getDeclaredMethods());
            Map<String, Class<?>[]>  sourceObjectMethodMaps = getSourceObjectMethodlist(srcClazz.getDeclaredMethods());
            Set sourceObjectMethodSets = sourceObjectMethodMaps.keySet();
            Set destinationObjectMethodSets = destinationObjectMethodMaps.keySet();
            /*for (Object obj : sourceObjectMethodSets) {
                System.out.println("Source:"+obj.toString());
            }
            System.out.println("-----------------------------");
            for (Object obj : destinationObjectMethodSets) {
                System.out.println("Des:"+obj.toString());
            }*/
            /*//如果为全拷贝则两个Set必须相等
            if (sourceObjectMethodSets.equals(destinationObjectMethodSets)){

            }else {
                throw new CopyException("源对象与目标对象属性个数不相等！");
            }*/
        } catch (Exception e){

        }
        return null;
    }


    /**
     * @Author xiaoyx hello.dba2@gmail.com
     * @Date 2016-9-16 17:20
     * @Description 拼装目标对象的Set方法及该方法参数的结果集
     */
    public static Map<String, Class<?>[]> getDestinationObjectMethodlist(Method[] methods) {
        //目标对象Set方法存储集，key为方法名，value该方法参数
        Map<String, Class<?>[]> DestinationObjectSetMethodMaps = new HashMap<String, Class<?>[]>();
        //过滤get方法，将剩下set方法装入Set方法存储集
        for (int i=0; i<methods.length; i++) {
            if("s".equals(methods[i].getName().substring(0,1))) {
                DestinationObjectSetMethodMaps.put(methods[i].getName(), methods[i].getParameterTypes());
            }
        }
        return DestinationObjectSetMethodMaps;
    }

    /**
     * @Author xiaoyx hello.dba2@gmail.com
     * @Date 2016-9-16 17:20
     * @Description 拼装源对象的Get方法结果集
     */
    public static Map<String, Class<?>[]> getSourceObjectMethodlist(Method[] methods) {

        //目标对象Set方法存储集
        Map<String, Class<?>[]> SourceObjectSetMethodMaps = new HashMap<String, Class<?>[]>();
        //过滤get方法，将剩下set方法装入Set方法存储集
        for (int i=0; i<methods.length; i++) {
            if("g".equals(methods[i].getName().substring(0,1))) {
                SourceObjectSetMethodMaps.put(methods[i].getName(), methods[i].getParameterTypes());
            }
        }
        return SourceObjectSetMethodMaps;
    }

    /**
     * @Author xiaoyx hello.dba2@gmail.com
     * @Date 2016-9-22 23:19
     * @param  srcClazz 源class对象 dscClazz 目标class对象
     * @Description canCopyValidate 校验是否能进行copy，主要校验字段名称、数量、get/set方法是否具备拷贝条件
     */
    private static boolean canCopyValidate(Class srcClazz, Class desClazz) {
        Field[] srcFields = srcClazz.getDeclaredFields();
        Field[] desFields = desClazz.getDeclaredFields();
        //校验源对象及目标对象属性名集合
        if (null == srcFields || srcFields.length ==0) {
            throw new CopyException("源对象参数列表为空，无法进行拷贝！");
        }
        if ( null == desFields || desFields.length ==0) {
            throw  new CopyException("目标对象参数列表为空，无法进行拷贝");
        }
        //将源对象及目标对象属性名称放入集合进行校验
        Set<String> srcFieldsNameSets = new HashSet<String>();
        for (Field field : desFields) {
            srcFieldsNameSets.add(field.getName());
        }

        Set<String> descFieldsNameSets = new HashSet<String>();
        for (Field field : srcFields) {
            descFieldsNameSets.add(field.getName());
        }
        //校验源对象是否包含目标对象所需要拷贝的全部字段
        boolean fieldsNumberCanCopy = srcFieldsNameSets.containsAll(descFieldsNameSets);
        System.out.println("is ok:" + fieldsNumberCanCopy);
        for (Field field : desFields) {
            System.out.println("des filed:" + field.getName());
        }
        return true;
    }
}
