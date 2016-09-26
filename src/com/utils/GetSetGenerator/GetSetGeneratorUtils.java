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
        try {
            long start = System.currentTimeMillis();
            for (int i=0; i< 10000; i++){
                Person p = new Person();
                p.setName("肖邦");
                p.setAge(27);
                p.setHeight(167.7);
                Object obj = fromSourceToDestination(p, new Human());
            }
            long stop = System.currentTimeMillis();
            System.out.println("This time cost's "+ (stop - start)+" mils");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * @Author xiaoyx hello.dba2@gmail.com
     * @Date 2016-9-16 17:20
     * @Description 拷贝的核心方法，从源src对象拷贝到目标对象des
     */
    public static Object fromSourceToDestination(Object src, Object des) throws Exception {
            //desClazz目标Pojo类Class对象
            Class<?> desClazz = des.getClass();
            //srcClazz源Pojo类Class对象
            Class<?> srcClazz = src.getClass();
            //校验是否满足拷贝条件，如果不满足会直接抛出异常，过程终止
            canCopyValidate(srcClazz, desClazz);
            //分别拿到目标对象的set方法列表以及源对象类的get方法列表
            Map<String, Class<?>[]> destinationObjectMethodMaps = getDestinationObjectMethodlist(desClazz.getDeclaredMethods());
            Map<String, Class<?>[]>  sourceObjectMethodMaps = getSourceObjectMethodlist(srcClazz.getDeclaredMethods());
            Set sourceObjectMethodSets = sourceObjectMethodMaps.keySet();
            Set destinationObjectMethodSets = destinationObjectMethodMaps.keySet();
            //构建目标对象
            Object desNewObj = desClazz.newInstance();
            //构建源对象
            for (Iterator iteratorOfSource = sourceObjectMethodSets.iterator();iteratorOfSource.hasNext();){
                String getMethodName = iteratorOfSource.next().toString();
                Class<?>[] clazzSrc = sourceObjectMethodMaps.get(getMethodName);
                Method methodSrc = srcClazz.getMethod(getMethodName, clazzSrc);
                Object obj = methodSrc.invoke(src,clazzSrc);
                for (Iterator iteratorOfDes = destinationObjectMethodSets.iterator();iteratorOfDes.hasNext();){
                    //比较外层方法名，从方法名第4个(包含头不包含尾)字符开始截取做比较，字面值相等则对目标对象相应字段设赋值
                    String setMethodName = iteratorOfDes.next().toString();
                    //src是源对象，通过反射需要调用其get方法拿到值，sec是目标对象最终调用其set方法赋值，这里通过取到二者方法名从第三位起截取字符串进行字段属性名称比较，如果二者字面值相等则对目标对象set方法赋值并调用
                    if(setMethodName.substring(3,setMethodName.length()).equals(getMethodName.substring(3,getMethodName.length()))){
                        Class<?>[] clazzDes = destinationObjectMethodMaps.get(setMethodName);
                        Method methodDes = desClazz.getMethod(setMethodName, clazzDes);
                        methodDes.invoke(desNewObj, obj);
                    }
                }
            }
        return desNewObj;
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
        for (Field field : srcFields) {
            srcFieldsNameSets.add(field.getName());
        }

        Set<String> desFieldsNameSets = new HashSet<String>();
        for (Field field : desFields) {
            desFieldsNameSets.add(field.getName());
        }
        //校验源对象是否包含目标对象所需要拷贝的全部字段
        boolean fieldsNumberCanCopy = srcFieldsNameSets.containsAll(desFieldsNameSets);
        if(!fieldsNumberCanCopy){
            throw new CopyException("源对象属性值无法满足目标对象拷贝！");
        }
        return true;
    }
}
