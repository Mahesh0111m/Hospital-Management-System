package com.mahesh.HMS.service;

import com.mahesh.HMS.dto.BillDTO;
import com.mahesh.HMS.mapper.BillMapper;
import com.mahesh.HMS.model.Appointment;
import com.mahesh.HMS.model.Bill;
import com.mahesh.HMS.repository.AppointmentRepository;
import com.mahesh.HMS.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillMapper billMapper;

    public BillDTO addBill(Long appointmentId, Bill bill) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        bill.setAppointment(appointment);
        Bill savedBill = billRepository.save(bill);

        return billMapper.toDTO(savedBill);
    }

    public Page<BillDTO> getAllBills(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> bills = billRepository.findAll(pageable);

        return bills.map(billMapper::toDTO);
    }

    public BillDTO getBillById(Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        return bill.map(billMapper::toDTO).orElse(null);
    }

    public BillDTO updateBillbyID(Long id, Bill updatedBill) {
        Optional<Bill> existingBill = billRepository.findById(id);

        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();
            bill.setAmount(updatedBill.getAmount());
            bill.setStatus(updatedBill.getStatus());

            Bill savedBill = billRepository.save(bill);
            return billMapper.toDTO(savedBill);
        } else {
            return null;
        }
    }

    public void deleteBillbyId(Long id) {
        billRepository.deleteById(id);
    }

    public List<BillDTO> getBillsByPatientId(Long patientId){
        List<Bill> bills = billRepository.findAllByPatientId(patientId);
        return bills.stream()
                .map(billMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Double getTotalBilledAmountForPatient(Long patientId) {
        Double totalAmount = billRepository.getTotalBilledAmountByPatientId(patientId);
        return totalAmount != null ? totalAmount : 0.0;
    }

    public Double getTotalRevenueByDoctor(Long doctorId) {
        Double totalRevenue = billRepository.getTotalRevenueByDoctor(doctorId);
        return totalRevenue != null ? totalRevenue : 0.0;
    }


}
