package car.guangqi.car.control;

import car.guangqi.Exeception.CommonException;
import car.guangqi.car.service.CarService;
import car.guangqi.util.LogUtil;
import car.guangqi.util.ResponseWrap;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedList;
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
    public Object queryCommentCountByTagCode (@RequestBody Map params) throws JSONException {
//        String code,int ownerCarId,int competitorCarId,boolean positive
        String code = (String) params.get("code");
        int ownerCarId = (int) params.get("ownerCarId");
        int competitorCarId = (int) params.get("competitorCarId");
        boolean positive = (boolean) params.get("positive");

        ResponseWrap<Map<String, List>> result = new ResponseWrap<>("success",true);
        try {
           Map<String, List> data = carService.queryCommentCountByTagCode(code,positive,ownerCarId,competitorCarId);
            result.setData(data);
        }catch (CommonException e) {
            LogUtil.Error(e.getMessage());
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return  result;
    }

    @RequestMapping("queryCarList")
    public Object queryCarList (@RequestBody Map params) {
        ResponseWrap result = new ResponseWrap("success",true);
        String ownerCar = "";
       List<String> competitors = new LinkedList<>();

        // 判断如果没有参数则查询所有自由车型 和 竞争车型
        if (params.containsKey("ownerCar") ) {
            ownerCar = (String) params.get("ownerCar");
        }
        if (params.containsKey("competitors") ) {
            competitors = (List) params.get("competitors");
        }

        Map<String,List> data = null;
        try {
            data = carService.queryCarList(ownerCar, competitors);
        } catch (CommonException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        result.setData(data);
        return  result;
    }

    @RequestMapping("queryFirstLevelTag")
    public  Object queryFirstLevelTag() {
        ResponseWrap result = new ResponseWrap("success",true);
        try {
            List<Map> data = carService.queryFirstLevelTag();
            result.setData(data);
        } catch (CommonException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }

    @RequestMapping("queryRecomContent")
    public Object queryRecomContent (@RequestBody Map params) {
        int carId = (int) params.get("carId");
        String code = (String) params.get("code");
        boolean positive = (boolean) params.get("positive");
        ResponseWrap result = new ResponseWrap("success",true);
        try {
            List<Map> data = carService.queryRecomContent(carId,code,positive);
            result.setData(data);
        } catch (CommonException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }

    @RequestMapping("queryCompositeScore")
    public Object queryCompositeScore(@RequestBody Map params) {
        int ownerCarId = (int) params.get("ownerCarId");
        int competitorCarId = (int) params.get("competitorCarId");
        ResponseWrap result = new ResponseWrap("success",true);
        try {
            Map data = carService.queryCompositeScore(ownerCarId,competitorCarId);
            result.setData(data);
        } catch (CommonException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return  result;
    }
}
