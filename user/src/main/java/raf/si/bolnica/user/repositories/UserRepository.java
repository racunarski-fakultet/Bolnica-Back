package raf.si.bolnica.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.user.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    User findByKorisnickoIme(String korisnickoIme);

    User findByLicniBrojZaposlenog(Long lbz);
}
