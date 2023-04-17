package utez.edu.mx.smartfitutez.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.smartfitutez.models.record.Record;
import utez.edu.mx.smartfitutez.models.record.RecordRepository;
import utez.edu.mx.smartfitutez.security.entity.UserLR;
import utez.edu.mx.smartfitutez.security.repository.UserLrRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class recordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserLrRepository userRepository;
    public void insertRecord(String weight, Date date, Long userId) {
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User with id " + userId + " and role 'Alumno' not found.");
        }
        recordRepository.insertRecord(weight, date, userId);
    }

    public List<Record> findAllByUserId(Long userId) {
        Optional<UserLR> user = userRepository.findByIdAndAlumnoRole(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User with id " + userId + " and role 'Alumno' not found.");
        }
        return recordRepository.findAllByUserId(userId);
    }
}
