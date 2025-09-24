package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.BillDTO;
import com.mahesh.HMS.dto.PaginatedResponsesDTO;
import com.mahesh.HMS.model.Bill;
import com.mahesh.HMS.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/{appointmentId}")
    public BillDTO addBill(@PathVariable Long appointmentId, @RequestBody Bill bill) {
        return billService.addBill(appointmentId, bill);
    }

    @GetMapping
    public PaginatedResponsesDTO<BillDTO> getAllBills(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Page<BillDTO> billPage = billService.getAllBills(page, size);
        return new PaginatedResponsesDTO<>(
                billPage.getContent(),
                billPage.getNumber(),
                billPage.getSize(),
                billPage.getTotalElements(),
                billPage.getTotalPages()
        );
    }


    @GetMapping("/{id}")
    public BillDTO getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @PutMapping("/{id}")
    public BillDTO updateBillbyID(@PathVariable Long id, @RequestBody Bill bill) {
        return billService.updateBillbyID(id, bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBillbyId(@PathVariable Long id) {
        billService.deleteBillbyId(id);
    }

    @GetMapping("/total/{patientId}")
    public Double getTotalBilledAmount(@PathVariable Long patientId) {
        return billService.getTotalBilledAmountForPatient(patientId);
    }
}