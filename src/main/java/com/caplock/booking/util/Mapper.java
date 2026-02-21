package com.caplock.booking.util;

import com.caplock.booking.entity.dao.BookingEntity;
import com.caplock.booking.entity.dao.BookingItemEntity;
import com.caplock.booking.entity.dao.EventEntity;
import com.caplock.booking.entity.dao.EventTicketConfigEntity;
import com.caplock.booking.entity.dao.InvoiceEntity;
import com.caplock.booking.entity.dao.PaymentEntity;
import com.caplock.booking.entity.dao.TicketEntity;
import com.caplock.booking.entity.dao.UserEntity;
import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.BookingItemDto;
import com.caplock.booking.entity.dto.EventDto;
import com.caplock.booking.entity.dto.EventTicketConfigDto;
import com.caplock.booking.entity.dto.InvoiceDto;
import com.caplock.booking.entity.dto.PaymentDto;
import com.caplock.booking.entity.dto.TicketDto;
import com.caplock.booking.entity.dto.UserDto;

public final class Mapper {
    private Mapper() {
    }

    public static EventDto toDto(EventEntity entity) {
        if (entity == null) return null;
        EventDto dto = new EventDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setBookingOpenAt(entity.getBookingOpenAt());
        dto.setBookingDeadline(entity.getBookingDeadline());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setStatus(entity.getStatus());
        dto.setCategory(entity.getCategory());
        return dto;
    }

    public static EventEntity toEntity(EventDto dto) {
        if (dto == null) return null;
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setLocation(dto.getLocation());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setBookingOpenAt(dto.getBookingOpenAt());
        entity.setBookingDeadline(dto.getBookingDeadline());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setStatus(dto.getStatus());
        entity.setCategory(dto.getCategory());
        return entity;
    }

