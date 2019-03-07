package car.guangqi.car.service;

import car.guangqi.Exeception.CommonException;
import car.guangqi.car.dao.CarDao;
import car.guangqi.car.entity.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarService {

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
        List<Long> ownerTagValueList = getCount(params, tagCodeList);
        // 查询对比车型
        params.put("carId", competitorCarId);
        List<Long> competitorTagValueList = getCount(params, tagCodeList);
        List<List<Long>> allTageValueList = new ArrayList<>();
        // 两个数据集合结合合并到一个集合
        allTageValueList.add(ownerTagValueList);
        allTageValueList.add(competitorTagValueList);

        result.put("name", tagList);
        result.put("value", allTageValueList);

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
}
