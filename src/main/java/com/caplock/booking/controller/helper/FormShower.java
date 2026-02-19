package com.caplock.booking.controller.helper;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.EventDetailsDto;
import org.springframework.ui.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class FormShower {

    public static <T> String showForm(
            Model model,
            long id,
            Function<Long, T> getByIdLong,
            Class<T> dtoClass
    ) {
        boolean editing = false;
        T dto = null;
        if (id > 0 && getByIdLong != null) {
            dto = getByIdLong.apply(id);
            editing = dto != null;
        }


        // Decide view + model attribute name based on dto class
        String attrName;
        String view;

        if (dtoClass == EventDetailsDto.class) {
            attrName = "event";
            view = "events/eventForm";
            model.addAttribute("eventDto", editing ? dto : new EventDetailsDto());
        } else if (dtoClass == BookingDto.class) {
            attrName = "booking";
            view = "bookings/bookingForm";
        } else {
            attrName = "waitList";
            view = "waitList/waitListForm";
        }

        try {


            if (editing) {


                if (dto == null) {

                    dto = dtoClass.getDeclaredConstructor().newInstance();
                }
            } else {
                dto = dtoClass.getDeclaredConstructor().newInstance();
            }

            model.addAttribute("editing", editing);
            model.addAttribute(attrName, dto);
            model.addAttribute("formName", editing ? "Edit" : "Add");
            model.addAttribute("formButton", editing ? "Update" : "Place " + attrName);

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("Failed to prepare form DTO for " + dtoClass.getSimpleName(), e);
        }

        return view;
    }
}
