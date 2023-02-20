package rs.raf.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.demo.model.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {

    Permission findPermissionById(int id);

}
