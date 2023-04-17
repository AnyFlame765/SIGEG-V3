package utez.edu.mx.smartfitutez.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.smartfitutez.models.record.Record;
import utez.edu.mx.smartfitutez.models.record.RecordRepository;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.repository.UserLrRepository;
import utez.edu.mx.smartfitutez.security.service.recordService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/record")
@CrossOrigin
public class RecordController {

    @Autowired
    private UserLrRepository userRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private recordService recordService;

    @PostMapping("/insertRecord")
    @ResponseBody
    public void insertRecord(@RequestParam("weight") String weight, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @RequestParam("userId") Long userId) {
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User with id " + userId + " and role 'Alumno' not found.");
        }
        recordRepository.insertRecord(weight, date, userId);
    }

    @GetMapping("/{userId}")
    public List<Record> findAllByUserId(@PathVariable Long userId) {
        return recordService.findAllByUserId(userId);
    }

}
