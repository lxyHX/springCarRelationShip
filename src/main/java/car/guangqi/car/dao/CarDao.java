package car.guangqi.car.dao;

import car.guangqi.car.entity.TagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CarDao {

    List queryAllCars () ;

    List<TagVO> querySubLevelByParentId(String code);

    List<Map<String,Object>> queryEmotionByCodes(Map params);


}
