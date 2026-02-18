package com.caplock.booking.controller.helper;

import com.caplock.booking.entity.dto.BookingDto;
import com.caplock.booking.entity.dto.EventDto;
import org.springframework.ui.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.function.LongFunction;

public class FormShower {

    public static <T> String showForm(
            Model model,
            long id,
            LongFunction<T> getById,
            Class<T> dtoClass
    ) {
        boolean editing = (id > 0) && getById.apply(id) != null;

        String noun;
        String view;
        if (dtoClass == EventDto.class) {
            noun = "event";
            view = "events/eventForm";
        } else if(dtoClass== BookingDto.class) {
            noun = "booking";
            view = "bookings/bookingForm";
        }else{
            noun = "waitList";
            view = "waitList/waitListForm";
        }
        try {
            model.addAttribute(noun,dtoClass.getDeclaredConstructor().newInstance());
            model.addAttribute("formName", editing ? "Edit" : "Add");
            model.addAttribute("formButton", editing ? "Update" : "Place " + noun);

        }catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            //log
        }

        return view;
    }
}
