package car.guangqi.car.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarDao {

    List queryAllCars () ;
}
