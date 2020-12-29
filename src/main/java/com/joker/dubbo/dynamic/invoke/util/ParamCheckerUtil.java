package com.joker.dubbo.dynamic.invoke.util;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
* @Description: 请求参数断言工具类
* @author JokerLee
* https://github.com/JokerLee-9527
* @date 2020/12/25 15:12
* @version V1.0
*/
public abstract class ParamCheckerUtil {

    /**
     * 断言字符串空
     * 该断言会会判断字符串是否为空，只包含空白符也会判断为空
     *
     * @param string 断言字符串
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void isBlank(String string, String msg) {
        if (StringUtils.isNotBlank(string)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言字符串非空
     * 该断言会会判断字符串是否为空，只包含空白符也会判断为空
     *
     * @param string 断言字符串
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void notBlank(String string, String msg) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言集合非空
     *
     * @param collection 集合
     * @param msg        提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void notEmpty(Collection collection, String msg) {
        if (null == collection || collection.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言String集合非空和集合元素非空
     *
     * @param collection String集合
     * @param elementMsg 集合元素不符合要求提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void nonBlankElements(Collection<String> collection, String elementMsg) {
        for (String str : collection) {
            notBlank(str, elementMsg);
        }
    }

    /**
     * 断言N选1必须选一个
     */
    public static void optionNotBlank(String elementMsg, String... strings) {
        boolean hasOneNotNull = false;
        for (String str : strings) {
            if (StringUtils.isNotBlank(str)) {
                hasOneNotNull = true;
                break;
            }
        }
        if (!hasOneNotNull) {
            throw new IllegalArgumentException(elementMsg);
        }
    }

    /**
     * 断言String集合容量
     *
     * @param collection 集合
     * @param maxSize    集合最大数量
     * @throws IllegalArgumentException 异常
     */
    public static void maxSize(Collection<?> collection, int maxSize, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        if (collection.size() > maxSize) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言对象非null
     *
     * @param object 对象
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void nonNull(Object object, String msg) {
        if (null == object) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言对象null
     *
     * @param object 对象
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void isNull(Object object, String msg) {
        if (null != object) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言表达式结果为真,为假则提示异常信息
     *
     * @param boolExpression 布尔表达式
     * @param falseMsg       提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectTrue(boolean boolExpression, String falseMsg) {
        if (!boolExpression) {
            throw new IllegalArgumentException(falseMsg);
        }
    }

    /**
     * 断言表达式结果为假,为真则提示异常信息
     *
     * @param boolExpression 布尔表达式
     * @param trueMsg        提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectFalse(boolean boolExpression, String trueMsg) {
        if (boolExpression) {
            throw new IllegalArgumentException(trueMsg);
        }
    }

    /**
     * 断言表达式结果都为真，抛出提示信息
     *
     * @param booleans 布尔表达式数组
     * @param msg      提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectAnyFalse(String msg, Boolean... booleans) throws IllegalArgumentException {
//        if (Arrays.stream(booleans).allMatch(t -> t)) {
//            throw new IllegalArgumentException(msg);
//        }
        Boolean allTrue = true;
        for (int i = 0; (i < booleans.length) && (allTrue == true); i++) {
            if (!booleans[i]) {
                allTrue = false;
            }
        }
        if (!allTrue) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 检查集合元素个数在[min, max]区间
     *
     * @param collection  表达式的参数
     * @param minElements 集合元素个数最小值
     * @param maxElements 集合元素个数最大值
     * @param outRangeMsg 条件表达式为true的提示消息
     */
    public static void expectInRange(Collection collection, int minElements, int maxElements, String outRangeMsg) {
        int elements = collection.size();
        if (elements < minElements || elements > maxElements) {
            throw new IllegalArgumentException(outRangeMsg);
        }
    }

    /**
     * 检查字符串长度在[min, max]区间
     *
     * @param string 断言字符串
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectInRange(String string, int minLength, int maxLength, String msg) {
        if (StringUtils.isBlank(string) || string.length() < minLength || string.length() > maxLength) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 可选字段，检查字符串长度在[min, max]区间
     *
     * @param string 断言字符串
     * @param msg    提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectInRange(Boolean required, String string, int minLength, int maxLength, String msg) {
        if (required) {
            expectInRange(string, minLength, maxLength, msg);
        } else {
            if (StringUtils.isNotBlank(string)) {
                expectInRange(string, minLength, maxLength, msg);
            }
        }
    }
    /**
     * 检查字符串长度在[min, max]区间
     *
     * @param integer 断言属性
     * @param msg     提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectInRange(Integer integer, int minValue, int maxValue, String msg) {
        if (null == integer || integer < minValue || integer > maxValue) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 检查数值长度在[min, max]区间
     *
     * @param bigDecimal 断言属性
     * @param msg        提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectInRange(BigDecimal bigDecimal, BigDecimal minValue, BigDecimal maxValue, String msg) {
        if (null == bigDecimal || bigDecimal.compareTo(minValue) < 0 || bigDecimal.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 检查日期格式
     *
     * @param sDate 断言字符串
     * @param msg   提示信息
     * @throws IllegalArgumentException 异常
     */
    public static void expectDateStrWithPattern(String sDate, String pattern, String msg) {
        Date outDate = null;
        if (StringUtils.isBlank(sDate)) {
            throw new IllegalArgumentException(msg);
        }
        if (StringUtils.isBlank(pattern)) {
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            df.parse(sDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(msg);
        }
    }
}
