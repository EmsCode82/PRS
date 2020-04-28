package com.prsweb.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prsweb.business.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

}
