package car.guangqi.car.control;

import car.guangqi.Exeception.CommonException;
import car.guangqi.car.service.CarService;
import car.guangqi.util.LogUtil;
import car.guangqi.util.ResponseWrap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("carApi")
public class restCarController {

    @Resource(name="carService")
    private CarService carService;

    @RequestMapping("getCarlist")
    public Object getCarlist () {
        return carService.getCars();
    }

    @RequestMapping("queryCommentCountByTagCode")
    public Object queryCommentCountByTagCode (String code,int ownerCarid,int competitorCarId,boolean positive) {
        ResponseWrap result = new ResponseWrap("success",true);
        try {
           Map<String, List> data = carService.queryCommentCountByTagCode(code,positive,ownerCarid,competitorCarId);
            result.setData(data);
        }catch (CommonException e) {
            LogUtil.Error(e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return  result;
    }
}
