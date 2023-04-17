package utez.edu.mx.smartfitutez.models.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utez.edu.mx.smartfitutez.security.entity.UserLR;

import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    @Modifying
    @Query(value = "INSERT INTO record (weight, date, id_userlr) VALUES (:weight, :date, :userId)", nativeQuery = true)
    void insertRecord(@Param("weight") String weight, @Param("date") Date date, @Param("userId") Long userId);

    @Query(value = "SELECT r FROM Record r WHERE r.userLR.id = :userId")
    List<Record> findAllByUserId(@Param("userId") Long userId);
}
