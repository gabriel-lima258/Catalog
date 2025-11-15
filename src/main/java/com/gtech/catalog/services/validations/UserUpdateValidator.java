package com.gtech.catalog.services.validations;

import com.gtech.catalog.dto.UserUpdateDTO;
import com.gtech.catalog.dto.errors.FieldMessageDTO;
import com.gtech.catalog.entities.User;
import com.gtech.catalog.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request; // guarda valores de requisição http

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        List<FieldMessageDTO> list = new ArrayList<>();

        // dicionario de valores da url / map -> id = 2 response
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        if (uriVars != null && uriVars.get("id") != null) {
            long userId = Long.parseLong(uriVars.get("id"));

            // tratando o erro de email unico e validando se o id encontrado é diferente do email dono
            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                User user = repository.findByEmail(dto.getEmail());
                if (user != null && userId != user.getId()) {
                    list.add(new FieldMessageDTO("email", "Este email já existe"));
                }
            }
        }

        for (FieldMessageDTO e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}