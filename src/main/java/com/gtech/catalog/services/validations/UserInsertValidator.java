package com.gtech.catalog.services.validations;

import java.util.ArrayList;
import java.util.List;

import com.gtech.catalog.dto.UserInsertDTO;
import com.gtech.catalog.dto.errors.FieldMessageDTO;
import com.gtech.catalog.entities.User;
import com.gtech.catalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessageDTO> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        // tratando o erro de email unico
        User user = repository.findByEmail(dto.getEmail());
        if (user != null) {
            list.add(new FieldMessageDTO("email", "Este email já existe"));
        }

        for (FieldMessageDTO e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}