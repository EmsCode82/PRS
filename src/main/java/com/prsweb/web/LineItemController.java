package com.prsweb.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prsweb.business.*;
import com.prsweb.db.LineItemRepository;
import com.prsweb.db.RequestRepository;

@RestController
@RequestMapping("/line-items")
public class LineItemController {
	@Autowired
	private LineItemRepository lineItemRepo;
	@Autowired
	private RequestRepository requestRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<LineItem> lineItems = lineItemRepo.findAll();
		if (lineItems.size() > 0) {
			jr = JsonResponse.getInstance(lineItems);
		} else {
			jr = JsonResponse.getErrorInstance("No line items Found.");
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<LineItem> lineItem = lineItemRepo.findById(id);
		if (lineItem.isPresent()) {
			jr = JsonResponse.getInstance(lineItem.get());
		} else {
			jr = JsonResponse.getErrorInstance("No line items found for Id." + id);
		}
		return jr;
	}

	@GetMapping("/lines-for-pr/{id}")
	public JsonResponse getLineItemsForRequest(@PathVariable int id) {
		JsonResponse jr = null;
		List<LineItem> lineItems = lineItemRepo.findAllByRequestId(id);

		if (!lineItems.isEmpty()) {
			jr = JsonResponse.getInstance(lineItems);
		} else {
			jr = JsonResponse.getErrorInstance("No lineItems found for request id: " + id);
		}
		return jr;
	}

	
	@PostMapping("/")
	public JsonResponse createLineItemRecal(@RequestBody LineItem li) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.save(li));
			recalculateLineItemTotal(li.getRequest());
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse updateLineItemRecal(@RequestBody LineItem li) {
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(li.getId())) {
				jr = JsonResponse.getInstance(lineItemRepo.save(li));
				recalculateLineItemTotal(li.getRequest());
			} else {
				jr = JsonResponse.getInstance("Error updating LineItem.  id: " + li.getId() + " doesn't exist!");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItemRecal(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			if (lineItemRepo.existsById(id)) {
				LineItem li = lineItemRepo.findById(id).get();
				Request r = li.getRequest();
				lineItemRepo.deleteById(id);
				jr = JsonResponse.getInstance("Delete successful!");
				recalculateLineItemTotal(r);
			} else {
				jr = JsonResponse.getInstance("Error deleting Line item id: " + id + " doesn't exist!");
			}
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	private void recalculateLineItemTotal(Request r) {
		List<LineItem> lineitem = lineItemRepo.findAllByRequestId(r.getId());
		double total = 0.0;
		for (LineItem line: lineitem) {
			total += line.getQuantity()*line.getProduct().getPrice();
		}
		r.setTotal(total);
		try {
			System.out.println("recalc saving request:  "+r);
			requestRepo.save(r);
		} catch (Exception e) {
			throw e;
		}
	}

//	private void recalculateLineItemTotal(Request request, Double total) {
//		Optional<Request> id = null;
//		List<LineItem> liList = lineItemRepo.findAllByRequest(id);
//		for (LineItem li : liList) {
//			total += li.getTotal();
//		}
//		request.setTotal(total);
//		try {
//			requestRepo.save(request);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
}
