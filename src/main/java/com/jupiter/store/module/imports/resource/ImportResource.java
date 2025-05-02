package com.jupiter.store.module.imports.resource;

import com.jupiter.store.module.imports.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imports")
public class ImportResource {
    @Autowired
    private ImportService importService;
}
