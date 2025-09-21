package com.mahesh.HMS.mapper;

import com.mahesh.HMS.dto.BillDTO;
import com.mahesh.HMS.model.Bill;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillMapper {

    public BillDTO toDTO(Bill bill) {
        if (bill == null) return null;

        return new BillDTO(
                bill.getId(),
                bill.getAmount(),
                bill.getStatus(),
                bill.getAppointment() != null ? bill.getAppointment().getId() : null
        );
    }

    public List<BillDTO> toDTOList(List<Bill> bills) {
        return bills.stream().map(this::toDTO).toList();
    }
}
