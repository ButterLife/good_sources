package cn.zhuchuangsoft.footstone.utils;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Description: mybatis类型转换，将查询结果的省市县编码转换成名称
 * <br>Date: 2020-04-16 16:03
 *
 * @author yanglizhang
 * @version 1.0
 */
public class AddressTypeHandler implements TypeHandler<String> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public String getResult(ResultSet resultSet, String s) throws SQLException {
        //获取省，市，县的编号
        String code = resultSet.getString(s);
        //转换成名称
        return JsonResourceUtils.codeAndNameMap.get(code);
    }

    @Override
    public String getResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    @Override
    public String getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
