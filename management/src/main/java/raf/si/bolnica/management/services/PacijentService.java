package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.Pacijent;

import java.util.UUID;

public interface PacijentService {

    Pacijent fetchPacijentByLBP(UUID lbp);

    Pacijent createPacijent(Pacijent pacijent);

    void deleteById(Long id);
}
