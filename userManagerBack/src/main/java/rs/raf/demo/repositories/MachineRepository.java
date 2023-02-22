package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Machine;

import javax.crypto.Mac;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    Machine findMachineById(long id);

}
