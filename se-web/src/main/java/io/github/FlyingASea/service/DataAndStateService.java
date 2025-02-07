package io.github.FlyingASea.service;

import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service("DataAndStateService")
public class DataAndStateService {
    @Resource
    private DataService dataService;

    @Resource
    private StateService stateService;

    public void changeSome(String type, String id, String data) {
        StateEntity past = stateService.getState(id);
        System.out.println("past: " + past.getLast_update());
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        System.out.println("now: " + last_update + "  type: " + type);
        stateService.changeState(type, data, past.getId(), last_update);
        StateEntity now = stateService.getState(id);
        System.out.println("now State: " + now.getLast_update());
        dataService.createState(now);
    }

    public void changeOrCreateStateAndData(Map<String, Object> data) {
        StateEntity now = stateService.getState((String) data.get("id"));
        if (now == null) {
            if (data.get("begin") == null) {
                LocalDateTime dateTime = LocalDateTime.now();
                Timestamp last_update = Timestamp.valueOf(dateTime);
                stateService.createState(data, last_update);
            } else {
                stateService.createState(data, (Timestamp) data.get("begin"));
            }

        } else {
            stateService.changeState(data);
        }
        dataService.createState(data);
    }

    public Map<String, Object> generateBill(String id) {
        Map<String, Object> bill = stateService.removeState(id);
        stateService.removeState(id);
        return bill;
    }

}
