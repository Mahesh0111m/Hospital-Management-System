package com.mahesh.HMS.service;

import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Bill;
import com.mahesh.HMS.model.Patient;
import com.mahesh.HMS.repository.AppointmentRepository;
import com.mahesh.HMS.repository.BillRepository;
import com.mahesh.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;


    public Bill addBill(Long appointmentId , Bill bill){
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new RuntimeException("No Appointments found"));

            bill.setAppointment(appointment);
            return billRepository.save(bill);
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
            return null;
        }
    }

    public Page<Bill> getAllBills(int page , int size){
        try {
            Pageable pageable = PageRequest.of(page , size);
            return billRepository.findAll(pageable);
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
            return null;
        }
    }

    public Bill getBillById(Long id){
        try {
            return billRepository.findById(id).orElse(null);
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
            return null;
        }
    }

    public Bill updateBillbyID(Long id, Bill updatedBill){
        try {
            Optional<Bill> existingBill = billRepository.findById(id);
            if(existingBill.isPresent()){
                Bill b = existingBill.get();
                b.setAmount(updatedBill.getAmount());
                b.setStatus(updatedBill.getStatus());

                Bill savedBill = billRepository.save(b);
                return savedBill;
            }
            else {
                System.out.println("No Bill found");
                return null;
            }
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
            return null;
        }
    }

    public void deleteBillbyId(Long id){
        try {
            billRepository.deleteById(id);
            System.out.println("deleted bill");
        }
        catch (Exception e){
            System.out.println("Exception" + e.getMessage());
        }
    }
}