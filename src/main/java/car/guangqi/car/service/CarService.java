package car.guangqi.car.service;

import car.guangqi.car.dao.CarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarDao carDao;

    public List getCars () {
        return carDao.queryAllCars();
    }
}
