package car.guangqi.car.control;

import car.guangqi.car.service.CarService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("car")
public class restCarController {

    @Resource(name="carService")
    private CarService carService;

    @RequestMapping("getCarlist")
    public Object getCarlist () {
        return carService.getCars();
    }
}
