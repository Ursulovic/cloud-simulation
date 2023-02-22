package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Machine;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Machine findMachineById(long id);



    @Query(value = "select m from Machine m where (:name is null or m.name like :name) " +
            "and (:status is null or m.status = :status)" +
            "and (:dateTo is null or m.creationDate between :dateFrom and :dateTo)")
    List<Machine> searchMachines(@Param("name") String name,
                                 @Param("status") String status,
                                 @Param("dateFrom") Long dateFrom,
                                 @Param("dateTo") Long dateTo);

}
