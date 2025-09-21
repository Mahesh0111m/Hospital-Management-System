package com.mahesh.HMS.controller;

import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Bill;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/{appointmentId}")
    public Bill addBill(@PathVariable Long appointmentId , @RequestBody Bill bill){
        return billService.addBill(appointmentId , bill);
    }

    @GetMapping
    public Page<Bill> getAllBills(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size){
        return billService.getAllBills(page , size);
    }

    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id){
        return billService.getBillById(id);
    }

    @PutMapping("/{id}")
    public Bill updateBillbyID(@PathVariable Long id , @RequestBody Bill bill){
        return billService.updateBillbyID(id , bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBillbyId(@PathVariable Long id){

    }
}