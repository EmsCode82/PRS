package com.prsweb.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prsweb.business.JsonResponse;
import com.prsweb.business.Vendor;
import com.prsweb.db.VendorRepository;

@RestController
@RequestMapping("/vendors")
public class VendorController {
	@Autowired
	private VendorRepository vendorRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Vendor> vendors = vendorRepo.findAll();
		if (vendors.size() > 0) {
			jr = JsonResponse.getInstance(vendors);
		} else {
			jr = JsonResponse.getErrorInstance("No vendor Found.");
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Vendor> vendor = vendorRepo.findById(id);
		if (vendor.isPresent()) {
			jr = JsonResponse.getInstance(vendor.get());
		} else {
			jr = JsonResponse.getErrorInstance("No vendor found for Id." + id);
		}
		return jr;
	}

	@PostMapping("/")
	public JsonResponse createVendor(@RequestBody Vendor p) {
		JsonResponse jr = null;

		try {
			p = vendorRepo.save(p);
			jr = JsonResponse.getInstance(p);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error creating vendor: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	@PutMapping("/")
	public JsonResponse updateVendor(@RequestBody Vendor v) {
		JsonResponse jr = null;

		try {
			v = vendorRepo.save(v);
			jr = JsonResponse.getInstance(v);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error updating vendor: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	@DeleteMapping("/{id}")
	public JsonResponse deleteVendor(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			vendorRepo.deleteById(id);
			jr = JsonResponse.getInstance("Vendor id: " + id + " deleted successfully");
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error deleting vendor: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
}
