package com.lemon.cases;

import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.Constants;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**123
 * 充值接口测试类型
 */
public class AddCase extends BaseCase {

    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo) {
//        1、参数化替换
        paramsReplace(caseInfo);
//        2、数据库前置查询结果(数据断言必须在接口执行前后都查询)
//        3、调用接口
        //获取鉴权头+默认头
        Map<String, String> authorizationHeader = getAuthorizationHeader();
        //调用注册接口
        String responseBody = HttpUtils.call(caseInfo, authorizationHeader);
        //存储loan_id
        getParamsInUserData(responseBody,"$.data.id","${loan_id}");
//        4、断言响应结果  断言，期望值和实际值匹配，匹配上了就是断言成功，相反断言失败。
        boolean responseAssertFlag = responseAssert(caseInfo.getExpectedResult(), responseBody);
//        5、添加接口响应回写内容
        addWriteBackData(sheetIndex,caseInfo.getId(), Constants.RESPONSE_CELL_NUM, responseBody);
//        6、数据库后置查询结果
//        7、据库断言
//        8、添加断言回写内容
        String assertResult = responseAssertFlag ? Constants.ASSERT_SUCCESS : Constants.ASSERT_FAILED;
        addWriteBackData(sheetIndex,caseInfo.getId(),Constants.ASSERT_CELL_NUM, assertResult);
//        9、添加日志
//        10、报表断言
        Assert.assertEquals(assertResult,Constants.ASSERT_SUCCESS);
    }

    @DataProvider
    public Object[] datas() throws Exception {
        List<CaseInfo> list = ExcelUtils.read(sheetIndex, 1, CaseInfo.class);
        return list.toArray();
    }
}
