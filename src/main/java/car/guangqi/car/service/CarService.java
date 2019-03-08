package car.guangqi.car.service;

import car.guangqi.Exeception.CommonException;
import car.guangqi.car.dao.CarDao;
import car.guangqi.car.entity.TagVO;
import car.guangqi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final static String  OWNER_KEY = "传祺";

    @Autowired
    private CarDao carDao;

    public List getCars () {
        return carDao.queryAllCars();
    }



    public Map<String, List> queryCommentCountByTagCode(String code, boolean positive, int carid, int competitorCarId) throws CommonException {
        HashMap<String,List> result = new HashMap<>();
        // 查二级指标 或三级指标的名称 和 ID
        List<TagVO> tagList =  carDao.querySubLevelByParentId(code);
        List<String> tagCodeList = tagList.stream().map(TagVO::getCode).collect(Collectors.toList());
        // 用 二级指标或三级指标的ID查询满意和不满意的数量
        Map<String,Object> params = new HashMap<>();
        params.put("codes", tagCodeList);
        params.put("emotion", positive ? 1 : -1);
        // 查询自由车型
        params.put("carId", carid);
        List<Long> ownerTagValueList = new ArrayList<>();
        if (tagCodeList.size() > 0) ownerTagValueList   = getCount(params, tagCodeList);
        // 查询对比车型
        params.put("carId", competitorCarId);
        List<Long> competitorTagValueList =  new ArrayList<>();
        if (tagCodeList.size() > 0)   competitorTagValueList =  getCount(params, tagCodeList);

        List<List<Long>> allTageValueList  = new ArrayList<>();

        // 两个数据集合结合合并到一个集合
        allTageValueList.add(ownerTagValueList);
        allTageValueList.add(competitorTagValueList);

        result.put("tags", tagList);
        result.put("values", allTageValueList);

        return result;
    }

    private List<Long> getCount(Map<String,Object> params,List<String> tagCodeList ) {
        List<Map<String,Object>> ownerEmotionList =  carDao.queryEmotionByCodes(params);
        // 按照名称顺序取出统计值
        List<Long> tagValueList = new ArrayList<>();
        for (String tempCode : tagCodeList) {
            boolean haveValue = false;
            for (Map emotion:ownerEmotionList) {
                if (tempCode.equals( emotion.get("recom_tag") )) {
                    tagValueList.add((Long) emotion.get("count"));
                    haveValue = true;
                }
            }
            if (!haveValue)  tagValueList.add((0L));
        }
        return tagValueList;
    }

    public Map<String, List> queryCarList(String ownerCar, List<String> competitors) throws CommonException{
        if (StringUtil.isBlank(ownerCar)) {
            ownerCar = OWNER_KEY;
        }

        List<Map> ownerCarInfoList = carDao.queryOwnerCarList(ownerCar);

        Map param = new HashMap();
        param.put("ownerCar", ownerCar);
        param.put("competitors", competitors);
        List<Map> competitorCarInfoList = carDao.queryCompetitorCarList(param);

        // 处理结果，处理成树形结构  系列 - 年款 - 车型
        Map<String,Map<String, List<Map<String,String>>>> ownerCarTree = _convertCarList(ownerCarInfoList);
        Map<String,Map<String, List<Map<String,String>>>> competitorCarTree = _convertCarList(competitorCarInfoList);

        Map result = new HashMap();
        result.put("ownerCars",ownerCarTree);
        result.put("competitorCars",competitorCarTree);
        return result;
    }

    // 将数据库查询出来的扁平的数据转换成有关系的树形数据
    private Map<String,Map<String, List<Map<String,String>>>> _convertCarList(List<Map> ownerCarInfoList) {
        Map<String,Map<String, List<Map<String,String>>>> ownerCarTree  = new HashMap<>();
        for (int _i =0; _i < ownerCarInfoList.size(); _i ++) {
            Map info = ownerCarInfoList.get(_i);
            int id = (int) info.get("id");
            String series = (String) info.get("series");
            String name = (String) info.get("name");
            String year = name.substring(0,name.indexOf(" "));
            String model = name.substring(name.indexOf(" ") + 1);
            // 二级对象 系列： {年款: [型号1，型号2]}
            Map<String,  List<Map<String,String>>> secondObjMap= new HashMap<>();
            if (ownerCarTree.containsKey(series)) {
                secondObjMap = ownerCarTree.get(series);
            } else {
                ownerCarTree.put(series, secondObjMap);
            }
            // 三级对象  年款: [型号1，型号2]
            List<Map<String,String>> thirdList = new ArrayList<>();
            if (secondObjMap.containsKey(year)) {
                thirdList = secondObjMap.get(year);
            } else {
                secondObjMap.put(year, thirdList);
            }
            Map modelAndIdMap = new HashMap();
            modelAndIdMap.put("id", id);
            modelAndIdMap.put("value", model);
            modelAndIdMap.put("label", model);
            thirdList.add(modelAndIdMap);
        }
        return ownerCarTree;
    }

    public List<Map> queryFirstLevelTag() throws CommonException{
        return carDao.queryFirstLevelTag();
    }

    public List<Map> queryRecomContent(int carId, String code, boolean positive) throws CommonException{
        Map param = new HashMap();
        param.put("carId",carId);
        param.put("code",code);
        param.put("emotion",positive ? 1 : -1);
        return carDao.queryRecomContent(param);
    }

    // 计算自由和竞品车型各一级分类的正面和反面热度
    public Map queryCompositeScore(int ownerCarId, int competitorCarId) throws CommonException{
        List<Map> firstTags = this.queryFirstLevelTag();

        Map<String,List> ownerCarData = this._queryCompositeScoreByCarId(ownerCarId, firstTags);
        Map<String,List> competitorCarData = this._queryCompositeScoreByCarId(competitorCarId, firstTags);

        List ownerPostitiveList = ownerCarData.get("positive");
        List competitorPostitiveList = competitorCarData.get("positive");

        List ownerNegativeList = ownerCarData.get("negative");
        List competitorNegativeList = competitorCarData.get("negative");

        Map<String,List> result = new HashMap<>();
        List<List> postitiveList = new ArrayList<>();
        postitiveList.add(ownerPostitiveList);
        postitiveList.add(competitorPostitiveList);

        List<List> negativeList = new ArrayList<>();
        negativeList.add(ownerNegativeList);
        negativeList.add(competitorNegativeList);
        result.put("negative",negativeList);
        result.put("positive",postitiveList);
        result.put("tagIndicator",firstTags);

        return result;
    }

    private Map<String,List> _queryCompositeScoreByCarId (int carId,List<Map> firstTags) {
        Map<String,List> result = new HashMap<>();
        List<Long>  postiveScore = new ArrayList<>(); // 正
        List<Long>  negativeScore = new ArrayList<>(); // 负

        Map param = new HashMap();
        param.put("carId", carId);
        for (Map tag : firstTags) {
            String code = (String) tag.get("id");
            param.put("code", code);
            List<Map> tagEmocationList = carDao.queryPMCount(param);
            if (tagEmocationList.size() < 2) {
                postiveScore.add(0L);
                negativeScore.add(0L);
                continue;
            }
            Map postiveTagEmocation = tagEmocationList.get(0);
            long postiveCount = (long) postiveTagEmocation.get("count");

            Map nagativeTagEmocation = tagEmocationList.get(1);
            long negativeCount = (long) nagativeTagEmocation.get("count");

            postiveScore.add((long) (Math.ceil(((float)postiveCount / (postiveCount + negativeCount))* 100) )) ;
            negativeScore.add((long) Math.ceil(((float)negativeCount / (postiveCount + negativeCount)* 100) ));
        }

        result.put("positive",postiveScore);
        result.put("negative",negativeScore);
        return result;
    }
}