    public static EventTicketConfigDto toDto(EventTicketConfigEntity entity) {
        if (entity == null) return null;
        EventTicketConfigDto dto = new EventTicketConfigDto();
        dto.setId(entity.getId());
        dto.setEventId(entity.getEventId());
        dto.setTicketType(entity.getTicketType());
        dto.setPrice(entity.getPrice());
        dto.setTotalSeats(entity.getTotalSeats());
        dto.setAvailableSeats(entity.getAvailableSeats());
        dto.setSaleStart(entity.getSaleStart());
        dto.setSaleEnd(entity.getSaleEnd());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    public static EventTicketConfigEntity toEntity(EventTicketConfigDto dto) {
        if (dto == null) return null;
        EventTicketConfigEntity entity = new EventTicketConfigEntity();
        entity.setId(dto.getId());
        entity.setEventId(dto.getEventId());
        entity.setTicketType(dto.getTicketType());
        entity.setPrice(dto.getPrice());
        entity.setTotalSeats(dto.getTotalSeats());
        entity.setAvailableSeats(dto.getAvailableSeats());
        entity.setSaleStart(dto.getSaleStart());
        entity.setSaleEnd(dto.getSaleEnd());
        entity.setVersion(dto.getVersion());
        return entity;
    }

    public static BookingDto toDto(BookingEntity entity) {
        if (entity == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(entity.getId());
        dto.setConfirmationCode(entity.getConfirmationCode());
        dto.setEventId(entity.getEventId());
        dto.setUserId(entity.getUserId());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setDiscountCode(entity.getDiscountCode());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCanceledAt(entity.getCanceledAt());
        dto.setExpiresAt(entity.getExpiresAt());
        return dto;
    }

    public static BookingEntity toEntity(BookingDto dto) {
        if (dto == null) return null;
        BookingEntity entity = new BookingEntity();
        entity.setId(dto.getId());
        entity.setConfirmationCode(dto.getConfirmationCode());
        entity.setEventId(dto.getEventId());
        entity.setUserId(dto.getUserId());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setDiscountCode(dto.getDiscountCode());
        entity.setDiscountAmount(dto.getDiscountAmount());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCanceledAt(dto.getCanceledAt());
        entity.setExpiresAt(dto.getExpiresAt());
        return entity;
    }

    public static BookingItemDto toDto(BookingItemEntity entity) {
        if (entity == null) return null;
        BookingItemDto dto = new BookingItemDto();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBookingId());
        dto.setEventTicketConfigId(entity.getEventTicketConfigId());
        dto.setTicketType(entity.getTicketType());
        dto.setQuantity(entity.getQuantity());
        dto.setPricePerSeat(entity.getPricePerSeat());
        dto.setSubtotal(entity.getSubtotal());
        return dto;
    }

    public static BookingItemEntity toEntity(BookingItemDto dto) {
        if (dto == null) return null;
        BookingItemEntity entity = new BookingItemEntity();
        entity.setId(dto.getId());
        entity.setBookingId(dto.getBookingId());
        entity.setEventTicketConfigId(dto.getEventTicketConfigId());
        entity.setTicketType(dto.getTicketType());
        entity.setQuantity(dto.getQuantity());
        entity.setPricePerSeat(dto.getPricePerSeat());
        entity.setSubtotal(dto.getSubtotal());
        return entity;
    }

    public static PaymentDto toDto(PaymentEntity entity) {
        if (entity == null) return null;
        PaymentDto dto = new PaymentDto();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBookingId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setMethod(entity.getMethod());
        dto.setTransactionId(entity.getTransactionId());
        dto.setProviderResponse(entity.getProviderResponse());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPaidAt(entity.getPaidAt());
        return dto;
    }

    public static PaymentEntity toEntity(PaymentDto dto) {
        if (dto == null) return null;
        PaymentEntity entity = new PaymentEntity();
        entity.setId(dto.getId());
        entity.setBookingId(dto.getBookingId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setMethod(dto.getMethod());
        entity.setTransactionId(dto.getTransactionId());
        entity.setProviderResponse(dto.getProviderResponse());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setPaidAt(dto.getPaidAt());
        return entity;
    }

    public static InvoiceDto toDto(InvoiceEntity entity) {
        if (entity == null) return null;
        InvoiceDto dto = new InvoiceDto();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBookingId());
        dto.setPaymentId(entity.getPaymentId());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setHolderName(entity.getHolderName());
        dto.setHolderEmail(entity.getHolderEmail());
        dto.setSubtotal(entity.getSubtotal());
        dto.setDiscount(entity.getDiscount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setIssuedAt(entity.getIssuedAt());
        dto.setPdfUrl(entity.getPdfUrl());
        return dto;
    }

    public static InvoiceEntity toEntity(InvoiceDto dto) {
        if (dto == null) return null;
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(dto.getId());
        entity.setBookingId(dto.getBookingId());
        entity.setPaymentId(dto.getPaymentId());
        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setHolderName(dto.getHolderName());
        entity.setHolderEmail(dto.getHolderEmail());
        entity.setSubtotal(dto.getSubtotal());
        entity.setDiscount(dto.getDiscount());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setIssuedAt(dto.getIssuedAt());
        entity.setPdfUrl(dto.getPdfUrl());
        return entity;
    }

    public static TicketDto toDto(TicketEntity entity) {
        if (entity == null) return null;
        TicketDto dto = new TicketDto();
        dto.setId(entity.getId());
        dto.setBookingId(entity.getBookingId());
        dto.setBookingItemId(entity.getBookingItemId());
        dto.setEventId(entity.getEventId());
        dto.setTicketType(entity.getTicketType());
        dto.setTicketCode(entity.getTicketCode());
        dto.setSection(entity.getSection());
        dto.setRow(entity.getRow());
        dto.setSeatNumber(entity.getSeatNumber());
        dto.setHolderName(entity.getHolderName());
        dto.setHolderEmail(entity.getHolderEmail());
        dto.setDiscountCode(entity.getDiscountCode());
        dto.setStatus(entity.getStatus());
        dto.setIssuedAt(entity.getIssuedAt());
        dto.setScannedAt(entity.getScannedAt());
        dto.setQrCodeUrl(entity.getQrCodeUrl());
        return dto;
    }

    public static TicketEntity toEntity(TicketDto dto) {
        if (dto == null) return null;
        TicketEntity entity = new TicketEntity();
        entity.setId(dto.getId());
        entity.setBookingId(dto.getBookingId());
        entity.setBookingItemId(dto.getBookingItemId());
        entity.setEventId(dto.getEventId());
        entity.setTicketType(dto.getTicketType());
        entity.setTicketCode(dto.getTicketCode());
        entity.setSection(dto.getSection());
        entity.setRow(dto.getRow());
        entity.setSeatNumber(dto.getSeatNumber());
        entity.setHolderName(dto.getHolderName());
        entity.setHolderEmail(dto.getHolderEmail());
        entity.setDiscountCode(dto.getDiscountCode());
        entity.setStatus(dto.getStatus());
        entity.setIssuedAt(dto.getIssuedAt());
        entity.setScannedAt(dto.getScannedAt());
        entity.setQrCodeUrl(dto.getQrCodeUrl());
        return entity;
    }

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) return null;
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmailHash(entity.getEmailHash());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setRole(entity.getRole());
        dto.setNotificationToken(entity.getNotificationToken());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmailHash(dto.getEmailHash());
        entity.setPasswordHash(dto.getPasswordHash());
        entity.setRole(dto.getRole());
        entity.setNotificationToken(dto.getNotificationToken());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
