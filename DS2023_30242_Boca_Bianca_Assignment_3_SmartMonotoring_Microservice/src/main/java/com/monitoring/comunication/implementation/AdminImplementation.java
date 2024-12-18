package com.monitoring.comunication.implementation;

import com.monitoring.comunication.entity.Admins;
import com.monitoring.comunication.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminImplementation {

    @Autowired
    private AdminRepository adminRepository;

    public UUID findAdminByUUID(UUID uuid)
    {
        return adminRepository.findById(uuid).get().getIdAdmin();
    }

    public UUID addAdmin(UUID uuid)
    {
        Admins admins =new Admins(uuid);
         this.adminRepository.save(admins);
         return  uuid;
    }

}
