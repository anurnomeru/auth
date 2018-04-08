package com.raythonsoft.server.util;

import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.util.ResultGenerator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by Anur IjuoKaruKas on 2017/8/8.
 * Description :
 */
public class BingdingResultValidator {
/**通用**/

    /**
     * @param result
     * @return
     */
    public static Result validate(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> errorList = result.getAllErrors();
            StringBuffer stringBuffer = new StringBuffer();
            for (ObjectError error : errorList) {
                stringBuffer.append(error.getDefaultMessage() + "，");
            }
            return ResultGenerator.genFailResult(stringBuffer.substring(0, stringBuffer.length() - 1));
        }
        return null;
    }
}
