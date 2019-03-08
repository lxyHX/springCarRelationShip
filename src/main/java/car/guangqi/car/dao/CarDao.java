package car.guangqi.car.dao;

import car.guangqi.car.entity.TagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CarDao {

    List queryAllCars () ;

    List<TagVO> querySubLevelByParentId(String code);

    // 根据二三级指标查询正负极性集合
    List<Map<String,Object>> queryEmotionByCodes(Map params);

    // 根据关键字查询汽车集合
    List<Map>  queryOwnerCarList(String param);

    // 根据关键字查询汽车集合
    List<Map>  queryCompetitorCarList(Map param);

    // 查询一级指标
    List<Map> queryFirstLevelTag();

    List<Map> queryRecomContent(Map param);

    List<Map> queryPMCount(Map param);
}
