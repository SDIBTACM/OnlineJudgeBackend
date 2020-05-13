package cn.edu.sdtbu.config;

import cn.edu.sdtbu.model.enums.IntValueEnum;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-05-10 19:10
 */
public class IntEnumValueDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        Class cls = (Class) type;
        Object[] enumConstants = cls.getEnumConstants();
        if (IntValueEnum.class.isAssignableFrom(cls)) {
            for (Object enumConstant : enumConstants) {
                if (lexer.stringVal().equals(((IntValueEnum) enumConstant).getValue() + "")) {
                    return (T) enumConstant;
                }
            }
        } else {
            //没实现EnumValue接口的 默认的按名字或者按ordinal
            if (token == JSONToken.LITERAL_INT) {
                int intValue = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);

                if (intValue < 0 || intValue > enumConstants.length) {
                    throw new JSONException("parse enum " + cls.getName() + " error, value : " + intValue);
                }
                return (T) enumConstants[intValue];
            } else if (token == JSONToken.LITERAL_STRING) {
                return (T) Enum.valueOf(cls, lexer.stringVal());
            }
        }
        return null;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}