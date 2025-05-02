package com.jupiter.store.module.imports.service;

import com.jupiter.store.module.imports.repository.ImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportService {
    @Autowired
    private ImportRepository importRepository;

}

