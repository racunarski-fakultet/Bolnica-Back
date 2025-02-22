package raf.si.bolnica.laboratory.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.Uput;
import raf.si.bolnica.laboratory.repositories.UputRepository;

import java.util.List;

@Service
@Transactional("transactionManager")
public class UputServiceImpl implements UputService {

    @Autowired
    UputRepository repository;


    @Override
    public Uput getUput(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Uput fetchUputById(Long id) {
        return repository.findByUputId(id);
    }

    @Override
    public List<Uput> getUputi() {
        return repository.findAll();
    }

    @Override
    public Uput saveUput(Uput uput) {
        return repository.save(uput);
    }

    @Override
    public Uput updateUput(Uput uput) {
        return repository.save(uput);
    }

    @Override
    public void deleteUput(Long id) {
        repository.deleteById(id);
    }
}
